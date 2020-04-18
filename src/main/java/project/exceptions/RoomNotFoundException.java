package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Aucune salle trouvée")
public class RoomNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
