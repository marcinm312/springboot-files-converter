package pl.marcinm312.filesconverter.shared;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.exception.FileException;
import pl.marcinm312.filesconverter.shared.utils.FileUtils;

import java.util.List;

public interface Converter {

	List<String> getAllowedExtensions();
	ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException;

	default void validateFile(MultipartFile file) {
		FileUtils.validateFileExtension(file, getAllowedExtensions());
	}
}
