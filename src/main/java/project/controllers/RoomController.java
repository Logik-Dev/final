package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import project.exceptions.ForbiddenException;
import project.models.entities.Room;
import project.models.entities.User;
import project.services.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin("http://localhost:4200")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@GetMapping
	public ResponseEntity<List<Room>> find(@RequestParam(required = false) String city,
			@RequestParam(required = false) String day, @RequestParam(required = false) String start,
			@RequestParam(required = false) String end) {
		return ResponseEntity.ok(roomService.find(city, day, start, end));
	}
	
	@GetMapping(value = "/photos/{id}", produces = "image/jpg")
	public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {		
		return ResponseEntity.ok(roomService.getPhoto(id));
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Room> create(@RequestPart Room room, @AuthenticationPrincipal User user, @RequestPart MultipartFile files[]) {
		if(user == null) throw new ForbiddenException();
		return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(room, user.getId(), files));
	}

	@PutMapping
	public ResponseEntity<Room> updateRoom(@RequestBody Room room, @AuthenticationPrincipal User user) {
		if(user == null) throw new ForbiddenException();
		return ResponseEntity.ok(roomService.update(room, user.getId()));
	}

}
