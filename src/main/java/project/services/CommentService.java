package project.services;


import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.exceptions.ForbiddenException;
import project.models.BookingStatus;
import project.models.entities.Booking;
import project.models.entities.Comment;
import project.models.entities.Room;
import project.models.entities.User;
import project.repositories.CommentRepository;

/**
 * Service de gestion des commentaires.
 * @author Cédric Maunier
 *
 */
@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private UserService userService;
	

	public Comment save(Comment comment, Long authorId, Long roomId) {

		Room room = roomService.findById(roomId);
		User author = userService.findById(authorId);
		
		if(author == room.getOwner()) {
			throw new ForbiddenException("Impossible de commenter ses propres salles");
		}
		if(!canComment(author, room)) {
			throw new ForbiddenException("Commentaire possible uniquement pour les réservations terminées");
		}
		comment.setAuthor(author);
		comment.setPublishedOn(LocalDate.now());
		
		// recalculer la note de la salle
		int rating = comment.getRating();
		for(Comment c: room.getComments()) {
			rating += c.getRating();
		}
		
		room.setRating(rating);
		comment.setRoom(room);
		return commentRepository.save(comment);
	}
	
	public boolean canComment(User author, Room room) {
		return hasFinishedBooking(author, room);
	}
	private boolean hasFinishedBooking(User user, Room room) {
		for(Booking booking: room.getBookings()) {
			if(booking.getClient().getId() == user.getId() && booking.getStatus() == BookingStatus.FINISHED) {
				return true;
			}
		}
		return false;
	}
	
}
