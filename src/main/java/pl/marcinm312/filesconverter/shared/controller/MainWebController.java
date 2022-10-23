package pl.marcinm312.filesconverter.shared.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class MainWebController {

	private static final String MAIN_PAGE = "mainPage";

	@GetMapping
	public String getMainPage() {
		return MAIN_PAGE;
	}
}
