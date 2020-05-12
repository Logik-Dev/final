package project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.models.entities.RoomType;
import project.repositories.RoomTypeRepository;

@Service
public class RoomTypeService {
	
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	
	public List<RoomType> findAll() {
		return roomTypeRepository.findAll();
	}
}
