package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.exceptions.ForbiddenException;
import project.models.entities.Booking;
import project.models.entities.User;
import project.models.responses.BooleanResponse;
import project.services.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("http://localhost:4200")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Booking> findById(@PathVariable Long id) {
		return ResponseEntity.ok(bookingService.findById(id));
	}
	
	@PostMapping("/rooms/{roomId}")
	public ResponseEntity<Booking> create(@AuthenticationPrincipal User user, @PathVariable Long roomId,
			@RequestBody Booking booking) {
		if (user == null) {
			throw new ForbiddenException();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.save(roomId, booking, user.getId()));
	}

	@GetMapping("/rooms/{roomId}/available")
	public ResponseEntity<BooleanResponse> checkAvailable(@AuthenticationPrincipal User user, @PathVariable Long roomId,
			@RequestParam String begin, @RequestParam String end, @RequestParam int weekRepetition) {
		if (user == null) {
			throw new ForbiddenException();
		}
		return ResponseEntity.ok(new BooleanResponse(bookingService.checkAvailable(begin, end, weekRepetition, roomId)));
	}

	@GetMapping("/rooms/{roomId}")
	public List<Booking> findByRoom(@PathVariable Long roomId) {
		return bookingService.findByRoom(roomId);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Booking> changeBookingStatus(@PathVariable Long id, @RequestParam String status,
			@AuthenticationPrincipal User user) {
		if (user == null)
			throw new ForbiddenException();
		return ResponseEntity.ok(bookingService.changeBookingStatus(id, status, user.getId()));
	}

}
