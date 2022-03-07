package pl.marcinm312.filesconverter.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

	private FileUtils() {

	}

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
}
