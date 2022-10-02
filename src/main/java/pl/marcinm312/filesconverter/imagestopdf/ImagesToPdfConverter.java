package pl.marcinm312.filesconverter.imagestopdf;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.BadRequestException;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.model.FileData;
import pl.marcinm312.filesconverter.shared.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ImagesToPdfConverter implements Converter {

	private final List<String> allowedExtensionsToUnzip = List.of("jpg", "jpeg", "tif", "tiff", "gif", "bmp", "png");
	private final List<String> allowedExtensionsToRead = List.of("zip");

	private static final PDRectangle a4Rectangle = PDRectangle.A4;
	private final PDRectangle a4RotatedRectangle = new PDRectangle(a4Rectangle.getHeight(), a4Rectangle.getWidth());
	private static final float MINIMAL_MARGIN = 20;

	@Override
	public ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException {

		validateFile(file);
		return convertMultipartFile(file);
	}

	private ResponseEntity<ByteArrayResource> convertMultipartFile(MultipartFile file) throws FileException {

		String oldFileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", oldFileName);

		List<FileData> unzippedFiles = FileUtils.readZipFile(file, allowedExtensionsToUnzip);
		int numberOfFiles = unzippedFiles.size();

		if (unzippedFiles.isEmpty()) {
			String errorMessage = String.format("Plik ZIP musi zawierać przynajmniej jeden plik o następującym rozszerzeniu: %s",
					String.join(", ", allowedExtensionsToUnzip));
			log.error(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		log.info("Start to convert file: {}", oldFileName);
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 PDDocument document = new PDDocument()) {

			int i = 0;
			for (FileData unzippedFile : unzippedFiles) {
				processUnzippedFile(document, unzippedFile);
				i++;
				log.info("File {} of {} processed", i, numberOfFiles);
			}

			document.save(outputStream);
			byte[] convertedFile = outputStream.toByteArray();
			String newFileName = FileUtils.getFileNameWithNewExtension(oldFileName, "pdf");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas konwertowania obrazów do pliku PDF: %s", e.getMessage());
			log.error(errorMessage);
			throw new FileException(errorMessage);
		}
	}

	private void processUnzippedFile(PDDocument document, FileData unzippedFile) throws IOException, FileException {

		String unzippedFileName = unzippedFile.getName();
		log.info("Processing image from file: {}", unzippedFileName);
		byte[] unzippedFileBytes = unzippedFile.getBytes();
		PDImageXObject image = PDImageXObject.createFromByteArray(document, unzippedFileBytes, unzippedFileName);

		float originalImageWidth = image.getWidth();
		float originalImageHeight = image.getHeight();

		PDPage page;
		float pageWidth;
		float pageHeight;

		if (originalImageHeight >= originalImageWidth) {
			page = new PDPage(a4Rectangle);
			pageWidth = a4Rectangle.getWidth();
			pageHeight = a4Rectangle.getHeight();
		} else {
			page = new PDPage(a4RotatedRectangle);
			pageWidth = a4RotatedRectangle.getWidth();
			pageHeight = a4RotatedRectangle.getHeight();
		}

		document.addPage(page);
		addImageToPage(document, image, originalImageWidth, originalImageHeight, page, pageWidth, pageHeight);
	}

	private void addImageToPage(PDDocument document, PDImageXObject image, float originalImageWidth, float originalImageHeight, PDPage page, float pageWidth, float pageHeight) throws FileException {

		try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
			float scale = (pageWidth - (MINIMAL_MARGIN * 2)) / originalImageWidth;
			if (scale > 1) {
				scale = 1;
			}
			float imageWidth = originalImageWidth * scale;
			float imageHeight = originalImageHeight * scale;
			float xMargin = MINIMAL_MARGIN;
			if (scale == 1) {
				xMargin = (pageWidth - imageWidth) / 2;
			}
			float yMargin = (pageHeight - imageHeight) / 2;

			if (yMargin <= 0) {
				scale = (pageHeight - (MINIMAL_MARGIN * 2)) / originalImageHeight;
				if (scale > 1) {
					scale = 1;
				}
				imageWidth = originalImageWidth * scale;
				imageHeight = originalImageHeight * scale;
				yMargin = MINIMAL_MARGIN;
				if (scale == 1) {
					yMargin = (pageHeight - imageHeight) / 2;
				}
				xMargin = (pageWidth - imageWidth) / 2;
			}

			contentStream.drawImage(image, xMargin, yMargin, imageWidth, imageHeight);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas dodawania obrazu do pliku PDF: %s", e.getMessage());
			log.error(errorMessage);
			throw new FileException(errorMessage);
		}
	}

	private void validateFile(MultipartFile file) {
		FileUtils.validateFileExtension(file, allowedExtensionsToRead);
	}
}
