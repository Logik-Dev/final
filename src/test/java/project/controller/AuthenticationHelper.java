package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import project.models.entities.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationHelper {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    public String getToken() throws Exception {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword("changeme");
        ResultActions result = mvc.perform(post("/api/users/login")
                .content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        return result.andReturn().getResponse().getContentAsString();
    }
}
