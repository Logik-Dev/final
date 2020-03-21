package project.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ForbiddenException extends AuthenticationException{
	
	private static final long serialVersionUID = 1L;

	public ForbiddenException() {
		super("Vous n'êtes pas autorisé à accéder à cette ressource");
	}

}
