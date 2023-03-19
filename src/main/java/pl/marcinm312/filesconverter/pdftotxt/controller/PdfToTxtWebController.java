package pl.marcinm312.filesconverter.pdftotxt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterWebController;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/app/pdfToTxt/")
public class PdfToTxtWebController extends AbstractConverterWebController {

	private static final String PAGE_TITLE = "PDF -> TXT";
	private static final String ACCEPTED_FILE_TYPES = ".pdf";

	private final Converter pdfToTxtConverter;

	@Override
	protected Converter getConverter() {
		return pdfToTxtConverter;
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
