package pl.marcinm312.filesconverter.mergepdf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterApiController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mergePdf")
public class MergePdfApiController implements AbstractConverterApiController {

	private final Converter mergePdfConverter;

	@Override
	public Converter getConverter() {
		return mergePdfConverter;
	}
}
