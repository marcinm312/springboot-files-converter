package pl.marcinm312.filesconverter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalWebExceptionHandler {

	@ExceptionHandler(MultipartException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView handleError1(MultipartException e) {

		log.error(e.getMessage());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("result", e.getMessage());
		modelAndView.setViewName("multipartException");
		return modelAndView;
	}
}
