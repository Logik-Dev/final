package project.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Address {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String label;
	
	private int zipCode;
	
	@JsonIgnoreProperties("address")
	@OneToOne(mappedBy = "address")
	private Room room;
	
	private double longitude;
	
	private double latitude;
	
	private String city;
}
