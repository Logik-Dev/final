package project.utils;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import project.exceptions.ConflictException;
import project.exceptions.InternalException;
import project.exceptions.NotFoundException;
import project.models.responses.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final ErrorResponse response = new ErrorResponse();

	@ExceptionHandler(InternalException.class)
	public ResponseEntity<ErrorResponse> handleInternalErrors(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflict(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentials(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleFrobiddenRoutes(Exception ex) throws IOException {
		return fillResponse(ex, HttpStatus.FORBIDDEN);
	}

	private ResponseEntity<ErrorResponse> fillResponse(Exception ex, HttpStatus statut) {
		response.setStatut(statut.value());
		response.setMessage(ex.getMessage());
		response.setDate(LocalDateTime.now());
		return new ResponseEntity<>(response, statut);
	}

}
