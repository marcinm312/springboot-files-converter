package pl.marcinm312.filesconverter.pdftopng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.controller.AbstractConverterWebController;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/app/pdfToPng")
public class PdfToPngWebController extends AbstractConverterWebController {

	private static final String PAGE_TITLE = "PDF -> PNG";
	private static final String ACCEPTED_FILE_TYPES = ".pdf";

	private final Converter pdfToPngConverter;

	@Override
	protected Converter getConverter() {
		return pdfToPngConverter;
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
