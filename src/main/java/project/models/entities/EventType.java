package project.models.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EventType {
	
	@Id
	private String id;
	
	@ManyToMany
	@JsonIgnoreProperties("eventTypes")
	private Set<Room> rooms = new HashSet<Room>();

	public EventType(String id) {
		this.id = id;
	}
	
	
}
