package pl.marcinm312.filesconverter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.converter.Converter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wordToPdf")
public class WordToPdfApiController implements AbstractConverterApiController {

	private final Converter wordToPdfConverter;

	public Converter getConverter() {
		return wordToPdfConverter;
	}
}
