package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.models.entities.EventType;
import project.services.EventTypeService;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:4200")
public class EventTypeController {
	
	@Autowired
	private EventTypeService eventTypeService;
	
	@GetMapping
	public List<EventType> findAll() {
		return eventTypeService.findAll();
	}
}
