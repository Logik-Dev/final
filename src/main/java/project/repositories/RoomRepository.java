package project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.models.entities.Photo;
import project.models.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

	@Query("SELECT r FROM Room r WHERE r.address.city = :city")
	List<Room> findByCity(@Param("city") String city);

	@Query("SELECT p FROM Photo p WHERE p.id = :id")
	Optional<Photo> findPhotoById(@Param("id") Long id);

	@Query("SELECT r FROM Room r JOIN r.availableDays d WHERE r.address.city = :city AND :day IN d")
	List<Room> findByCityAndAvailableDays(@Param("city") String city, @Param("day") int day);

}
