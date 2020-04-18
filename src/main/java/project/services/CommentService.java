package project.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.models.entities.Comment;
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
	
	public Comment create(Comment comment) {
		return commentRepository.save(comment);
	}
	
}
