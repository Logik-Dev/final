package project.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.exceptions.ForbiddenException;
import project.exceptions.InternalException;
import project.exceptions.NotFoundException;
import project.models.entities.Booking;
import project.models.entities.Equipment;
import project.models.entities.Photo;
import project.models.entities.Room;
import project.models.entities.RoomType;
import project.repositories.EquipmentRepository;
import project.repositories.PhotoRepository;
import project.repositories.RoomRepository;
import project.repositories.RoomTypeRepository;
import project.utils.DateUtils;

/**
 * Service de gestion des salles.
 * 
 * @author Cédric Maunier
 *
 */
@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private RoomTypeRepository roomTypeRepository;

	@Autowired
	private EquipmentRepository equipmentRepository;

	@Autowired
	private UserService userService;
	
	public List<RoomType> getTypes() {
		return roomTypeRepository.findAll();
	}
	
	public List<Equipment> getEquipments() {
		return equipmentRepository.findAll();
	}
	
	public List<Room> find(String city, String day) {
		if(city != null) {
			if(day != null) {
				System.out.println("here");
				return findByCityAndDay(city, day);

			} else {
				return findByCity(city);
			}
		} else 
			return findAll();
	}

	public List<Room> findByCity(String city) throws NotFoundException {
		List<Room> rooms = roomRepository.findByCity(city);
		if (rooms.isEmpty())
			throw new NotFoundException();
		return rooms;
	}

	public List<Room> findByCityAndDay(String city, String day) {
		List<Room> rooms = roomRepository.findByCityAndDay(city,
				DateUtils.parseDate(day).getDayOfWeek());
		if (rooms.isEmpty()) {
			throw new NotFoundException();
		}
		return rooms;
	}
	/*
	public List<Room> findByCityAndDate(String city, String day, String start, String end) throws NotFoundException {
		List<Room> rooms = this.findByCity(city);
		List<Room> freeRooms = new ArrayList<>();

		for (Room room : rooms) {
			if (isFree(room, DateUtils.parseDate(day), DateUtils.parseTime(start), DateUtils.parseTime(end))) {
				freeRooms.add(room);
			}
		}

		if (freeRooms.isEmpty())
			throw new NotFoundException();
		return freeRooms;
	}*/

	public List<Room> findAll() throws NotFoundException {
		List<Room> rooms = roomRepository.findAll();
		if (rooms.isEmpty())
			throw new NotFoundException();
		return rooms;
	}
	public Room addPhotos(Long roomId, MultipartFile files[], Long ownerId ) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new NotFoundException());
		if(room.getOwner().getId() != ownerId) throw new ForbiddenException();
		room = setPhotos(room, files);
		return roomRepository.save(room);
	}
	
	public Room save(Room room, Long ownerId) {
		room = setRoomType(room);
		room = setEquipments(room);
		room.setOwner(userService.findById(ownerId));

		return roomRepository.save(room);
	}

	public Room update(Room room, Long ownerId) throws ForbiddenException {
		if (room.getOwner().getId() != ownerId) {
			throw new ForbiddenException();
		}
		return roomRepository.save(room);
	}

	public Room findById(Long id) throws NotFoundException {
		return roomRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public byte[] getPhoto(Long id) {
		Photo photo = this.photoRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
		return photo.getFile();
	}

	public boolean isFree(Room room, LocalDate day, LocalTime start, LocalTime end) {
		// parcourir les reservations da la salle
		for (Booking booking : room.getBookings()) {

			// on vérifie que cette journée est disponible
			if (! room.getAvailableDays().contains(day.getDayOfWeek())) return false;

				// si la date est déjà réservée
				if (booking.getDates().contains(day)) {
					System.out.println(booking.getEnd());
					// on vérifie que les heures de début et de fin soient disponibles
					if (start.equals(booking.getBegin().toLocalTime()) || end.equals(booking.getEnd().toLocalTime())) {
						return false;
					} else if (start.isAfter(booking.getBegin().toLocalTime())
							&& end.isBefore(booking.getEnd().toLocalTime())) {
						return false;
					}
				}
		}
		return true;
	}

	private Room setPhotos(Room room, MultipartFile[] files) {
		Set<Photo> photos = new HashSet<>();
		for (MultipartFile file : files) {
			Photo photo = new Photo();
			try {
				photo.setFile(file.getBytes());
				photo.setRoom(room);
				photos.add(photo);	
			} catch (IOException e) {
				throw new InternalException();
			}
		}
		room.setPhotos(photos);
		return room;
	}

	private Room setRoomType(Room room) {
		RoomType type = roomTypeRepository.findByName(room.getType().getName())
				.orElseThrow(() -> new NotFoundException(room.getType().getName()));
		room.setType(type);
		return room;
	}

	private Room setEquipments(Room room) {
		if (room.getEquipments() != null) {
			Set<Equipment> equipments = new HashSet<>();
			for (Equipment equipment : room.getEquipments()) {
				Equipment e = equipmentRepository.findByName(equipment.getName())
						.orElseThrow(() -> new NotFoundException(equipment.getName()));
				equipments.add(e);
			}
			room.setEquipments(equipments);
		}
		return room;

	}

}
