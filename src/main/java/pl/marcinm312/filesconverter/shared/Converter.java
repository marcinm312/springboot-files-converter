package pl.marcinm312.filesconverter.shared;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.exception.FileException;

public interface Converter {

	ResponseEntity<ByteArrayResource> executeConversion(MultipartFile file) throws FileException;
}
