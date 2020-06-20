package project.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.exceptions.ForbiddenException;
import project.exceptions.InternalException;
import project.exceptions.PhotoNotFoundException;
import project.models.entities.Photo;
import project.models.entities.Room;
import project.models.entities.User;
import project.repositories.PhotoRepository;

@Service
public class PhotoService {

	@Autowired
	PhotoRepository photoRepository;

	@Autowired
	RoomService roomService;

	/**
	 * Enregister les photos d'une salle
	 * 
	 * @param files  le fichiers à enregistrer
	 * @param roomId l'identifiant de la salle concernée
	 * @param user   l'utilisateur authentifié
	 * @throws ForbiddenException si l'utilisateur n'est pas le propriétaire
	 * @throws InternalException  si une photo n'a pas pu être enregistrée
	 */
	public void create(MultipartFile[] files, int roomId, User user){
		Room room = roomService.findById(roomId);
		if (room.getOwner().getId() != user.getId())
			throw new ForbiddenException();
		for (MultipartFile file : files) {
			try {
				Photo photo = new Photo();
				photo.setRoom(room);
				photo.setFile(file.getBytes());
				photoRepository.save(photo);
			} catch (IOException e) {
				throw new InternalException("Impossible d'enregistrer les photos");
			}
		}
	}

	/**
	 * Chercher une photo par son id
	 * 
	 * @param id l'identifiant de la photo recherchée
	 * @return le fichier photo sous forme de byte[]
	 * @throws PhotoNotFoundException si la photo est introuvable
	 */
	public byte[] findById(int id){
		Photo photo = photoRepository.findById(id).orElseThrow(PhotoNotFoundException::new);
		return photo.getFile();
	}
}
