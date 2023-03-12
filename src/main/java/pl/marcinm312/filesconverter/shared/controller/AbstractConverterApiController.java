package pl.marcinm312.filesconverter.shared.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.FileException;

public interface AbstractConverterApiController {

	Converter getConverter();

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	default ResponseEntity<ByteArrayResource> convertMultipartFile(@RequestBody MultipartFile file) throws FileException {

		return getConverter().executeConversion(file);
	}
}
