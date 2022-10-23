package pl.marcinm312.filesconverter.imagestopdf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterApiController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/imagesToPdf")
public class ImagesToPdfApiController implements AbstractConverterApiController {

	private final Converter imagesToPdfConverter;

	@Override
	public Converter getConverter() {
		return imagesToPdfConverter;
	}
}
