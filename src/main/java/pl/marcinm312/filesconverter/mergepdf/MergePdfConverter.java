package pl.marcinm312.filesconverter.mergepdf;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.*;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MergePdfConverter implements Converter {

	private final List<String> allowedExtensionsToUnzip = List.of("pdf");

	@Getter
	private final List<String> allowedExtensions = List.of("zip");

	@Override
	public ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException {
		validateFile(file);
		return convertMultipartFile(file);
	}

	private ResponseEntity<ByteArrayResource> convertMultipartFile(MultipartFile file) throws FileException {

		String oldFileName = FileUtils.getFileName(file);
		log.info("Start to load file: {}", oldFileName);

		List<FileData> unzippedFiles = FileUtils.readZipFromMultipartFile(file, allowedExtensionsToUnzip);

		log.info("Start to convert file: {}", oldFileName);
		List<RandomAccessRead> filesToMerge = prepareFilesToMerge(unzippedFiles);

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			PDFMergerUtility pdfMerger = new PDFMergerUtility();
			pdfMerger.setDocumentMergeMode(PDFMergerUtility.DocumentMergeMode.OPTIMIZE_RESOURCES_MODE);
			pdfMerger.addSources(filesToMerge);
			pdfMerger.setDestinationStream(outputStream);

			pdfMerger.mergeDocuments(IOUtils.createTempFileOnlyStreamCache());

			byte[] convertedFile = outputStream.toByteArray();
			String newFileName = FileUtils.getFileNameWithNewExtension(oldFileName, "pdf");
			log.info("Converted file: {}", newFileName);
			return FileUtils.generateResponseWithFile(convertedFile, newFileName, "application/pdf");

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas łączenia plików PDF: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private List<RandomAccessRead> prepareFilesToMerge(List<FileData> unzippedFiles) throws FileException {

		List<RandomAccessRead> filesToMerge = new ArrayList<>();
		int numberOfFiles = unzippedFiles.size();
		int i = 0;

		for (FileData unzippedFile : unzippedFiles) {

			byte[] unzippedFileBytes = unzippedFile.bytes();
			try (ByteArrayInputStream inputStream = new ByteArrayInputStream(unzippedFileBytes)) {
				RandomAccessReadBuffer buffer = RandomAccessReadBuffer.createBufferFromStream(inputStream);
				filesToMerge.add(buffer);
			} catch (Exception e) {
				String errorMessage = String.format("Błąd podczas przetwarzania pliku PDF: %s", e.getMessage());
				log.error(errorMessage, e);
				throw new FileException(errorMessage);
			}
			i++;
			log.info("File {} of {} processed", i, numberOfFiles);
		}
		return filesToMerge;
	}
}
