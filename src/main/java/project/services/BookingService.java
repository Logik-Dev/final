package project.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.exceptions.ConflictException;
import project.exceptions.ForbiddenException;
import project.exceptions.NotFoundException;
import project.models.BookingStatus;
import project.models.entities.Booking;
import project.repositories.BookingRepository;
import project.utils.DateUtils;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RoomService roomService;

	@Autowired
	private UserService userService;

	public Booking save(Long roomId, Booking booking, Long clientId) throws ConflictException {
		// définir le client, la salle et calculer le prix
		booking.setClient(userService.findById(clientId));
		booking.setRoom(roomService.findById(roomId));
		booking.calculatePrice();

		// Vérifier la disponibilité
		if (!isAvalaible(booking)) {
			throw new ConflictException("Cette date est déjà réservée");
		}
		
		// enregistrer la réservation pour obtenir l'id
		Booking savedBooking = bookingRepository.save(booking);
		
		// notifier le propriétaire avec l'id de la reservation
		savedBooking.getRoom().getOwner().getBookingNotifications().add(savedBooking.getId());
		
		return bookingRepository.save(savedBooking);
	}
	public boolean checkAvailable(String begin, String end, int weekRepetition, Long roomId) {
		Booking booking = new Booking();
		booking.setBegin(DateUtils.parseDateTime(begin));
		booking.setEnd(DateUtils.parseDateTime(end));
		booking.setWeekRepetition(weekRepetition);
		booking.setRoom(roomService.findById(roomId));
		return isAvalaible(booking);
	}
	public List<Booking> findByRoom(Long roomId) throws NotFoundException {
		List<Booking> bookings = bookingRepository.findByRoomId(roomId);
		if (bookings.isEmpty())
			throw new NotFoundException();
		return bookings;

	}

	public Booking changeBookingStatus(Long id, String status, Long userId) throws NotFoundException, ForbiddenException {
		// changer le statut de la réservation
		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
		booking.setStatus(BookingStatus.valueOf(status));

		// la demande vient du client alors notifier le propriétaire
		if (booking.getClient().getId() == userId) {
			booking.getRoom().getOwner().getBookingNotifications().add(booking.getId());
			booking.getClient().getBookingNotifications().remove(booking.getId());
		}
		// sinon notifier le client
		else if (booking.getRoom().getOwner().getId() == userId) {
			booking.getClient().getBookingNotifications().add(booking.getId());
			booking.getRoom().getOwner().getBookingNotifications().remove(booking.getId());
		}
		// sinon l'utilisateur n'est pas autorisé à changé le statut
		else {
			throw new ForbiddenException();
		}
		return bookingRepository.save(booking);

	}

	private boolean isAvalaible(Booking booking) {
		// parcourir les jours de réservation 
		for (LocalDate date : booking.getDates()) {
			// vérifier si la salle est libre ce jour aux heures souhaitées
			if (!roomService.isFree(booking.getRoom(), date, booking.getBegin().toLocalTime(),
					booking.getEnd().toLocalTime())) {
				return false;
			}
		}
		return true;
	}

}
