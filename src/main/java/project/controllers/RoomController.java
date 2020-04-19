package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
	@ResponseBody
	public Room create(@RequestBody Room room, @AuthenticationPrincipal User user) {
		return roomService.create(room, user);
	}
	
	@GetMapping
	public List<Room> all(@RequestParam(required = false) String city,
			@RequestParam(required = false) String day, @RequestParam(required = false) Integer zipCode) {
		return roomService.findAll(city, zipCode, day);
	}
	
	@GetMapping("/users/{id}")
	public List<Room> allByUser(@PathVariable Long id){
		return roomService.findByUserId(id);
	}
	
	@ResponseBody
	@GetMapping("/{id}")
	public Room findById(@PathVariable Long id) {
		return roomService.findById(id);
	}
	
	
	@GetMapping("/types")
	public List<RoomType> allTypes() {
		return roomService.allTypes();
	}

	@GetMapping("/equipments")
	public List<Equipment> allEquipments() {
		return roomService.allEquipments();
	}
	
	@ResponseBody
	@PutMapping
	public Room updateRoom(@RequestBody Room room, @AuthenticationPrincipal User user) {
		return roomService.update(room, user);
	}
	
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		roomService.delete(id);
	}
	
}
