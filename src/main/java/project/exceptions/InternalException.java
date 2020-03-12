package project.exceptions;

public class InternalException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InternalException() {
		super("La requète n'a pas pu aboutir suite à une erreur interne");
	}
}
