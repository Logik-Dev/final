package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.models.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
	
	List<Room> findByAddressCityAndAddressZipCode(String city, int zipCode);
	
	List<Room> findByOwnerId(int id);

	List<Room> findByAvailableDaysAndAddressCityAndAddressZipCode(String day, String city, int zipCode);
	
	List<Room> findByEquipmentsEquipmentId(String equipment);
	
	List<Room> findByEventTypesId(String event);

	List<Room> findByTypeId(String type);
	
	List<Room> findDistinctRoomsByNameContainingOrTypeIdOrEventTypesIdAllIgnoreCase(String name, String type, String eventType);

	Boolean existsByNameIgnoreCase(String name);
	
	@Query("SELECT r FROM Room r WHERE (r.address.latitude < :lat + 0.03 AND r.address.latitude > :lat - 0.03) "
			+ "AND (r.address.longitude < :lon + 0.03 AND r.address.longitude > :lon - 0.03)")
	List<Room> findByCoordinates(@Param("lat") double lat, @Param("lon") double lon);
	
}
