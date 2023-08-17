package pl.marcinm312.filesconverter.jpgtopng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterWebController;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/app/jpgToPng/")
public class JpgToPngWebController extends AbstractConverterWebController {

	private static final String PAGE_TITLE = "JPG -> PNG";
	private static final String ACCEPTED_FILE_TYPES = ".jpg, .jpeg";

	private final Converter jpgToPngConverter;

	@Override
	protected Converter getConverter() {
		return jpgToPngConverter;
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
