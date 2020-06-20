package project.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.exceptions.CommentExistsException;
import project.exceptions.ForbiddenException;
import project.models.entities.Booking;
import project.models.entities.Comment;
import project.models.entities.TimeSlot;
import project.models.entities.User;
import project.repositories.BookingRepository;
import project.repositories.CommentRepository;

/**
 * Service de gestion des commentaires.
 * 
 * @author Cédric Maunier
 *
 */
@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private BookingRepository bookingRepository;

	/**
	 * Enregistrer un commentaire
	 * 
	 * @param comment l'objet de type Comment à enregistrer
	 * @param user    l'utilisateur authentifé
	 * @return un objet de type Comment contenant un identifiant unique
	 * @throws ForbiddenException si l'utilisateur n'est pas autorisé à commenter 
	 */
	public Comment create(Comment comment, User user){
		if (comment.getAuthor().getId() != user.getId() || !canComment(comment)) {
			throw new ForbiddenException();
		}
		return commentRepository.save(comment);
	}

	/**
	 * Vérifier si l'utilisateur peut commenter la salle
	 * 
	 * @param comment l'objet de type Comment contenant l'id de l'auteur et de la salle
	 * @return true si l'utilisateur est autorisé ou false sinon
	 * @throws CommentExistsException si l'auteur a déjà commenté la salle
	 */
	private boolean canComment(Comment comment){
		int clientId = comment.getAuthor().getId();
		int roomId = comment.getRoom().getId();
		if (commentRepository.hasComment(roomId, clientId))
			throw new CommentExistsException();
		List<Booking> bookings = bookingRepository.findByClientAndRoom(clientId, roomId);
		for (Booking booking : bookings) {
			for (TimeSlot slot : booking.getSlots()) {
				if (slot.getEnd().isBefore(LocalDateTime.now())) {
					return true;
				}
			}
		}
		return false;
	}

}
