package project.controllers;


import java.util.Collections;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import project.exceptions.ForbiddenException;
import project.models.entities.User;
import project.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
public class UserController {
	
	@Autowired
	private UserService userService;
		
	@GetMapping("/{id}")
	public @ResponseBody User findById(@PathVariable Long id, @AuthenticationPrincipal User user) throws ForbiddenException{
		if(user == null || id != user.getId()) {
			throw new ForbiddenException();
		}
		return userService.findById(id);
	}
	
	@GetMapping("/exists")
	public @ResponseBody Map<String, Boolean> userExists(@RequestParam String email){
		return Collections.singletonMap("result", userService.emailExists(email));
	}
	
	@PostMapping
	public ResponseEntity<User> create(@RequestBody User user) {
		return ResponseEntity.status(HttpStatus.CREATED).body((userService.save(user)));
	}
	
	@PostMapping("/login")
	public @ResponseBody Map<String, String> login(@RequestBody User user) {
		return Collections.singletonMap("jwt", userService.authenticate(user));
	}
	
	@PutMapping
	public ResponseEntity<User> update(@RequestBody User user, @AuthenticationPrincipal User loggedUser) throws ForbiddenException{
		if(user == null || user.getId() != loggedUser.getId()) throw new ForbiddenException();
		return ResponseEntity.ok(userService.update(user));
	}

}
