package project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
	public Optional<RoomType> findByName(String name);
}
