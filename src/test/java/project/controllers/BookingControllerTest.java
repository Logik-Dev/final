package project.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import project.exceptions.BookingNotFoundException;
import project.exceptions.UnavailableException;
import project.models.entities.Booking;
import project.services.BookingService;


class BookingControllerTest extends AbstractControllerTest {
	
	@MockBean
	private BookingService bookingService;
	
	private Booking booking = new Booking();
	
	private static final String URL = "/api/bookings";
	
	@BeforeEach
	public void setUpBeforeEach() {
		booking.setId(1);
		booking.setClient(user);
		booking.setPrice(15);
		mockAuthentication();
	}
	
	@Test
	void testCreate() throws Exception {
		when(bookingService.create(Mockito.any())).thenReturn(booking);
		ResultActions result = mvc.perform(post(URL).headers(getAuthorizationHeaders()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(booking)))
			.andExpect(status().isCreated());
		assertOnBooking(result);
	}
	
	@Test
	void testCreateUnavailable() throws Exception {
		when(bookingService.create(Mockito.any())).thenThrow(UnavailableException.class);
		mvc.perform(post(URL).headers(getAuthorizationHeaders()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(booking)))
			.andExpect(status().isConflict());
	}
	
	@Test
	void testFindById() throws Exception {
		when(bookingService.findById(booking.getId())).thenReturn(booking);
		ResultActions result = mvc.perform(get(URL + "/" + booking.getId()).headers(getAuthorizationHeaders()))
				.andExpect(status().isOk());
		assertOnBooking(result);
	}
	
	@Test
	void testFindByIdWrongId() throws Exception {
		when(bookingService.findById(booking.getId())).thenThrow(BookingNotFoundException.class);
		mvc.perform(get(URL + "/" + booking.getId()).headers(getAuthorizationHeaders()))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void testFindByRoom() throws Exception {
		when(bookingService.findByRoom(1)).thenReturn(List.of(booking));
		ResultActions result = mvc.perform(get(URL + "/rooms/1").headers(getAuthorizationHeaders())).andExpect(status().isOk());
		assertOnList(result);
	}
	
	@Test
	void testFindByRoomNoResult() throws Exception {
		when(bookingService.findByRoom(1)).thenThrow(BookingNotFoundException.class);
		mvc.perform(get(URL + "/rooms/1").headers(getAuthorizationHeaders())).andExpect(status().isNotFound());		
	}
	
	private void assertOnBooking (ResultActions result) throws Exception {
		result
			.andExpect(jsonPath("$.id").value(booking.getId()))
			.andExpect(jsonPath("$.price").value(booking.getPrice()))
			.andExpect(jsonPath("$.client.id").value(user.getId()));
	}
	
	private void assertOnList (ResultActions result) throws Exception {
		result
			.andExpect(jsonPath("$.[0].id").value(booking.getId()))
			.andExpect(jsonPath("$.[0].price").value(booking.getPrice()))
			.andExpect(jsonPath("$.[0].client.id").value(user.getId()));
	}

}
