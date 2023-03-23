package pl.marcinm312.filesconverter.mergepdf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterWebController;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/app/mergePdf/")
public class MergePdfWebController extends AbstractConverterWebController {

	private static final String PAGE_TITLE = "Połącz pliki PDF";
	private static final String ACCEPTED_FILE_TYPES = ".zip";

	private final Converter mergePdfConverter;

	@Override
	protected Converter getConverter() {
		return mergePdfConverter;
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
