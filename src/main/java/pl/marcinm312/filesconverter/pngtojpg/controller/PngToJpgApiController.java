package pl.marcinm312.filesconverter.pngtojpg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterApiController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pngToJpg")
public class PngToJpgApiController implements AbstractConverterApiController {

	private final Converter pngToJpgConverter;

	@Override
	public Converter getConverter() {
		return pngToJpgConverter;
	}
}
