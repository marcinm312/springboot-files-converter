package pl.marcinm312.filesconverter.pdftotxt;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@Component
public class PdfToTxtConverter implements Converter {

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

			log.info("Start to convert file: {}", oldFileName);
			PDFTextStripper pdfTextStripper = new PDFTextStripper();
			String parsedText = pdfTextStripper.getText(document);

			log.info("Start to create TXT file");
			byte[] convertedFile = createTxtFile(parsedText);
			String newFileName = FileUtils.getFileNameWithNewExtension(oldFileName, "txt");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas konwertowania pliku PDF do pliku TXT: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private byte[] createTxtFile(String parsedText) throws FileException {

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 PrintWriter writer = new PrintWriter(outputStream)) {

			writer.print(parsedText);
			writer.flush();

			return outputStream.toByteArray();

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas zapisu pliku TXT: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private void validateFile(MultipartFile file) {
		FileUtils.validateFileExtension(file, allowedExtensions);
	}
}
