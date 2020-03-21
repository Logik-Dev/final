package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
