package project.exceptions;

public class BookingNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;
	
	public BookingNotFoundException() {
		super("Aucune réservation correspondante");
	}

}
