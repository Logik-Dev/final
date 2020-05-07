package project.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import project.exceptions.UserNotFoundException;
import project.models.Role;
import project.services.AdminService;

class AdminControllerTest extends AbstractControllerTest {

	@MockBean
	private AdminService adminService;
	
	private static final String URL = "/api/admin";
	
	@BeforeEach
	public void setUpBeforeEach() {
		user.setRoles(Set.of(Role.ADMIN));
		mockAuthentication();
	}
	
	@Test
	void testUpdate() throws Exception {
		when(adminService.update(2, true)).thenReturn(user);
		
		mvc.perform(put(URL + "?userId=2&locked=true" ).headers(getAuthorizationHeaders()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(user.getId()))
			.andExpect(jsonPath("$.email").value(user.getEmail()));
	}
	
	@Test
	void testUpdateUserNotFound() throws Exception {
		when(adminService.update(2, false)).thenThrow(UserNotFoundException.class);

		mvc.perform(put(URL + "?userId=2&locked=false").headers(getAuthorizationHeaders()))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void testUpdateLockAdmin() throws Exception {
		when(adminService.update(1, true)).thenReturn(user);
		mvc.perform(put(URL + "?userId=1&locked=true").headers(getAuthorizationHeaders()))
			.andExpect(status().isForbidden());
	}

}
