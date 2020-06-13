package project.exceptions;

public class DayUnavailableException extends ConflictException {

	private static final long serialVersionUID = 1L;
	
	public DayUnavailableException(String day) {
		super("Cette salle n'est pas disponible le " + day);
	}
}
