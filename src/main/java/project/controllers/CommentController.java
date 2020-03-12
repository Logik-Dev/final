package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.models.Comment;
import project.services.CommentService;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {
	
	private final CommentService commentService;
	
	@PostMapping("/room/{roomId}/author/{authorId}")
	public ResponseEntity<Comment> create(@RequestBody Comment comment, @PathVariable int roomId, @PathVariable int authorId, @RequestParam String createdOn){
		return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(comment, roomId, authorId ,createdOn));
	}

}
