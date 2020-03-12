package project.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends NotFoundException{

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(int id) {
		super("L'utilisateur", id);
	}
}
