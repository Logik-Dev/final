package project.services;


import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.models.entities.Comment;
import project.models.entities.Room;
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
		comment.setAuthor(userService.findById(authorId));
		Room room = roomService.findById(roomId);
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
	
}
