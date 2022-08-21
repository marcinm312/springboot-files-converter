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

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("")
public class WordToPdfWebController {

	private static final String MAIN_PAGE = "mainPage";
	private static final String RESULT = "result";

	private final WordToPdfConverter wordToPdfConverter;

	@GetMapping
	public String getMainPage(Model model) {
		model.addAttribute(RESULT, "");
		return MAIN_PAGE;
	}

	@PostMapping("/convertWordToPdf")
	public Object convertWordToPdf(@RequestParam("file") MultipartFile file, Model model) {

		try {
			return wordToPdfConverter.executeConversion(file);
		} catch (Exception e) {
			log.error("Error converting the file: {}", e.getMessage());
			model.addAttribute(RESULT, e.getMessage());
			return MAIN_PAGE;
		}
	}
}
