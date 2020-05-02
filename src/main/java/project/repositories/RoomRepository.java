package project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.models.entities.Equipment;
import project.models.entities.Photo;
import project.models.entities.Room;
import project.models.entities.RoomType;

public interface RoomRepository extends JpaRepository<Room, Long> {

	@Query("SELECT r FROM Room r WHERE r.address.city = :city AND r.address.zipCode = :zipCode")
	List<Room> findByCity(@Param("city") String city, @Param("zipCode") int zipCode);

	@Query("SELECT r FROM Room r WHERE r.owner.id = :id")
	List<Room> findByUser(@Param("id") Long id);

	@Query("SELECT p FROM Photo p WHERE p.id = :id")
	Optional<Photo> findPhotoById(@Param("id") Long id);

	@Query("SELECT r FROM Room r JOIN r.availableDays d WHERE r.address.city = :city AND d = :day AND r.address.zipCode = :zipCode")
	List<Room> findByCityAndDay(@Param("city") String city, @Param("zipCode") Integer zipCode,
			@Param("day") String day);

	@Query("SELECT e FROM Equipment e")
	List<Equipment> findAllEquipments();

	@Query("SELECT t FROM RoomType t")
	List<RoomType> findAllRoomTypes();

	@Query("SELECT r FROM Room r WHERE (r.address.latitude < :lat + 0.03 AND r.address.latitude > :lat - 0.03) "
			+ "AND (r.address.longitude < :lon + 0.03 AND r.address.longitude > :lon - 0.03)")
	List<Room> findByCoordinates(@Param("lat") double lat, @Param("lon") double lon);

}
