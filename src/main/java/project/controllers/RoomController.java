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
import project.models.Room;
import project.services.RoomService;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomController {

	private final RoomService roomService;

	@GetMapping("/city/{city}")
	public List<Room> findByCity(@PathVariable String city) {
		return roomService.findByCity(city);
	}

	@GetMapping("/city/{city}/date")
	public List<Room> findByCityAndDate(@PathVariable String city, @RequestParam String date,
			@RequestParam String start, @RequestParam String end) {
		return roomService.findByCityAndDate(city, date, start, end);
	}

	@GetMapping()
	public List<Room> findAll() {
		return roomService.findAll();
	}

	@PostMapping("/user/{ownerId}")
	public ResponseEntity<Room> create(@RequestBody Room room, @PathVariable int ownerId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(room, ownerId));
	}

	@PutMapping
	public ResponseEntity<Room> updateRoom(@RequestBody Room room) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roomService.update(room));
	}

}
