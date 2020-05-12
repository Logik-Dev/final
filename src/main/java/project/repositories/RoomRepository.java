package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.models.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
	
	List<Room> findByAddress_cityAndAddress_zipCode(String city, int zipCode);
	
	List<Room> findByOwner_id(int id);

	List<Room> findByAvailableDaysAndAddress_cityAndAddress_zipCode(String day, String city, int zipCode);
	
	List<Room> findByEquipments_equipment_id(String equipment);
	
	List<Room> findByEventTypes_id(String event);

	List<Room> findByType_id(@Param("type") String type);

	@Query("SELECT r FROM Room r WHERE (r.address.latitude < :lat + 0.03 AND r.address.latitude > :lat - 0.03) "
			+ "AND (r.address.longitude < :lon + 0.03 AND r.address.longitude > :lon - 0.03)")
	List<Room> findByCoordinates(@Param("lat") double lat, @Param("lon") double lon);

}
