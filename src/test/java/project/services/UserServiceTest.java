package project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.exceptions.BadCredentialsException;
import project.exceptions.EmailExistsException;
import project.exceptions.ForbiddenException;
import project.exceptions.UserNotFoundException;
import project.models.entities.Room;
import project.models.entities.User;
import project.repositories.UserRepository;

@SpringBootTest
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	AuthenticationManager authManager;

	@Mock
	JwtService jwtService;

	@InjectMocks
	private UserService userService;

	@Spy
	private static User user = new User();

	private static Room room = new Room();

	private static Authentication auth;

	@BeforeAll
	public static void setUp() {
		user.setId(1);
		user.setPassword("password");
		user.setEmail("email");
		user.setFirstname("firstname");
		user.setLastname("lastname");
		auth = createAuth();
		room.setId(1);
	}

	@Test
	void testCreate() {
		// Arrange
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
		when(userRepository.save(Mockito.any())).thenReturn(user);
		// Act
		userService.create(user);
		// Assert
		verify(userRepository, times(1)).existsByEmail(user.getEmail());
		verify(passwordEncoder, times(1)).encode("password");
		verify(userRepository, times(1)).save(Mockito.any());
	}

	@Test
	void testCreateEmailExist() {
		// Arrange
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
		// Assert
		assertThrows(EmailExistsException.class, () -> userService.create(user));

	}

	@Test
	void testAuthenticate() {
		// Arrange
		when(authManager.authenticate(Mockito.any())).thenReturn(auth);
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(jwtService.generateToken(user)).thenReturn("token");
		// Act
		String token = userService.authenticate(user);
		// Assert
		assertEquals("token", token);
	}

	@Test
	void testAuthenticateThrowsBadCredentialsException() {
		// Arrange
		when(authManager.authenticate(Mockito.any())).thenThrow(RuntimeException.class);

		// Assert
		assertThrows(BadCredentialsException.class, () -> userService.authenticate(user));

	}

	@Test
	void testFindById() {
		// Arrange
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		// Act
		User result = userService.findById(1, user);
		// Assert
		verify(userRepository, times(1)).findById(1);
		assertEquals(1, result.getId());
		assertEquals("email", result.getEmail());
	}

	@Test
	void testFindByIdThrowsForbiddenException() {
		assertThrows(ForbiddenException.class, () -> userService.findById(2, user));
		assertThrows(ForbiddenException.class, () -> userService.findById(1, null));

	}

	@Test
	void testFindByIdThrowsUserNotFoundException() {
		// Arrange
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		// Assert
		assertThrows(UserNotFoundException.class, () -> userService.findById(1, user));

	}

	@Test
	void testLoadByUsername() {
		// Arrange
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		// Act
		UserDetails result = userService.loadUserByUsername(user.getEmail());
		// Assert
		verify(userRepository, times(1)).findByEmail(user.getEmail());
		assertEquals(user.getEmail(), result.getUsername());
	}

	@Test
	void testLoadByUsernameThrowsUserNotFoundException() {
		// Arrange
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
		// Assert
		assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));
		verify(userRepository, times(1)).findByEmail(user.getEmail());
	}

	@Test
	void testEmailExists() {
		// Arrange
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
		// Assert
		assertTrue(userService.emailExists(user.getEmail()));
	}

	@Test
	void testUpdate() {
		// Arrange
		user.setPassword("password");
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		// Act
		User result = userService.update(user, user);
		// Assert
		assertEquals(user.getId(), result.getId());
		verify(userRepository, times(1)).findById(user.getId());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testUpdateNothing() {
		// Arrange
		User userToUpdate = new User();
		userToUpdate.setId(1);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userToUpdate));
		when(userRepository.save(Mockito.any())).thenReturn(userToUpdate);
		// Act
		User result = userService.update(userToUpdate, userToUpdate);
		// Assert
		assertEquals(userToUpdate.getId(), result.getId());
	}

	@Test
	void testUpdateThrowExceptions() {
		// Arrange
		User badUser = new User();
		badUser.setId(2);
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
		// Assert
		assertThrows(ForbiddenException.class, () -> userService.update(user, null));
		assertThrows(ForbiddenException.class, () -> userService.update(user, badUser));
		assertThrows(UserNotFoundException.class, () -> userService.update(user, user));
	}

	@Test
	void testAddRoomToFavorites() {
		// Arrange
		user.setFavorites(new HashSet<Room>());
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		// Act
		userService.addRoomToFavorites(user, room);
		// Assert
		assertTrue(user.getFavorites().contains(room));
		assertEquals(1, user.getFavorites().size());
	}

	@Test
	void testRemoveFromFavorites() {
		// Arrange
		user.getFavorites().add(room);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		// Act
		userService.addRoomToFavorites(user, room);
		// Assert
		assertTrue(!user.getFavorites().contains(room));
		assertEquals(0, user.getFavorites().size());

	}

	@Test
	void testAddRoomToFavoritesThrowsException() {
		// Arrange
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
		// Assert
		assertThrows(UserNotFoundException.class, () -> userService.addRoomToFavorites(user, room));
	}

	private static Authentication createAuth() {
		return new Authentication() {

			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return user.getEmail();
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

			}

			@Override
			public boolean isAuthenticated() {
				return false;
			}

			@Override
			public Object getPrincipal() {
				return null;
			}

			@Override
			public Object getDetails() {
				return null;
			}

			@Override
			public Object getCredentials() {
				return null;
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		};
	}

}
