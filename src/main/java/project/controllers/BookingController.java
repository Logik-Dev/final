package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.models.Booking;
import project.models.BookingRequest;
import project.services.BookingService;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingController {

	private final BookingService bookingService;

	@PostMapping()
	public ResponseEntity<Booking> create(@RequestBody BookingRequest booking) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.save(booking));
	}

	@GetMapping()
	public List<Booking> findByRoom(@RequestParam int roomId) {
		return bookingService.findByRoom(roomId);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Booking> changeBookingStatus(@PathVariable int id, @RequestParam String status,
			@RequestParam int userId) {
		return ResponseEntity.ok(bookingService.changeBookingStatus(id, status, userId));
	}

}
