package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
	List<Room> findByCity(String city);

}
