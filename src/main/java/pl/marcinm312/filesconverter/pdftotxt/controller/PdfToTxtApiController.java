package pl.marcinm312.filesconverter.pdftotxt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterApiController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pdfToTxt")
public class PdfToTxtApiController implements AbstractConverterApiController {

	private final Converter pdfToTxtConverter;

	@Override
	public Converter getConverter() {
		return pdfToTxtConverter;
	}
}
