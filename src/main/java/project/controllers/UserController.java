package project.controllers;


import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import project.models.entities.Room;
import project.models.entities.User;
import project.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public User create(@RequestBody User user) {
		return userService.create(user);
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping("/login")
	public Map<String, String> login(@RequestBody User user) {
		return Collections.singletonMap("jwt", userService.authenticate(user));
	}
	
	@GetMapping("/{id}")
	public User findById(@PathVariable int id, @AuthenticationPrincipal User user) {
		return userService.findById(id, user);
	}
	
	@GetMapping
	public Map<String, Boolean> emailExists(@RequestParam String email){
		return Collections.singletonMap("result", userService.emailExists(email));
	}
	
	@PutMapping
	public User update(@RequestBody User user, @AuthenticationPrincipal User loggedUser) {
		return userService.update(user, loggedUser);
	}
	
	@PutMapping("/favorites")
	public User favorites(@RequestBody Room room, @AuthenticationPrincipal User loggedUser) {
		return userService.addRoomToFavorites(loggedUser, room);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id, @AuthenticationPrincipal User loggedUser) {
		userService.delete(id, loggedUser);
	}
	
}
