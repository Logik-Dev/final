package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Vous avez déjà commenté cette salle")
public class CommentExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
