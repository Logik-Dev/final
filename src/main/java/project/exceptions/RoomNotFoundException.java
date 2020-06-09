package project.exceptions;

public class RoomNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;
		
	public RoomNotFoundException() {
		super("Aucune salle trouvée");
	}
}
