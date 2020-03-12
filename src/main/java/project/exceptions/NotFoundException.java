package project.exceptions;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super("Aucun résultat");
	}
	
	public NotFoundException(String prefix, int id) {
		super(prefix + " avec l'id " + id + " est introuvable");
	}
} 
