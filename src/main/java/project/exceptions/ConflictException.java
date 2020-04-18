package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Cette ressource existe déjà")
public class ConflictException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
}
