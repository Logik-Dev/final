package project.exceptions;

public class EmailExistsException extends ConflictException {

	private static final long serialVersionUID = 1L;
	
	public EmailExistsException() {
		super("Cette adresse mail est déjà utilisée");
	}
}
