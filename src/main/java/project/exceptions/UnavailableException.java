package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT, reason = "Cette date est déjà réservée")
public class UnavailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
