package project.exceptions;

public class CommentExistsException extends ConflictException {

	private static final long serialVersionUID = 1L;
	
	public CommentExistsException() {
		super("Salle déjà commentée");
	}

}
