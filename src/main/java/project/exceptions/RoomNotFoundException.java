package project.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoomNotFoundException extends NotFoundException {
	
	private static final long serialVersionUID = 1L;
	
	public RoomNotFoundException(int id) {
		super("La salle", id);
	}
	
}
