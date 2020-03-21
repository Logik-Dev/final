package project.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.exceptions.ForbiddenException;
import project.models.entities.User;
import project.models.responses.JwtResponse;
import project.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
public class UserController {
	
	@Autowired
	private UserService userService;
		
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id, @AuthenticationPrincipal User user) throws ForbiddenException{
		if(user == null || id != user.getId()) {
			throw new ForbiddenException();
		}
		return ResponseEntity.ok(userService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<User> create(@RequestBody User user) {
		return ResponseEntity.status(HttpStatus.CREATED).body((userService.save(user)));
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody User user) {
		JwtResponse response = new JwtResponse(userService.authenticate(user));
		return ResponseEntity.ok(response);
	}
	
	@PutMapping
	public ResponseEntity<User> update(@RequestBody User user, @AuthenticationPrincipal User loggedUser) throws ForbiddenException{
		if(user == null || user.getId() != loggedUser.getId()) throw new ForbiddenException();
		return ResponseEntity.ok(userService.update(user));
	}

}
