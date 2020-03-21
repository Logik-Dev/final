package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.Photo;
import project.models.entities.Room;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
	
	public List<Photo> findByRoom(Room room);
	
}
