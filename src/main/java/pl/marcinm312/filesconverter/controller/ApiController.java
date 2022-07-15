package pl.marcinm312.filesconverter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.converter.WordToPdfConverter;

@RestController
@RequestMapping("/api")
public class ApiController {

	private final WordToPdfConverter wordToPdfConverter;

	@Autowired
	public ApiController(WordToPdfConverter wordToPdfConverter) {
		this.wordToPdfConverter = wordToPdfConverter;
	}

	@PostMapping("/convertWordToPdf")
	public ResponseEntity<ByteArrayResource> convertWordToPdf(@RequestParam MultipartFile file) {

		return wordToPdfConverter.validateAndConvertFile(file);
	}
}
