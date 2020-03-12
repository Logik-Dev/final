package project.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.models.Comment;
import project.models.Room;
import project.models.User;
import project.repositories.CommentRepository;
import project.utils.DateUtils;

/**
 * Service de gestion des commentaires.
 * @author Cédric Maunier
 *
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	private final RoomService roomService;
	
	private final UserService userService;
	
	public Comment save(Comment comment, int roomId, int authorId, String createdOn) {
		User author = userService.findById(authorId);
		Room room = roomService.findById(roomId);
		comment.setCreatedOn(DateUtils.parseDate(createdOn));
		comment.setAuthor(author);
		int grade = comment.getGrade();
		for(Comment c: room.getComments()) {
			grade += c.getGrade();
		}
		room.setGrade(grade / (room.getComments().size() + 1));
		comment.setRoom(room);
		
		return commentRepository.save(comment);
	}
	
}
