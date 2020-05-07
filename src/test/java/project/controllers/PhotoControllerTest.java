package project.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import project.exceptions.PhotoNotFoundException;
import project.models.entities.Photo;
import project.services.PhotoService;


class PhotoControllerTest extends AbstractControllerTest {
    
	@MockBean
	private PhotoService photoService;
	
	private final Photo photo = new Photo();
	
	private final static String URL = "/api/photos";
	
	@BeforeEach
	public void setUpBeforeEach() {
		byte[] file = {'a', 'b'};
		photo.setFile(file);
		mockAuthentication();
	}
	
	@Test
	void testCreate() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file.png", photo.getFile());
		MultipartFile[] files = {file};
		doNothing().when(photoService).create(files, 1, user);
		mvc.perform(MockMvcRequestBuilders.multipart(URL + "/rooms/1").file(file).headers(getAuthorizationHeaders()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.result").value("Photos enregistrées"));
	}
	
	@Test
	void testFindById() throws Exception {
		when(photoService.findById(photo.getId())).thenReturn(photo.getFile());
		mvc.perform(get(URL + "/" + photo.getId()))
			.andExpect(status().isOk())
			.andExpect(content().bytes(photo.getFile()));
	}
	
	@Test
	void testFindByIdWrongId() throws Exception {
		when(photoService.findById(photo.getId())).thenThrow(PhotoNotFoundException.class);
		mvc.perform(get(URL + "/" + photo.getId()))
			.andExpect(status().isNotFound());
	}

}
