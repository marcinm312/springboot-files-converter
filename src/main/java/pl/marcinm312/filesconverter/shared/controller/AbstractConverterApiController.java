package pl.marcinm312.filesconverter.shared.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.FileException;

public interface AbstractConverterApiController {

	Converter getConverter();

	@PostMapping
	default ResponseEntity<ByteArrayResource> convertMultipartFile(@RequestPart MultipartFile file) throws FileException {

		return getConverter().executeConversion(file);
	}
}
