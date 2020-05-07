package project.controllers;

import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.models.entities.User;
import project.services.JwtService;
import project.services.UserService;

@AutoConfigureMockMvc
@SpringBootTest
public class AbstractControllerTest {
	
    @MockBean
    protected UserService userService;
    
    @Autowired
    protected MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    protected ObjectMapper mapper;
    
    protected static final User user = new User();
    
    public AbstractControllerTest() {
        user.setId(1);
        user.setPassword("password");
        user.setEmail("email");
    }
    
    protected HttpHeaders getAuthorizationHeaders() {
        MultiValueMap<String, String> h = new LinkedMultiValueMap<>();
        h.add("Authorization", "Bearer " + jwtService.generateToken(user));
        return new HttpHeaders(h);
    }
    
    protected void mockAuthentication() {
        when(userService.loadUserByUsername("email")).thenReturn(user);
    }
    
}
