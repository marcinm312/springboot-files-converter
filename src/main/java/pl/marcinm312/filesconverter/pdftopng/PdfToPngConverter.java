package pl.marcinm312.filesconverter.pdftopng;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.model.FileData;
import pl.marcinm312.filesconverter.shared.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PdfToPngConverter implements Converter {

	@Getter
	private final List<String> allowedExtensions = List.of("pdf");

	@Override
	public ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException {

		validateFile(file);
		return convertMultipartFile(file);
	}

	private ResponseEntity<ByteArrayResource> convertMultipartFile(MultipartFile file) throws FileException {

		String oldFileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", oldFileName);

		try (InputStream inputStream = file.getInputStream();
			 PDDocument document = PDDocument.load(inputStream)) {

			PDFRenderer pdfRenderer = new PDFRenderer(document);
			List<FileData> filesToZip = new ArrayList<>();

			int numberOfPages = document.getNumberOfPages();
			log.info("Start to convert file: {}", oldFileName);

			for (int page = 0; page < numberOfPages; page++) {
				processPdfPage(pdfRenderer, filesToZip, page, numberOfPages);
			}

			byte[] convertedFile = FileUtils.createZipFile(filesToZip);
			String newFileName = FileUtils.getFileNameWithNewExtension(oldFileName, "zip");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas konwertowania pliku PDF do plików PNG: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private void processPdfPage(PDFRenderer pdfRenderer, List<FileData> filesToZip, int page, int numberOfPages)
			throws IOException, FileException {

		int pageToDisplay = page + 1;
		BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			String imageFormat = "png";
			ImageIO.write(bufferedImage, imageFormat, outputStream);

			String fileName = String.format("pdf-%d.%s", pageToDisplay, imageFormat);
			byte[] bytes = outputStream.toByteArray();
			filesToZip.add(new FileData(fileName, bytes));

			log.info("Page {} of {} processed", pageToDisplay, numberOfPages);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas przetwarzania %s. strony z pliku PDF " +
					"zawierającego %s stron: %s", pageToDisplay, numberOfPages, e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}
}
