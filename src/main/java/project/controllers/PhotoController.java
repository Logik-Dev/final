package project.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import project.models.entities.User;
import project.services.PhotoService;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin("http://localhost:4200")
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/rooms/{roomId}")
	public Map<String, String> create(@RequestParam MultipartFile[] files, @PathVariable Long roomId,
			@AuthenticationPrincipal User user) {
		photoService.create(files, roomId, user);
		return Collections.singletonMap("result", "Photos enregistrées");
	}

	@GetMapping(value = "/{id}", produces = "image/jpg")
	public byte[] findById(@PathVariable Long id) {
		return photoService.findById(id);
	}

}
