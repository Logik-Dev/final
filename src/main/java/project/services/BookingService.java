package project.services;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.exceptions.BookingNotFoundException;
import project.exceptions.DayUnavailableException;
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

	/**
	 * Enregistrer une réservation
	 * 
	 * @param booking l'objet de type Booking à enregistrer
	 * @return un objet de type Booking avec un identifiant unique
	 * @throws UnavailableException si la salle est indisponible
	 */
	public Booking create(Booking booking) throws UnavailableException {
		if (!isBookable(booking)) {
			throw new UnavailableException();
		}
		return bookingRepository.save(booking);
	}

	/**
	 * Rechercher une réservation par son id
	 * @param id l'id de la reservation à rechercher
	 * @return un objet de type Booking
	 * @throws BookingNotFoundException si la réservation est introuvable
	 */
	public Booking findById(Long id) throws BookingNotFoundException {
		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException());
		return booking;
	}
	
	/**
	 * Rechercher les réservations d'une salle
	 * @param roomId l'id de la salle 
	 * @return un objet de type List<Booking> contenant les réservations
	 * @throws BookingNotFoundException si il n'y a aucune réservation
	 */
	public List<Booking> findByRoom(Long roomId) throws BookingNotFoundException {
		List<Booking> bookings = bookingRepository.findByRoomId(roomId);
		if (bookings.isEmpty())
			throw new BookingNotFoundException();
		return bookings;
	}

	/**
	 * Vérifier si la réservation est possible
	 * @param booking l'objet de type Booking à tester
	 * @return true si la réservation est possible ou false sinon
	 * @throws DayUnavailableException si le jour n'est pas réservable
	 */
	private boolean isBookable(Booking booking) throws DayUnavailableException {
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
