package pl.marcinm312.filesconverter.shared.exception;

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
	public ModelAndView handleError(MultipartException e) {

		String errorMessage = String.format("MultipartException: %s", e.getMessage());
		log.error(errorMessage);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("result", errorMessage);
		modelAndView.setViewName("multipartException");
		return modelAndView;
	}
}
