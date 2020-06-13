package project.exceptions;

public class PhotoNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;

	public PhotoNotFoundException() {
		super("Aucune photo trouvée");
	}
	
}
