package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.Parent;

public interface ParentRepository extends JpaRepository<Parent, Integer> {

}
