package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.Booking;
import project.models.Room;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
	List<Booking> findByRoom(Room room);
	
}
