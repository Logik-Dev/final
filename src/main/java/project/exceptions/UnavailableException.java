package project.exceptions;

public class UnavailableException extends ConflictException {

	private static final long serialVersionUID = 1L;
	
	public UnavailableException() {
		super("La salle n'est pas disponible");
	}

}
