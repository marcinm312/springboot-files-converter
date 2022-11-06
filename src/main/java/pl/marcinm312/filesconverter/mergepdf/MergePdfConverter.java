package pl.marcinm312.filesconverter.mergepdf;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.model.FileData;
import pl.marcinm312.filesconverter.shared.utils.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Component
public class MergePdfConverter implements Converter {

	private final List<String> allowedExtensionsToUnzip = List.of("pdf");
	private final List<String> allowedExtensionsToRead = List.of("zip");

	private final MemoryUsageSetting memoryUsageSetting = MemoryUsageSetting.setupMainMemoryOnly();

	@Override
	public ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException {
		validateFile(file);
		return convertMultipartFile(file);
	}

	private ResponseEntity<ByteArrayResource> convertMultipartFile(MultipartFile file) throws FileException {

		String oldFileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", oldFileName);

		List<FileData> unzippedFiles = FileUtils.readZipFromMultipartFile(file, allowedExtensionsToUnzip);
		int numberOfFiles = unzippedFiles.size();

		log.info("Start to convert file: {}", oldFileName);
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			PDFMergerUtility pdfMerger = new PDFMergerUtility();
			pdfMerger.setDestinationStream(outputStream);

			int i = 0;
			for (FileData unzippedFile : unzippedFiles) {
				processUnzippedFile(pdfMerger, unzippedFile);
				i++;
				log.info("File {} of {} processed", i, numberOfFiles);
			}

			pdfMerger.mergeDocuments(memoryUsageSetting);

			byte[] convertedFile = outputStream.toByteArray();
			String newFileName = FileUtils.getFileNameWithNewExtension(oldFileName, "pdf");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName);

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas łączenia plików PDF: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private void processUnzippedFile(PDFMergerUtility pdfMerger, FileData unzippedFile) throws FileException {

		String unzippedFileName = unzippedFile.name();
		log.info("Processing PDF file: {}", unzippedFileName);
		byte[] unzippedFileBytes = unzippedFile.bytes();

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(unzippedFileBytes)) {
			pdfMerger.addSource(inputStream);
		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas przetwarzania pliku PDF: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private void validateFile(MultipartFile file) {
		FileUtils.validateFileExtension(file, allowedExtensionsToRead);
	}
}
