package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.models.AuthenticationRequest;
import project.models.AuthenticationResponse;
import project.models.User;
import project.security.JwtUtil;
import project.services.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
	
	private final UserService userService;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtUtil jwtUtil;
	
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable int id) {
		return ResponseEntity.ok(userService.findById(id));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> create(@RequestBody User user) {
		return ResponseEntity.ok(userService.save(user));
	}
	
	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest auth) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));
		}
		catch(BadCredentialsException e) {
			throw new Exception("Bad credentials", e);
		}
		final UserDetails userDetails = userService
				.loadUserByUsername(auth.getEmail());
		return new AuthenticationResponse(jwtUtil.generateToken(userDetails));
	}
	
}
