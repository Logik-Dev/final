package project.exceptions;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super("Aucun résultat trouvé");
	}
	
	public NotFoundException(Object id) {
		super("L'identifiant " + id + " est introuvable");
	}
} 
