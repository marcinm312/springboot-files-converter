package pl.marcinm312.filesconverter.pngtojpg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterWebController;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/app/pngToJpg/")
public class PngToJpgWebController extends AbstractConverterWebController {

	private static final String PAGE_TITLE = "PNG -> JPG";
	private static final String ACCEPTED_FILE_TYPES = ".png";

	private final Converter pngToJpgConverter;

	@Override
	protected Converter getConverter() {
		return pngToJpgConverter;
	}

	@Override
	protected String getPageTitle() {
		return PAGE_TITLE;
	}

	@Override
	protected String getAcceptedFileTypes() {
		return ACCEPTED_FILE_TYPES;
	}
}
