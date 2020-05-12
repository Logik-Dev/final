package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.models.entities.Equipment;
import project.services.EquipmentService;

@RestController
@RequestMapping("/api/equipments")
@CrossOrigin("http://localhost:4200")
public class EquipmentController {
	
	@Autowired
	private EquipmentService equipmentService;
	
	@GetMapping
	public List<Equipment> findAll() {
		return equipmentService.findAll();
	}
}
