package project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.entities.Child;
import project.models.entities.Parent;

public interface ChildRepository extends JpaRepository<Child, Integer> {
	List<Child> findByParent(Parent parent);
}
