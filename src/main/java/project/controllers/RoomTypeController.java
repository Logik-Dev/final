package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.models.entities.RoomType;
import project.services.RoomTypeService;

@RestController
@RequestMapping("/api/types")
@CrossOrigin("http://localhost:4200")
public class RoomTypeController {
	
	@Autowired
	private RoomTypeService roomTypeService;
	
	@GetMapping
	public List<RoomType> findAll() {
		return this.roomTypeService.findAll();
	}
}
