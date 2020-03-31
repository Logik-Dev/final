package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.exceptions.ForbiddenException;
import project.models.entities.Comment;
import project.models.entities.User;
import project.services.CommentService;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin("http://localhost:4200")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping
	public ResponseEntity<Comment> create(@RequestBody Comment comment, @RequestParam Long roomId, @AuthenticationPrincipal User user){
		if(user == null) throw new ForbiddenException("Pas authentifié");
		return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(comment, user.getId(), roomId));
	}

	
}
