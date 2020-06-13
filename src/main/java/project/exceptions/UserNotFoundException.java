package project.exceptions;

public class UserNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() {
		super("Aucun utilisateur correspondant");
	}

}
