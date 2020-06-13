package project.exceptions;

public class PriceNotMatchingException extends ConflictException{

	private static final long serialVersionUID = 1L;

	public PriceNotMatchingException() {
		super("Le prix ne correspond pas");
	}
	
	
}
