package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
