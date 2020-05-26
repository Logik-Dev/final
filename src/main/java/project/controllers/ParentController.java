package project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.models.entities.Child;
import project.models.entities.Parent;
import project.repositories.ChildRepository;
import project.repositories.ParentRepository;

@RestController
@RequestMapping("/api/parents")
public class ParentController {
	
	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private ChildRepository childRepository;
	
	@PostMapping
	public Parent create(@RequestBody Parent parent) {
		return parentRepository.save(parent);

	}
	
	@GetMapping
	public List<Parent> findAll(){
		return parentRepository.findAll();
	}
	
	@PutMapping
	public Parent update(@RequestBody Parent parent) {
		Parent savedParent = parentRepository.findById(parent.getId()).get();
		List<Child> childs = childRepository.findByParent(savedParent);
		for(Child c: parent.getChilds()) {
			childs.remove(c);
		}
		childRepository.deleteAll(childs);
		return parentRepository.save(parent);
	
	}
}
