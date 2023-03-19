package pl.marcinm312.filesconverter.shared.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.shared.Converter;
import pl.marcinm312.filesconverter.shared.exception.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;

@Slf4j
public abstract class AbstractConverterWebController {

	private static final String RESULT = "result";
	private static final String CONVERTER_PAGE = "converterPage";

	protected abstract Converter getConverter();
	protected abstract String getPageTitle();
	protected abstract String getAcceptedFileTypes();


	@GetMapping
	public String getConverterPage(Model model) {

		model.addAttribute(RESULT, "");
		prepareConverterPage(model);
		return CONVERTER_PAGE;
	}

	@PostMapping
	public Object convertMultipartFile(@RequestParam("file") MultipartFile file, Model model, HttpServletResponse response) {

		try {
			return getConverter().executeConversion(file);
		} catch (Exception e) {
			String errorMessage = String.format("Błąd podczas konwertowania pliku: %s", e.getMessage());
			log.error(errorMessage);
			if (e instanceof BadRequestException) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			model.addAttribute(RESULT, errorMessage);
			prepareConverterPage(model);
			return CONVERTER_PAGE;
		}
	}

	private void prepareConverterPage(Model model) {
		model.addAttribute("title", getPageTitle());
		model.addAttribute("acceptedFileTypes", getAcceptedFileTypes());
	}
}
