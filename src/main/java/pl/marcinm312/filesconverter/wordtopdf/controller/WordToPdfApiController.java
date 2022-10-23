package pl.marcinm312.filesconverter.wordtopdf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterApiController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wordToPdf")
public class WordToPdfApiController implements AbstractConverterApiController {

	private final Converter wordToPdfConverter;

	@Override
	public Converter getConverter() {
		return wordToPdfConverter;
	}
}
