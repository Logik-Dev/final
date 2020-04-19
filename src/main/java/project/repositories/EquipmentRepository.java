package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {

}
