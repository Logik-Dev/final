package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DayUnavailableException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public DayUnavailableException(String day) {
		super("Cette salle n'est pas disponible le " + day);
	}
}
