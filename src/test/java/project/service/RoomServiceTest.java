package project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import project.exceptions.NotFoundException;
import project.models.entities.Booking;
import project.models.entities.Room;
import project.repositories.RoomRepository;
import project.services.RoomService;

@SpringBootTest
class RoomServiceTest {

	@Mock
	private RoomRepository roomRepository;
	
	@InjectMocks
	private RoomService roomService;

	private static Room room = new Room();

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		room.setId(Long.valueOf(1));
		Booking booking = new Booking();
		booking.setRoom(room);
		room.setBookings(Set.of(booking));
		
	}

	@Test
	void testSaveOrUpdate() {
		// arrange
		when(roomRepository.save(room)).thenReturn(room);

		// act
		Room result = roomService.create(room);

		// assert
		assertEquals(1, result.getId());
	}

	@Test
	void testFindById() {
		// arrange
		when(roomRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(room));
		when(roomRepository.findById(Long.valueOf(2))).thenReturn(Optional.empty());

		// act
		Room result = roomService.findById(Long.valueOf(1));

		// assert
		assertEquals(1, result.getId());
		assertThrows(NotFoundException.class, () -> roomService.findById(Long.valueOf(2)));
	}
	/**
	@Test
	void testFindByCity() {
		// arrange
		when(roomRepository.findByCity("nantes")).thenReturn(Arrays.asList(room));
		when(roomRepository.findByCity("unknown")).thenReturn(new ArrayList<>());

		// act
		List<Room> result = roomService.findByCity("nantes");

		// assert
		assertEquals(1, result.get(0).getId());
		assertThrows(NotFoundException.class, () -> roomService.findByCity("unknown"));

	}

	@Test
	void testFindByCityAndDate() {
		// arrange
		when(roomRepository.findByCity("nantes")).thenReturn(Arrays.asList(room));

		// act
		List<Room> result = roomService.findByCityAndDate("nantes", "01/01/2020", "11:00", "12:00");

		// assert
		assertEquals(1, result.get(0).getId());
		
		assertThrows(NotFoundException.class,
				() -> roomService.findByCityAndDate("nantes", "01/01/2020", "10:00", "11:00")); // -> Heure déjà réservée
	}
	*/
	@Test
	void testFindAll() {
		// arrange
		when(roomRepository.findAll()).thenReturn(Arrays.asList(room));
		
		// act
		List<Room> result = roomService.findAll();
		
		// assert
		assertEquals(1, result.get(0).getId());
	}
	
	@Test
	void testAllFindNothing() {
		// arrange
		when(roomRepository.findAll()).thenReturn(new ArrayList<>());
		
		// assert
		assertThrows(NotFoundException.class, () -> roomService.findAll());
	}


}
