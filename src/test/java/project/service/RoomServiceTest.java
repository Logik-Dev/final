package project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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

import project.exceptions.RoomNotFoundException;
import project.models.Booking;
import project.models.Room;
import project.repositories.RoomRepository;
import project.services.RoomService;
import project.utils.DateUtils;

@SpringBootTest
class RoomServiceTest {

	@Mock
	private RoomRepository roomRepository;
	
	@InjectMocks
	private RoomService roomService;

	private static Room room = new Room();

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		room.setId(1);
		Booking booking = new Booking();
		booking.setDates(Set.of(DateUtils.parseDate("01/01/2020")));
		booking.setStart(DateUtils.parseTime("10:00"));
		booking.setEnd(DateUtils.parseTime("11:00"));
		booking.setDuration(1);
		booking.setRoom(room);
		room.setBookings(Arrays.asList(booking));
		
	}

	@Test
	void testSaveOrUpdate() {
		// arrange
		when(roomRepository.save(room)).thenReturn(room);

		// act
		Room result = roomService.save(room, 1);

		// assert
		assertEquals(1, result.getId());
	}

	@Test
	void testFindById() {
		// arrange
		when(roomRepository.findById(1)).thenReturn(Optional.of(room));
		when(roomRepository.findById(2)).thenReturn(Optional.empty());

		// act
		Room result = roomService.findById(1);

		// assert
		assertEquals(1, result.getId());
		assertThrows(RoomNotFoundException.class, () -> roomService.findById(2));
	}

	@Test
	void testFindByCity() {
		// arrange
		when(roomRepository.findByCity("nantes")).thenReturn(Arrays.asList(room));
		when(roomRepository.findByCity("unknown")).thenReturn(new ArrayList<>());

		// act
		List<Room> result = roomService.findByCity("nantes");

		// assert
		assertEquals(1, result.get(0).getId());
		assertThrows(RoomNotFoundException.class, () -> roomService.findByCity("unknown"));

	}

	@Test
	void testFindByCityAndDate() {
		// arrange
		when(roomRepository.findByCity("nantes")).thenReturn(Arrays.asList(room));

		// act
		List<Room> result = roomService.findByCityAndDate("nantes", "01/01/2020", "11:00", "12:00");

		// assert
		assertEquals(1, result.get(0).getId());
		
		assertThrows(RoomNotFoundException.class,
				() -> roomService.findByCityAndDate("nantes", "01/01/2020", "10:00", "11:00")); // -> Heure déjà réservée
	}

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
		assertThrows(RoomNotFoundException.class, () -> roomService.findAll());
	}

	// TODO refactoring to comment
	void testGetAverageGrade() {
		fail("Not yet implemented");
	}


}
