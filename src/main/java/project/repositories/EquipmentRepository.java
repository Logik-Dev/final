package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {
	
	List<Equipment> findByCustom(boolean custom);
}
