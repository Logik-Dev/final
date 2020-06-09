package project.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import project.exceptions.ConflictException;
import project.exceptions.ForbiddenException;
import project.exceptions.NotFoundException;
import project.models.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleInternal(Exception e) {
		return new ErrorResponse("Une erreur est survenue", 500);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ErrorResponse handleNotFound(NotFoundException e){
		return new ErrorResponse(e.getMessage(), 404);
	
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	public ErrorResponse handleForbidden(ForbiddenException e) {
		return new ErrorResponse(e.getMessage(), 403);
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ConflictException.class)
	public ErrorResponse handleConfict(ConflictException e) {
		return new ErrorResponse(e.getMessage(), 409);
	}
}
