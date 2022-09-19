package pl.marcinm312.filesconverter.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.model.FileToZip;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
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

	public static byte[] createZipFile(List<FileToZip> filesToZip) throws FileException {

		log.info("Start to create ZIP file");
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

			for (FileToZip fileToZip : filesToZip) {
				String fileName = fileToZip.getName();
				ZipEntry zipEntry = new ZipEntry(fileName);
				zipOutputStream.putNextEntry(zipEntry);
				zipOutputStream.write(fileToZip.getBytes());
				zipOutputStream.closeEntry();
				log.info("File {} added to ZIP file", fileName);
			}

			zipOutputStream.finish();
			log.info("ZIP file created");
			return byteArrayOutputStream.toByteArray();

		} catch (Exception e) {
			log.error("Error while generating ZIP file: {}", e.getMessage());
			throw new FileException(e.getMessage());
		}
	}

	public static String getFileNameWithNewExtension(String oldFileName, String newExtension) {

		String[] splitFileName = oldFileName.split("\\.");
		return splitFileName[0].replace("—", "-") + "." + newExtension;
	}
}
