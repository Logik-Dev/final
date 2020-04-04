package project.controllers;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import project.exceptions.ForbiddenException;
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

	
	@GetMapping("/{id}")
	public ResponseEntity<Room> findOne(@PathVariable Long id) {
		return ResponseEntity.ok(roomService.findById(id));
	}

	@GetMapping
	public ResponseEntity<List<Room>> find(@RequestParam(required = false) String city,
			@RequestParam(required = false) String day, @RequestParam(required = false) Integer zipCode) {
		return ResponseEntity.ok(roomService.find(city, zipCode, day));
	}

	@GetMapping(value = "/photos/{id}", produces = "image/jpg")
	public byte[] getPhoto(@PathVariable Long id) {
		return roomService.getPhoto(id);
	}

	@GetMapping("/types")
	public ResponseEntity<List<RoomType>> getRoomTypes() {
		return ResponseEntity.ok(roomService.getTypes());
	}

	@GetMapping("/equipments")
	public ResponseEntity<List<Equipment>> getEquipments() {
		return ResponseEntity.ok(roomService.getEquipments());
	}

	@PostMapping
	public ResponseEntity<Room> create(@RequestBody Room room, @AuthenticationPrincipal User user) {
		if (user == null)
			throw new ForbiddenException();
		return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(room, user.getId()));
	}

	@PostMapping("/{id}/photos")
	public ResponseEntity<Room> addPhotos(@PathVariable Long id, @AuthenticationPrincipal User user,
			@RequestParam MultipartFile files[]) throws IOException {
		if (user == null)
			throw new ForbiddenException();
		return ResponseEntity.ok(roomService.addPhotos(id, files, user.getId()));
	}

	@PutMapping
	public ResponseEntity<Room> updateRoom(@RequestBody Room room, @AuthenticationPrincipal User user) {
		if (user == null)
			throw new ForbiddenException();
		return ResponseEntity.ok(roomService.update(room, user.getId()));
	}

}
