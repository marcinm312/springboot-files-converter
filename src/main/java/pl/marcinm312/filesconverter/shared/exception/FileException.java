package pl.marcinm312.filesconverter.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileException extends Exception {

	public FileException(String message) {
		super(message);
	}
}
