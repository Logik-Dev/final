package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.exceptions.ConflictException;
import project.exceptions.UserNotFoundException;
import project.models.User;
import project.repositories.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;

	public User findById(int id) throws UserNotFoundException {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
	}
	
	public User save(User user) throws ConflictException {
		if(userRepository.existsByEmail(user.getEmail())) 
			throw new ConflictException("Cette adresse est déjà utilisée");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	public User findByEmail(String email) throws UserNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException());
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UserNotFoundException{
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException());
		return user;
	}
}
