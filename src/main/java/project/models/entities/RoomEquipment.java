package project.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class RoomEquipment {
		
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne
	private Equipment equipment;
	
	private int quantity;

	public RoomEquipment(Equipment equipment, int quantity) {
		this.equipment = equipment;
		this.quantity = quantity;
	}
}
