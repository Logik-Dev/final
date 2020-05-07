package project.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import project.exceptions.CommentExistsException;
import project.models.entities.Comment;
import project.services.CommentService;

class CommentControllerTest extends AbstractControllerTest {

	@MockBean
	private CommentService commentService;
	
	private final static String URL = "/api/comments";
	
	private final Comment comment = new Comment();
	
	@BeforeEach
	public void setUpBeforeEach() {
		comment.setId(1);
		comment.setAuthor(user);
		comment.setContent("content");
		mockAuthentication();
	}
	
	@Test
	void testCreate() throws Exception {
		when(commentService.create(Mockito.any(), Mockito.any())).thenReturn(comment);
		mvc.perform(post(URL).headers(getAuthorizationHeaders()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(comment)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(comment.getId()))
			.andExpect(jsonPath("$.author.id").value(user.getId()))
			.andExpect(jsonPath("$.content").value(comment.getContent()));
	}
	
	@Test
	void testCreateCannotComment() throws Exception {
		when(commentService.create(Mockito.any(), Mockito.any())).thenThrow(CommentExistsException.class);
		mvc.perform(post(URL).headers(getAuthorizationHeaders()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(comment)))
			.andExpect(status().isConflict());
		
	}

}
