package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, String> {

}
