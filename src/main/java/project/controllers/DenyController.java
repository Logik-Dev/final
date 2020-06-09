package project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import project.models.ErrorResponse;

@RestController
@RequestMapping("/deny")
public class DenyController {
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@GetMapping
	public ErrorResponse deny() {
		return new ErrorResponse("Authentification requise", 403);
	}
}
