package project.models.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Equipment {
	
	@Id 
	private String id;
	
	private boolean custom = false;

	public Equipment(String id) {
		this.id = id;
	}
	
	
}
