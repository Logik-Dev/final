package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.exceptions.ConflictException;
import project.exceptions.NotFoundException;
import project.models.entities.User;
import project.repositories.UserRepository;
import project.utils.JwtUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	AuthenticationManager authManager;

	public String authenticate(User user) {
		User authenticatedUser = null;
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			authenticatedUser = findByEmail(user.getEmail());
		} catch (RuntimeException e) {
			throw new BadCredentialsException("Identifiants invalides");
		}
		return jwtUtil.generateToken(authenticatedUser);
	}

	public User findById(Long id) throws NotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public User save(User user) {
		if (userRepository.existsByEmail(user.getEmail()))
			throw new ConflictException("Cette adresse mail est déjà utilisée");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public User findByEmail(String email) throws NotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
		return user;
	}

	public User update(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		return findByEmail(email);
	}
}
