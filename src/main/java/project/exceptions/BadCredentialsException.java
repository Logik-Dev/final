package project.exceptions;

public class BadCredentialsException extends ForbiddenException{

	private static final long serialVersionUID = 1L;

	public BadCredentialsException() {
		super("Identifiants invalides");
	}
	
}
