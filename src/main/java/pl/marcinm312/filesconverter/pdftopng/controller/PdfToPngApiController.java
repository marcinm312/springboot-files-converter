package pl.marcinm312.filesconverter.pdftopng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterApiController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pdfToPng")
public class PdfToPngApiController implements AbstractConverterApiController {

	private final Converter pdfToPngConverter;

	@Override
	public Converter getConverter() {
		return pdfToPngConverter;
	}
}
