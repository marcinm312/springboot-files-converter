package pl.marcinm312.filesconverter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.converter.WordToPdfConverter;
import pl.marcinm312.filesconverter.exception.FileException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WordToPdfApiController {

	private final WordToPdfConverter wordToPdfConverter;

	@PostMapping("/convertWordToPdf")
	public ResponseEntity<ByteArrayResource> convertWordToPdf(@RequestParam MultipartFile file) throws FileException {

		return wordToPdfConverter.executeConversion(file);
	}
}
