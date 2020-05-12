package project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.models.entities.EventType;
import project.repositories.EventTypeRepository;

@Service
public class EventTypeService {
	
	@Autowired
	private EventTypeRepository eventTypeRepository;
	
	public List<EventType> findAll() {
		return eventTypeRepository.findAll();
	}
}
