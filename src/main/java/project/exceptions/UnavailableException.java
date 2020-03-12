package project.exceptions;

public class UnavailableException extends ConflictException {
	
	private static final long serialVersionUID = 1L;

	public UnavailableException() {
		super("Cette date est déja réservée");
	}
}
