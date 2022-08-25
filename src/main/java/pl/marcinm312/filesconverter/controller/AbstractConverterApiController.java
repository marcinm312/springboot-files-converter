package pl.marcinm312.filesconverter.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.converter.Converter;
import pl.marcinm312.filesconverter.exception.FileException;

public interface AbstractConverterApiController {

	Converter getConverter();

	@PostMapping
	default ResponseEntity<ByteArrayResource> convertMultipartFile(@RequestParam MultipartFile file) throws FileException {

		return getConverter().executeConversion(file);
	}
}
