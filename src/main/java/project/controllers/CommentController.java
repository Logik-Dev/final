package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import project.models.entities.Comment;
import project.models.entities.User;
import project.services.CommentService;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin("http://localhost:4200")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@PostMapping
	public Comment create(@RequestBody Comment comment, @AuthenticationPrincipal User user){
		return commentService.create(comment);
	}

	
}
