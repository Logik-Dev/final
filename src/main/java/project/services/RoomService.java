package project.services;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.exceptions.ForbiddenException;
import project.exceptions.RoomNotFoundException;
import project.models.SearchRoomParams;
import project.models.entities.Room;
import project.models.entities.RoomEquipment;
import project.models.entities.User;
import project.repositories.EquipmentRepository;
import project.repositories.RoomRepository;
import project.utils.DateUtils;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private EquipmentRepository equipmentRepository;

	/**
	 * Enregistrer une salle
	 * 
	 * @param room un objet de type Room à enregistrer
	 * @param user l'utilisateur authentifié
	 * @return un objet de type Room contenant un identifiant unique
	 * @throws ForbiddenException si l'id de l'utilisateur n'est pas celui du
	 *                            propriétaire de la salle
	 */
	public Room create(Room room, User user){
		if(user == null) throw new ForbiddenException();
		for (RoomEquipment e : room.getEquipments()) {
			if (!equipmentRepository.existsById(e.getEquipment().getId())) {
				equipmentRepository.save(e.getEquipment());
			}
		}
		User u = new User();
		u.setId(user.getId());
		room.setOwner(u);
		return roomRepository.save(room);
	}

	/**
	 * Obtenir les salles par critère
	 * 
	 * @param params l'objet contenant les critères de recherche
	 * @return la liste des salles correspondant aux critères
	 * @throws RoomNotFoundException si aucune salle n'est trouvée
	 */
	public List<Room> findAll(SearchRoomParams params){
		List<Room> rooms;
		if (params.getLat() != null && params.getLon() != null) {
			rooms = roomRepository.findByCoordinates(params.getLat(), params.getLon());
		} else if (params.getType() != null) {
			rooms = roomRepository.findByTypeId(params.getType());
		} else if (params.getEquipment() != null) {
			rooms = roomRepository.findByEquipmentsEquipmentId(params.getEquipment());
		} else if (params.getEvent() != null) {
			rooms = roomRepository.findByEventTypesId(params.getEvent());
		} else if (params.getCity() != null && params.getZipCode() != null) {
			if (params.getDate() != null) {
				String day = DateUtils.parseDate(params.getDate()).getDayOfWeek().getDisplayName(TextStyle.FULL,
						Locale.FRANCE);
				rooms = roomRepository.findByAvailableDaysAndAddressCityAndAddressZipCode(day, params.getCity(),
						params.getZipCode());

			} else {
				rooms = roomRepository.findByAddressCityAndAddressZipCode(params.getCity(), params.getZipCode());
			}
		} else {
			rooms = roomRepository.findAll();
		}
		if (rooms.isEmpty())
			throw new RoomNotFoundException();
		return rooms;
	}
	
	public List<Room> search(String query){
		return roomRepository.findDistinctRoomsByNameContainingOrTypeIdOrEventTypesIdAllIgnoreCase(query, query, query);
	}
	
	public boolean exists(String name) {
		return roomRepository.existsByNameIgnoreCase(name);
	}
	/**
	 * Rechercher une salle par son identifiant
	 * 
	 * @param id l'identifiant de la salle à trouver
	 * @return la salle recherchée
	 * @throws RoomNotFoundException si la salle est introuvable
	 */
	public Room findById(int id){
		return roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
	}

	
	
	/**
	 * Obtenir les salles d'un utilisateur
	 * 
	 * @param id l'identifiant de l'utiliseur
	 * @return la liste des salles de l'utilisateur
	 * @throws RoomNotFoundException si la liste est vide
	 */
	public List<Room> findByUserId(int id){
		List<Room> rooms = this.roomRepository.findByOwnerId(id);
		if (rooms.isEmpty())
			throw new RoomNotFoundException();
		return rooms;
	}

	/**
	 * Modifier une salle
	 * 
	 * @param room la salle modifiée
	 * @param user l'utilisateur authentifié
	 * @return la salle modifée
	 * @throws ForbiddenException si l'utilisateur n'est pas le propriétaire
	 */
	public Room update(Room room, User user){
		if (user == null || user.getId() != room.getOwner().getId())
			throw new ForbiddenException();
		return roomRepository.save(room);
	}

	/**
	 * Supprimer une salle
	 * 
	 * @param id l'identifiant de la salle à supprimer
	 * @throws RoomNotFoundException si la salle est introuvable
	 * @throws ForbiddenException    si l'id de l'utilisateur n'est pas celui du
	 *                               propriétaire
	 */
	public void delete(int id, User user){
		Room room = roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
		if (room.getOwner().getId() != user.getId()) {
			throw new ForbiddenException();
		}
		roomRepository.deleteById(id);
	}

}
