package project.exceptions;

public class ForbiddenException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(message);
	}
	
	public ForbiddenException() {
		super("Vous n'avez pas les droits pour accéder à cette resource");
	}
}
