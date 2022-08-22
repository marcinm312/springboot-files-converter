package pl.marcinm312.filesconverter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.marcinm312.filesconverter.converter.WordToPdfConverter;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/app")
public class WordToPdfWebController {

	private static final String CONVERTER_PAGE = "converterPage";
	private static final String RESULT = "result";

	private final WordToPdfConverter wordToPdfConverter;

	@GetMapping("/wordToPdf")
	public String getConverterPage(Model model) {
		model.addAttribute(RESULT, "");
		return CONVERTER_PAGE;
	}

	@PostMapping("/wordToPdf")
	public Object convertWordToPdf(@RequestParam("file") MultipartFile file, Model model, HttpServletResponse response) {

		try {
			return wordToPdfConverter.executeConversion(file);
		} catch (Exception e) {
			log.error("Error converting the file: {}", e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			model.addAttribute(RESULT, e.getMessage());
			return CONVERTER_PAGE;
		}
	}
}
