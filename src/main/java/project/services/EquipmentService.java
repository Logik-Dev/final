package project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.models.entities.Equipment;
import project.repositories.EquipmentRepository;

@Service
public class EquipmentService {
	
	@Autowired
	private EquipmentRepository equipmentRepository;
	
	public List<Equipment> findAll() {
		return equipmentRepository.findByCustom(false);
	}
}
