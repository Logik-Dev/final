package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.models.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	@Query("SELECT b FROM Booking b WHERE b.room.id = :id")
	List<Booking> findByRoomId(@Param("id") Long id);
	
	@Query("SELECT b FROM Booking b WHERE b.client.id = :clientId AND b.room.id = :roomId")
	List<Booking> findByClientAndRoom(@Param("clientId") Long clientId, @Param("roomId") Long roomId);
}
