package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.Photo;
import project.models.Room;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
	public List<Photo> findByRoom(Room room);
}
