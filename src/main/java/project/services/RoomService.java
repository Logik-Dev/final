package project.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.exceptions.ForbiddenException;
import project.exceptions.InternalException;
import project.exceptions.NotFoundException;
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
	

	public Room create(Room room) {
		return roomRepository.save(room);
	}
	
	public List<RoomType> getTypes() {
		return roomTypeRepository.findAll();
	}
	
	public List<Equipment> getEquipments() {
		return equipmentRepository.findAll();
	}
	
	public List<Room> findByUserId(Long id){
		List<Room> rooms = this.roomRepository.findByUser(id);
		if(rooms.isEmpty()) throw new NotFoundException();
		return rooms;
	}
	
	public List<Room> find(String city, Integer zipCode,  String day) {
		if(city != null) {
			if(day != null) {
				return findByCityAndDay(city, day);

			} else {
				return findByCity(city, zipCode);
			}
		} else 
			return findAll();
	}

	public List<Room> findByCity(String city, int zipCode) throws NotFoundException {
		List<Room> rooms = roomRepository.findByCity(city, zipCode);
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

	public Room findById(Long id) throws NotFoundException {
		return roomRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public byte[] getPhoto(Long id) {
		Photo photo = this.photoRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
		return photo.getFile();
	}

	public Room update(Room room, Long ownerId) throws ForbiddenException {
		return roomRepository.save(room);
	}
	
	public void delete(Long id) {
		roomRepository.deleteById(id);
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


}
