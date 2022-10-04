package pl.marcinm312.filesconverter.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.exception.BadRequestException;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.model.FileData;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class FileUtils {

	public static ResponseEntity<ByteArrayResource> generateResponseWithFile(byte[] bytes, String fileName) {

		ByteArrayResource resource = new ByteArrayResource(bytes);
		return ResponseEntity.ok().contentLength(bytes.length)
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}

	public static String getFileName(MultipartFile multipartFile) {

		String fileName = multipartFile.getOriginalFilename();
		if (fileName == null) {
			fileName = "";
		}
		return fileName;
	}

	public static byte[] createZipFile(List<FileData> filesToZip) throws FileException {

		log.info("Start to create ZIP file");
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

			for (FileData fileData : filesToZip) {
				String fileName = fileData.getName();
				ZipEntry zipEntry = new ZipEntry(fileName);
				zipOutputStream.putNextEntry(zipEntry);
				zipOutputStream.write(fileData.getBytes());
				zipOutputStream.closeEntry();
				log.info("File {} added to ZIP file", fileName);
			}

			zipOutputStream.finish();
			log.info("ZIP file created");
			return byteArrayOutputStream.toByteArray();

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas tworzenia pliku ZIP: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	public static List<FileData> readZipFile(MultipartFile file, List<String> allowedExtensions) throws FileException {

		log.info("Start to read ZIP file");
		try (InputStream inputStream = file.getInputStream();
			 ZipInputStream zis = new ZipInputStream(inputStream)) {

			List<FileData> fileDataList = new ArrayList<>();

			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {

				String fileName = zipEntry.getName();
				log.info("Processing entry with file: {}", fileName);
				boolean isCorrectFile = !zipEntry.isDirectory() &&
						(allowedExtensions == null || allowedExtensions.contains(FilenameUtils.getExtension(fileName).toLowerCase()));
				if (!isCorrectFile) {
					continue;
				}

				byte[] bytes = zis.readAllBytes();
				fileDataList.add(new FileData(fileName, bytes));
				log.info("Entry with file {} added to list", fileName);
			}
			log.info("ZIP file read");
			return fileDataList;

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas odczytywania pliku ZIP: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	public static String getFileNameWithNewExtension(String oldFileName, String newExtension) {

		return FilenameUtils.removeExtension(oldFileName).replace("—", "-") + "." + newExtension;
	}

	public static void validateFileExtension(MultipartFile file, List<String> allowedExtensions) {

		if (file == null || file.isEmpty()) {
			String errorMessage = "Nie wybrano pliku";
			log.error(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		String fileName = FileUtils.getFileName(file).toLowerCase();
		String extension = FilenameUtils.getExtension(fileName);

		if (!allowedExtensions.contains(extension)) {
			String errorMessage = String.format("Nieprawidłowy format pliku. Dozwolone rozszerzenia: %s",
					String.join(", ", allowedExtensions));
			log.error(errorMessage);
			throw new BadRequestException(errorMessage);
		}
	}
}
