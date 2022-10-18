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

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

	public static List<FileData> readZipFromMultipartFile(MultipartFile file, List<String> allowedExtensionsToUnzip)
			throws FileException {

		InputStream inputStream;
		try {
			inputStream = file.getInputStream();
		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas odczytu pliku z żądania: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}

		List<FileData> unzippedFiles = readZipFile(inputStream, allowedExtensionsToUnzip);

		if (unzippedFiles.isEmpty()) {
			String errorMessage = String.format("Plik ZIP musi zawierać przynajmniej jeden plik o następującym rozszerzeniu: %s",
					String.join(", ", allowedExtensionsToUnzip));
			log.error(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		return unzippedFiles;
	}

	public static List<FileData> readZipFile(InputStream inputStream, List<String> allowedExtensions) throws FileException {

		log.info("Start to read ZIP file");
		Charset charset = StandardCharsets.ISO_8859_1;

		int entriesLimit = 5000;
		int sizeLimit = 250 * 1000000; // 250MB
		double compressionRatioLimit = 10;
		int totalSizeArchive = 0;
		int totalEntryArchive = 0;

		try (inputStream;
			 ZipInputStream zis = new ZipInputStream(inputStream, charset)) {

			List<FileData> fileDataList = new ArrayList<>();

			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {

				String fileName = validateFileName(zipEntry.getName());
				log.info("Processing entry with file: {}", fileName);
				boolean isCorrectFile = !zipEntry.isDirectory() &&
						(allowedExtensions == null || allowedExtensions.contains(FilenameUtils.getExtension(fileName).toLowerCase()));
				if (!isCorrectFile) {
					continue;
				}

				try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

					totalEntryArchive ++;

					int nBytes;
					byte[] buffer = new byte[2048];
					int totalSizeEntry = 0;

					while((nBytes = zis.read(buffer)) > 0) {

						outputStream.write(buffer, 0, nBytes);
						totalSizeEntry += nBytes;
						totalSizeArchive += nBytes;

						double compressionRatio = (double) totalSizeEntry / (double) zipEntry.getCompressedSize();
						if(compressionRatio > compressionRatioLimit) {
							// ratio between compressed and uncompressed data is highly suspicious, looks like a Zip Bomb Attack
							break;
						}
					}

					fileDataList.add(new FileData(fileName, outputStream.toByteArray()));
					log.info("Entry with file {} added to list", fileName);
				}

				if(totalSizeArchive > sizeLimit || totalEntryArchive > entriesLimit) {
					// the uncompressed data size is too much for the application resource capacity
					// too many entries in this archive, can lead to inodes exhaustion of the system
					break;
				}
			}
			log.info("ZIP file read");
			return fileDataList;

		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas odczytywania pliku ZIP: %s", e.getMessage());
			log.error(errorMessage, e);
			throw new FileException(errorMessage);
		}
	}

	private static String validateFileName(String fileName) throws IOException {

		String intendedDir = ".";
		File f = new File(fileName);
		String canonicalPath = f.getCanonicalPath();

		File iD = new File(intendedDir);
		String canonicalID = iD.getCanonicalPath();

		if (canonicalPath.startsWith(canonicalID)) {
			return fileName;
		} else {
			String errorMessage = String.format("File %s is outside extraction target directory", fileName);
			log.error(errorMessage);
			throw new IllegalStateException(errorMessage);
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

		String fileName = getFileName(file).toLowerCase();
		String extension = FilenameUtils.getExtension(fileName);

		if (!allowedExtensions.contains(extension)) {
			String errorMessage = String.format("Nieprawidłowy format pliku. Dozwolone rozszerzenia: %s",
					String.join(", ", allowedExtensions));
			log.error(errorMessage);
			throw new BadRequestException(errorMessage);
		}
	}
}
