package project.models.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Parent {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@JsonIgnoreProperties("parent")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
	private Set<Child> childs = new HashSet<>();
	
	
	@PrePersist
	public void prePersist() {
		System.out.println("prePersist called with:");
		for(Child c: childs) {
			c.setParent(this);
		}
	}
	@PreUpdate
	public void preUpdate() {
		System.out.println("preUpdate called");
		prePersist();
	}
}
