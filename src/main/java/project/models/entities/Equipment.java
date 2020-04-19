package project.models.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
	
	@Id 
	private String id;

	@Override
	public String toString() {
		return "Equipment [id=" + id + "]";
	}
	
	
	
}
