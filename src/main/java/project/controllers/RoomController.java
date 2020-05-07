package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import project.models.entities.Equipment;
import project.models.entities.Room;
import project.models.entities.RoomType;
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
	public List<Room> all(@RequestParam(required = false) String city, @RequestParam(required = false) String date,
			@RequestParam(required = false) Integer zipCode, @RequestParam(required = false) Double lat,
			@RequestParam(required = false) Double lon) {
		return roomService.findAll(city, zipCode, date, lat, lon);
	}

	@GetMapping("/users/{id}")
	public List<Room> allByUser(@PathVariable int id) {
		return roomService.findByUserId(id);
	}

	@GetMapping("/{id}")
	public Room findById(@PathVariable int id) {
		return roomService.findById(id);
	}

	@GetMapping("/types")
	public List<RoomType> allTypes() {
		return roomService.allTypes();
	}

	@GetMapping("/types/{type}")
	public List<Room> findByType(@PathVariable String type) { return roomService.findByType(type); }

	@GetMapping("/equipments")
	public List<Equipment> allEquipments() {
		return roomService.allEquipments();
	}

	@GetMapping("/equipments/{equipment}")
	public List<Room> findByEquipment(@PathVariable String equipment) { return roomService.findByEquipment(equipment); }

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
