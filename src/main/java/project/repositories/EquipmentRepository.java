package project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
	public Optional<Equipment> findByName(String name);
}
