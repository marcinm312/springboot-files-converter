package pl.marcinm312.filesconverter.wordtopdf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterWebController;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/app/wordToPdf")
public class WordToPdfWebController extends AbstractConverterWebController {

	private static final String PAGE_TITLE = "Word -> PDF";
	private static final String ACCEPTED_FILE_TYPES = ".doc, .docx";

	private final Converter wordToPdfConverter;


	public Converter getConverter() {
		return wordToPdfConverter;
	}

	public String getPageTitle() {
		return PAGE_TITLE;
	}

	public String getAcceptedFileTypes() {
		return ACCEPTED_FILE_TYPES;
	}
}
