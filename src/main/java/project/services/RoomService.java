package project.services;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.exceptions.ForbiddenException;
import project.exceptions.RoomNotFoundException;
import project.models.entities.Equipment;
import project.models.entities.Room;
import project.models.entities.RoomType;
import project.models.entities.User;
import project.repositories.RoomRepository;
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

	/**
	 * Enregistrer une salle
	 * 
	 * @param room un objet de type Room à enregistrer
	 * @param user l'utilisateur authentifié
	 * @return un objet de type Room contenant un identifiant unique
	 * @throws ForbiddenException si l'id de l'utilisateur n'est pas celui du
	 *                            propriétaire de la salle
	 */
	public Room create(Room room, User user) throws ForbiddenException {
		if (user.getId() != room.getOwner().getId())
			throw new ForbiddenException();
		return roomRepository.save(room);
	}

	/**
	 * Obtenir toutes les salles par critère
	 * 
	 * @param city    la ville ciblée
	 * @param zipCode le code postal de la ville ciblée
	 * @param day     le jour souhaité
	 * @return la liste des salles correspondant aux critères
	 */
	public List<Room> findAll(String city, Integer zipCode, String day) {
		if (city != null) {
			if (day != null) {
				return findByCityAndDay(city, day);

			} else {
				return findByCity(city, zipCode);
			}
		} else
			return findAll();
	}

	/**
	 * Rechercher une salle par son identifiant
	 * 
	 * @param id l'identifiant de la salle à trouver
	 * @return la salle recherchée
	 * @throws RoomNotFoundException si la salle est introuvable
	 */
	public Room findById(Long id) throws RoomNotFoundException {
		return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException());
	}

	/**
	 * Obtenir la liste de tous les types de salle
	 * 
	 * @return la liste des types de salles
	 */
	public List<RoomType> allTypes() {
		return roomRepository.findAllRoomTypes();
	}

	/**
	 * Obtenir la liste de tous les équipements
	 * 
	 * @return la liste des équipements
	 */
	public List<Equipment> allEquipments() {
		return roomRepository.findAllEquipments();
	}

	/**
	 * Obtenir les salles d'un utilisateur
	 * 
	 * @param id l'identifiant de l'utiliseur
	 * @return la liste des salles de l'utilisateur
	 * @throws RoomNotFoundException si la liste est vide
	 */
	public List<Room> findByUserId(Long id) throws RoomNotFoundException {
		List<Room> rooms = this.roomRepository.findByUser(id);
		if (rooms.isEmpty())
			throw new RoomNotFoundException();
		return rooms;
	}

	/**
	 * Obtenir la liste des salles dans une ville
	 * 
	 * @param city    la ville ciblée
	 * @param zipCode le code postal de la ville ciblée
	 * @return la liste des salles correspondantes
	 * @throws RoomNotFoundException si la liste est vide
	 */
	private List<Room> findByCity(String city, int zipCode) throws RoomNotFoundException {
		List<Room> rooms = roomRepository.findByCity(city, zipCode);
		if (rooms.isEmpty())
			throw new RoomNotFoundException();
		return rooms;
	}

	/**
	 * Obtenir la liste des salle dans une ville et pour un jour donné
	 * 
	 * @param city la ville ciblée
	 * @param day  le jour souhaité
	 * @return la liste des salles correspondantes
	 * @throws RoomNotFoundException si la liste est vide
	 */
	private List<Room> findByCityAndDay(String city, String day) throws RoomNotFoundException {
		List<Room> rooms = roomRepository.findByCityAndDay(city,
				DateUtils.parseDate(day).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE));
		if (rooms.isEmpty()) {
			throw new RoomNotFoundException();
		}
		return rooms;
	}

	/**
	 * Obtenir la liste de toutes les salles
	 * 
	 * @return une liste contenant toutes le salles
	 * @throws RoomNotFoundException si la liste est vide
	 */
	private List<Room> findAll() throws RoomNotFoundException {
		List<Room> rooms = roomRepository.findAll();
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
	public Room update(Room room, User user) throws ForbiddenException {
		if (user.getId() != room.getOwner().getId())
			throw new ForbiddenException();
		return roomRepository.save(room);
	}

	/**
	 * Supprimer une salle
	 * 
	 * @param id l'identifiant de la salle à supprimer
	 * @throws RoomNotFoundException si la salle est introuvable
	 */
	public void delete(Long id) throws RoomNotFoundException {
		if (!roomRepository.existsById(id))
			throw new RoomNotFoundException();
		roomRepository.deleteById(id);
	}

}
