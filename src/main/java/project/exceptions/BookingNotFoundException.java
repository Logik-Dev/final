package project.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookingNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;
	
	public BookingNotFoundException(int id) {
		super("La réservation", id);
	}
	
}
