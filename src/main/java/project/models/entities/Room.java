package project.models.entities;

import java.time.DayOfWeek;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double price;
	
	private double size;
	
	private String name;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	
	private int maxCapacity;
	
	@ElementCollection
	private Set<DayOfWeek> availableDays;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	private RoomType type;
	
	@ManyToMany
	private Set<Equipment> equipments;
	
	@Column(columnDefinition = "tinyint default 5")
	private int rating = 5;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Photo> photos;
	
	@ManyToOne
	private User owner;
	
	@OneToMany(mappedBy = "room")
	private Set<Booking> bookings;
	
	@OneToMany(mappedBy = "room")
	private Set<Comment> comments;

}
