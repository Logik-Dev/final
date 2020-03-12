package project.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private double price;
	
	private double size;
	
	private String postCode;
	
	private String address;
	
	@ElementCollection
	private List<Double> coordinates;
	
	private String city;
	
	private int maxCapacity;
	
	@Column(columnDefinition = "integer default 0")
	private int rating = 0;
	
	@OneToMany(mappedBy = "room")
	private List<Photo> photos;
	
	@ManyToOne
	private User owner;
	
	@OneToMany(mappedBy = "room")
	private List<Booking> bookings;
	
	@OneToMany(mappedBy = "room")
	private List<Comment> comments;

}
