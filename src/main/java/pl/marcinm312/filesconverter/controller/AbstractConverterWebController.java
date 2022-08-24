package pl.marcinm312.filesconverter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.converter.Converter;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public abstract class AbstractConverterWebController {

	private static final String RESULT = "result";
	private static final String CONVERTER_PAGE = "converterPage";

	abstract Converter getConverter();
	abstract String getPageTitle();
	abstract String getAcceptedFileTypes();


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
			log.error("Error converting the file: {}", e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			model.addAttribute(RESULT, e.getMessage());
			prepareConverterPage(model);
			return CONVERTER_PAGE;
		}
	}

	private void prepareConverterPage(Model model) {
		model.addAttribute("title", getPageTitle());
		model.addAttribute("acceptedFileTypes", getAcceptedFileTypes());
	}
}
