package project.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.controllers.RoomController;
import project.exceptions.RoomNotFoundException;
import project.models.entities.Address;
import project.models.entities.Room;
import project.services.RoomService;

@SpringBootTest
@WebMvcTest(RoomController.class)
class RoomControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RoomService roomService;

	@Autowired
	private ObjectMapper mapper = new ObjectMapper();

	private static Room room = new Room();

	private final String URL = "/api/room";

	private ResultMatcher matcher = jsonPath("$.[0].city").value("nantes");

	private ResultMatcher notFound = status().isNotFound();

	private ResultMatcher ok = status().isOk();

	@BeforeAll
	static void setUpBeforeClass() {
		Address address = new Address();
		address.setCity("nantes");
		room.setAddress(address);
	}
	/**
	@Test
	void testFindByCity() throws Exception {
		when(roomService.findByCity("nantes")).thenReturn(Arrays.asList(room));
		when(roomService.findByCity("unknown")).thenThrow(new NotFoundException());

		mvc.perform(get(URL + "/city/nantes")).andExpect(ok).andExpect(matcher);

		mvc.perform(get(URL + "/city/unknown")).andExpect(notFound);
	}

	@Test
	void testFindByCityAndDate() throws Exception {
		when(roomService.findByCityAndDate("nantes", "22/05/2019", "10:00", "11:00"))
				.thenReturn(Arrays.asList(room));
		when(roomService.findByCityAndDate("unknown", "22/05/2019","10:00", "11:00"))
				.thenThrow(new NotFoundException());

		mvc.perform(get(URL + "/city/nantes/date?date=22/05/2019&start=10:00&end=11:00")).andExpect(ok)
				.andExpect(matcher);

		mvc.perform(get(URL + "/city/unknown/date?date=22/05/2019&start=10:00&end=11:00")).andExpect(notFound);
	}
	 
	@Test
	void testFindAll() throws Exception {
		when(roomService.findAll()).thenReturn(Arrays.asList(room));

		mvc.perform(get(URL)).andExpect(ok).andExpect(matcher);

		when(roomService.findAll(city, zipCode, day)).thenThrow(new RoomNotFoundException());

		mvc.perform(get(URL)).andExpect(notFound);
	}

	@Test
	void testCreate() throws JsonProcessingException, Exception {
		when(roomService.create(room)).thenReturn(room);

		mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(room)))
				.andExpect(status().isCreated());
	}

	@Test
	void testUpdateRoom() throws JsonProcessingException, Exception {
		when(roomService.update(room, Long.valueOf(1))).thenReturn(room);

		mvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(room)))
				.andExpect(status().isCreated());
	}
	*/

}
