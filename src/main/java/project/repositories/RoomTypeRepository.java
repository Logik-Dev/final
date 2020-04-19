package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, String> {

}
