package project.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import project.models.SearchRoomParams;
import project.models.entities.Room;
import project.models.entities.User;
import project.services.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin("http://localhost:4200")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Room create(@RequestBody Room room, @AuthenticationPrincipal User user) {
		return roomService.create(room, user);
	}

	@GetMapping
	public ResponseEntity<Object> all(@RequestParam(required = false) String name,
			@RequestParam(required = false) String city, @RequestParam(required = false) String date,
			@RequestParam(required = false) Integer zipCode, @RequestParam(required = false) Double lat,
			@RequestParam(required = false) Double lon, @RequestParam(required = false) String type,
			@RequestParam(required = false) String equipment, @RequestParam(required = false) String event) {
		if (name != null) {
			return ResponseEntity.ok(Collections.singletonMap("result", roomService.exists(name)));
		}
		SearchRoomParams params = new SearchRoomParams(city, zipCode, date, lat, lon, type, equipment, event);
		return ResponseEntity.ok(roomService.findAll(params));
	}

	@GetMapping("/search")
	public List<Room> search(@RequestParam String query) {
		return roomService.search(query);
	}

	@GetMapping("/users/{id}")
	public List<Room> allByUser(@PathVariable int id) {
		return roomService.findByUserId(id);
	}

	@GetMapping("/{id}")
	public Room findById(@PathVariable int id) {
		return roomService.findById(id);
	}

	@PutMapping
	public Room updateRoom(@RequestBody Room room, @AuthenticationPrincipal User user) {
		return roomService.update(room, user);
	}

	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id, @AuthenticationPrincipal User user) {
		roomService.delete(id, user);
	}

}
