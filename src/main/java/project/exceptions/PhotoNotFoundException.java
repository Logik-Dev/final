package project.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PhotoNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;
		
	public PhotoNotFoundException(int id) {
		super("La photo", id);
	}

}
