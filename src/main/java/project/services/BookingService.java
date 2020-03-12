package project.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.exceptions.BookingNotFoundException;
import project.exceptions.UnavailableException;
import project.models.Booking;
import project.models.BookingRequest;
import project.models.BookingStatus;
import project.models.Room;
import project.models.User;
import project.repositories.BookingRepository;
import project.utils.DateUtils;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingService {

	private final BookingRepository bookingRepository;

	private final RoomService roomService;

	private final UserService userService;

	public Booking save(BookingRequest bookingRequest) {
		Booking booking = createAndFillBooking(bookingRequest);
		return bookingRepository.save(booking);

	}

	public List<Booking> findByRoom(int roomId) throws BookingNotFoundException {
		Room room = roomService.findById(roomId);
		List<Booking> bookings = bookingRepository.findByRoom(room);
		if (bookings.isEmpty())
			throw new BookingNotFoundException();
		return bookings;

	}
	public Booking changeBookingStatus(int id, String status, int userId)  throws BookingNotFoundException {
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new BookingNotFoundException(id));
		
		booking.setStatus(BookingStatus.valueOf(status));
		
		// la demande vient du client alors notifier le propriétaire
		if(booking.getClient().getId() == userId) {
			booking.getRoom().getOwner().getBookingNotifications().add(booking.getId());
			booking.getClient().getBookingNotifications().remove(booking.getId());
		}
		// sinon notifier le client
		else {
			booking.getClient().getBookingNotifications().add(booking.getId());
			booking.getRoom().getOwner().getBookingNotifications().remove(booking.getId());
		}
		
		return bookingRepository.save(booking);
		
	}


	private Booking createAndFillBooking(BookingRequest bookingRequest) throws UnavailableException{
		Booking booking = new Booking();
		Room room = roomService.findById(bookingRequest.getRoomId());
		User client = userService.findById(bookingRequest.getClientId());
		
		// notification propriétaire
		room.getOwner().getBookingNotifications().add(room.getId());

		// remplir l'objet booking
		booking.setClient(client);
		booking.setRoom(room);
		booking.setStart(DateUtils.parseTime(bookingRequest.getStartTime()));
		booking.setEnd(DateUtils.parseTime(bookingRequest.getEndTime()));
		booking.setDuration((int) Duration.between(booking.getStart(), booking.getEnd()).toHours());
		booking.setDates(generateDates(bookingRequest.getStartDate(), bookingRequest.getEndDate(),
				bookingRequest.getWeekRepetition()));
		booking.setPrice(generatePrice(booking));

		// Vérifier la disponibilité
		if (!isAvalaible(booking)) {
			throw new UnavailableException();
		}

		return booking;
	}

	private Set<LocalDate> generateDates(String startDate, String endDate, int weekRepetition) {
		// si la réservation ne concerne qu'une journée
		if (weekRepetition == 0 || startDate.equals(endDate)) {
			return Set.of(DateUtils.parseDate(startDate));
		} 
		// sinon définir les dates
		else {
			return DateUtils.parseDate(startDate)
					.datesUntil(DateUtils.parseDate(endDate), Period.ofWeeks(weekRepetition))
					.collect(Collectors.toSet());
		}
	}

	private boolean isAvalaible(Booking booking) {
		for (LocalDate date : booking.getDates()) {
			if (!roomService.isFree(booking.getRoom(), date, booking.getStart(), booking.getEnd())) {
				return false;
			}
		}
		return true;
	}

	private double generatePrice(Booking booking) {
		return booking.getDates().size() * booking.getDuration() * booking.getRoom().getPrice();
	}

}
