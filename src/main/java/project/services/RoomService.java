package project.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.exceptions.RoomNotFoundException;
import project.models.Booking;
import project.models.Room;
import project.models.User;
import project.repositories.RoomRepository;
import project.utils.DateUtils;
/**
 * Service de gestion des salles.
 * @author Cédric Maunier
 *
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomService {
	
	private final RoomRepository roomRepository;
	
	private final UserService userService;
	
	/**
	 * Enregistrer une salle.
	 * @param room la salle concernée.
	 * @return l'objet Room crée.
	 */
	public Room save(Room room, int ownerId) {
		User owner = userService.findById(ownerId);
		room.setOwner(owner);
		return roomRepository.save(room);
	}
	
	public Room update(Room room) {
		return roomRepository.save(room);
	}
	
	/**
	 * Rechercher une salle par id.
	 * @param id un int identifiant la salle recherchée.
	 * @return l'objet Room recherché.
	 * @throws RoomNotFoundException si l'id ne correspond à aucune salle.
	 */
	public Room findById(int id) throws RoomNotFoundException {
		return roomRepository.findById(id)
				.orElseThrow(() -> new RoomNotFoundException(id));
	}
	
	/**
	 * Trouver les salles qui sont dans la ville passée en paramètre.
	 * @param city un String qui est le nom de la ville souhaitée.
	 * @return une liste contenant les salles correspondantes.
	 * @throws RoomNotFoundException si aucune salle n'est trouvée.
	 */
	public List<Room> findByCity(String city) throws RoomNotFoundException{
		List<Room> rooms = roomRepository.findByCity(city);
		if(rooms.isEmpty())
			throw new RoomNotFoundException();
		return rooms;

	}
	
	/**
	 * Trouver les salles disponible pendant une période et dans une ville données.
	 * @param city un String qui est le nom de la ville souhaitée.
	 * @param start un String représentant le début de la reservation.
	 * @param end un String représentant la fin de la réservation.
	 * @return une lsite contenant les salles correspondantes.
	 * @throws RoomNotFoundException si aucune salle n'est trouvée.
	 */
	public List<Room> findByCityAndDate(String city, String date, String start, String end) throws RoomNotFoundException{
		List<Room> rooms = this.findByCity(city);
		List<Room> freeRooms = new ArrayList<>();
		for(Room room: rooms) {
			if(isFree(room, DateUtils.parseDate(date), DateUtils.parseTime(start), DateUtils.parseTime(end))) {
				freeRooms.add(room);
			}
		}
		if(freeRooms.isEmpty()) throw new RoomNotFoundException();
		return freeRooms;
	}
	
	/**
	 * Obtenir la liste de toutes les salles.
	 * @return une liste contenant les salles trouvées.
	 * @throws RoomNotFoundException si aucune salle n'est trouvée.
	 */
	public List<Room> findAll() throws RoomNotFoundException {
		List<Room> rooms = roomRepository.findAll();
		if(rooms.isEmpty()) throw new RoomNotFoundException();
		return rooms;
	}
	public boolean isFree(Room room, LocalDate date, LocalTime start, LocalTime end) {
		System.out.println("date recherchée : " + date);
		for(Booking booking: room.getBookings()) {
			System.out.print("date réservées: ");
			booking.getDates().forEach(System.out::print);
			System.out.print(",");
			if(booking.getDates().contains(date)) {

				if(start.equals(booking.getStart()) || end.equals(booking.getEnd())){
					return false;
				}
				else if(start.isAfter(booking.getStart()) && end.isBefore(booking.getEnd())) {
					return false;
				}
			}
		}
		return true;
	}
	
	
}
