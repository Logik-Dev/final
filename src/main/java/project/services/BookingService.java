package project.services;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.exceptions.DayUnavailableException;
import project.exceptions.NotFoundException;
import project.exceptions.RoomNotFoundException;
import project.exceptions.UnavailableException;
import project.models.entities.Booking;
import project.models.entities.Room;
import project.models.entities.TimeSlot;
import project.repositories.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RoomService roomService;

	public Booking create(Booking booking) {
		if (!isBookable(booking)) {
			throw new UnavailableException();
		}
		return bookingRepository.save(booking);
	}

	public List<Booking> all(Long roomId) throws NotFoundException {
		List<Booking> bookings = bookingRepository.findByRoomId(roomId);
		if (bookings.isEmpty())
			throw new RoomNotFoundException();
		return bookings;
	}

	public Booking get(Long id) {
		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());
		return booking;
	}

	private boolean isBookable(Booking booking) {
		Room room = roomService.findById(booking.getRoom().getId());
		for (TimeSlot slot : booking.getSlots()) {
			String day = slot.getStart().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			if (room.getAvailableDays().contains(day)) {
				throw new DayUnavailableException(day);
			}
			for (Booking b : room.getBookings()) {
				for (TimeSlot roomSlot : b.getSlots()) {
					if (slot.getStart().equals(roomSlot.getStart()) || slot.getEnd().equals(roomSlot.getEnd())) {
						return false;
					} else if (slot.getStart().isAfter(roomSlot.getStart())
							&& slot.getEnd().isBefore(roomSlot.getEnd())) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
