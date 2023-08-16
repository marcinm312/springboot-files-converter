package pl.marcinm312.filesconverter.jpgtopng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterApiController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jpgToPng")
public class JpgToPngApiController implements AbstractConverterApiController {

	private final Converter jpgToPngConverter;

	@Override
	public Converter getConverter() {
		return jpgToPngConverter;
	}
}
