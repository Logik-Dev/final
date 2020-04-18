package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import project.models.entities.Booking;
import project.services.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("http://localhost:4200")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Booking create(@RequestBody Booking booking) {
		return this.bookingService.create(booking);
	}
	
	@ResponseBody
	@GetMapping("/{id}")
	public Booking read(@PathVariable Long id) {
		return bookingService.get(id);
	}
	
	@GetMapping("/rooms/{roomId}")
	public List<Booking> all(@PathVariable Long roomId) {
		return bookingService.all(roomId);
	}

}
