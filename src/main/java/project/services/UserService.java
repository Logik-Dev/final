package project.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.exceptions.BadCredentialsException;
import project.exceptions.ConflictException;
import project.exceptions.ForbiddenException;
import project.exceptions.UserNotFoundException;
import project.models.entities.Room;
import project.models.entities.User;
import project.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtUtil;

	@Autowired
	AuthenticationManager authManager;

	/**
	 * Enregistrer un nouvel utilisateur et hasher son mot de passe
	 * 
	 * @param user un objet de type User à enregistrer
	 * @return l'objet de type User contenant un identifiant unique
	 * @throws ConflictException si l'adresse mail est déja utilisée
	 */
	public User create(User user) throws ConflictException {
		if (userRepository.existsByEmail(user.getEmail()))
			throw new ConflictException();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	/**
	 * Authentifier un utilisateur avec JWT
	 * 
	 * @param user un objet de type User contenant les identifiants de connexion
	 * @return un token de type String
	 * @throws BadCredentialsException si l'authentification a échouée
	 */
	public String authenticate(User user) throws BadCredentialsException {
		User authenticatedUser;
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			authenticatedUser = findByEmail(user.getEmail());
		} catch (RuntimeException e) {
			throw new BadCredentialsException();
		}
		return jwtUtil.generateToken(authenticatedUser);
	}

	/**
	 * Rechercher un utilisateur par son id
	 * 
	 * @param id   l'identifiant unique de l'utilisateur à trouver
	 * @param user l'utilisateur authentifié
	 * @return un objet de type User
	 * @throws UserNotFoundException si l'utilisateur est introuvable
	 * @throws ForbiddenException    si l'id ne correspond pas à celui de
	 *                               l'utilisateur
	 */
	public User findById(int id, User user) throws UserNotFoundException, ForbiddenException {
		if (user.getId() != id)
			throw new ForbiddenException();
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
	}

	/**
	 * Rechercher un utilisateur par son email
	 * 
	 * @param email l'adresse mail de l'utilisateur à trouver
	 * @return un objet de type User
	 * @throws UserNotFoundException si l'utilisateur est introuvable
	 */
	private User findByEmail(String email) throws UserNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		return findByEmail(email);
	}

	/**
	 * Verifier si l'email est déjà utilisée
	 * 
	 * @param email l'adresse mail à vérifier
	 * @return true si l'adresse mail est déjà utilisée false sinon
	 */
	public boolean emailExists(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * Modifier un utilisateur
	 * 
	 * @param user       un objet de type User à enregistrer
	 * @param loggedUser l'utilisateur faisant la requète
	 * @return un objet de type User modifié
	 * @throws ForbiddenException si l'id de l'objet ne correspond pas à l'id de
	 *                            l'utilisateur connecté
	 */
	public User update(User user, User loggedUser) throws ForbiddenException {
		if (loggedUser.getId() != user.getId())
			throw new ForbiddenException();
		User dbUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException());

		if (!StringUtils.isBlank(user.getFirstname()))
			dbUser.setFirstname(user.getFirstname());
		if (!StringUtils.isBlank(user.getLastname()))
			dbUser.setFirstname(user.getLastname());
		if (!StringUtils.isBlank(user.getEmail()))
			dbUser.setFirstname(user.getEmail());
		if (!StringUtils.isBlank(user.getPassword()))
			dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(dbUser);

	}

	public User addRoomToFavorites(User loggedUser, Room room) {
		User dbUser = userRepository.findById(loggedUser.getId()).orElseThrow(() -> new UserNotFoundException());
		if(dbUser.getFavorites().contains(room)) {
			dbUser.getFavorites().remove(room);
		} else {
			dbUser.getFavorites().add(room);
		}
		return userRepository.save(dbUser);
	}

	/**
	 * Supprimer un utilisateur
	 * 
	 * @param id         l'identifiant de l'utilisateur à supprimer
	 * @param loggedUser l'utilisateur faisant la requète
	 * @throws ForbiddenException    si l'id de l'objet ne correspond pas à l'id de
	 *                               l'utilisateur connecté
	 * @throws UserNotFoundException si l'utilisateur à supprimer est introuvable
	 */
	public void delete(int id, User loggedUser) throws ForbiddenException, UserNotFoundException {
		if (loggedUser.getId() != id)
			throw new ForbiddenException();
		if (!userRepository.existsById(id))
			throw new UserNotFoundException();
		userRepository.deleteById(id);
	}

}
