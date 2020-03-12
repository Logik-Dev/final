package project.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import project.models.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	private final ErrorResponse response = new ErrorResponse();
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflict(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InternalException.class)
	public ResponseEntity<ErrorResponse> handleInternalErrors(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	private ResponseEntity<ErrorResponse> fillResponse(Exception ex, HttpStatus statut) {
		response.setStatut(statut.value());
		response.setMessage(ex.getMessage());
		response.setDate(LocalDateTime.now());
		return new ResponseEntity<>(response, statut);
	}
}
