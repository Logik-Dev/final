package project.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.ResultActions;

import project.exceptions.ConflictException;
import project.exceptions.UserNotFoundException;

class UserControllerTest extends AbstractControllerTest {
	
	private static final String URL = "/api/users";
	
	@Test
	void testCreate() throws Exception {
		when(userService.create(Mockito.any())).thenReturn(user);
		ResultActions result = mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
			.andExpect(status().isCreated());
		assertOnUser(result);
	}
	
	@Test
	void testCreateEmailExists() throws Exception {
		when(userService.create(Mockito.any())).thenThrow(ConflictException.class);
		mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
			.andExpect(status().isConflict());
	}
	
	@Test
	void testLogin() throws Exception {
		when(userService.authenticate(Mockito.any())).thenReturn("token");
		mvc.perform(post(URL + "/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
			.andExpect(status().isAccepted())
			.andExpect(jsonPath("$.jwt").value("token"));
	}
	
	@Test
	void testLoginBadCredentials() throws Exception {
		when(userService.authenticate(Mockito.any())).thenThrow(BadCredentialsException.class);
		mvc.perform(post(URL + "/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
			.andExpect(status().isForbidden());
	}
	
	@Test
	void testFindById() throws Exception {
		mockAuthentication();
		when(userService.findById(user.getId(), user)).thenReturn(user);
		ResultActions result = mvc.perform(get(URL + "/" + user.getId()).headers(getAuthorizationHeaders()))
			.andExpect(status().isOk());
		assertOnUser(result);
	}
	
	@Test
	void testFindByIdWrongId() throws Exception {
		mockAuthentication();
		when(userService.findById(user.getId(), user)).thenThrow(UserNotFoundException.class);
		mvc.perform(get(URL + "/" + user.getId()).headers(getAuthorizationHeaders()))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void testEmailExists() throws Exception {
		when(userService.emailExists(user.getEmail())).thenReturn(true);
		mvc.perform(get(URL + "?email=" + user.getEmail()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result").value(true));
	}
	
	@Test
	void testUpdate() throws Exception {
		mockAuthentication();
		when(userService.update(Mockito.any(), Mockito.any())).thenReturn(user);
		ResultActions result = mvc.perform(put(URL).headers(getAuthorizationHeaders()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		assertOnUser(result);
	}
	
	@Test
	void testDelete() throws Exception {
		mockAuthentication();
		doNothing().when(userService).delete(user.getId(), user);
		mvc.perform(delete(URL + "/" + user.getId()).headers(getAuthorizationHeaders()))
			.andExpect(status().isNoContent());
	}
	
	private void assertOnUser(ResultActions result) throws Exception {
		result
			.andExpect(jsonPath("$.id").value(user.getId()))
			.andExpect(jsonPath("$.email").value(user.getEmail()));
	}
}
