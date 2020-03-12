package project.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project.models.Photo;
import project.services.PhotoService;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhotoController {
	
	private final PhotoService photoService;
	
	@GetMapping(value = "/{id}", produces = "image/jpg")
	public byte[] findById(@PathVariable int id) {
		return photoService.findById(id).getFile();
	}
	
	@GetMapping("/room/{id}")
	public List<Photo> findByRoom(@PathVariable int id) {
		return photoService.findByRoom(id);
	}
	
	@PostMapping("/room/{id}")
	public Photo create(@RequestParam MultipartFile file, @PathVariable int id) throws IOException  {
			return photoService.save(file, id);
	}
}
