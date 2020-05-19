package project.models.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PreRemove;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.models.Volume;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private double price;
	
	private double size;
	
	private String name;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	
	private int maxCapacity;
	
	@ElementCollection
	private Set<String> availableDays;
	
	@Enumerated(EnumType.STRING)
	private Volume maxVolume;
	
	@ManyToMany
	@JsonIgnoreProperties("rooms")
	private Set<EventType> eventTypes = new HashSet<EventType>();
		
	@OneToOne
	private RoomType type;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<RoomEquipment> equipments = new HashSet<RoomEquipment>();
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<RoomEquipment> customEquipments = new HashSet<RoomEquipment>();
	
	@Column(columnDefinition = "tinyint default 5")
	private int rating = 5;
	
	@JsonIgnoreProperties("room")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Photo> photos = new HashSet<Photo>();
	
	@JsonIgnoreProperties({"rooms", "bookings"})
	@ManyToOne
	private User owner;
	
	@JsonIgnoreProperties({"room", "client"})
	@OneToMany(mappedBy = "room")
	private Set<Booking> bookings = new HashSet<Booking>();
	
	@JsonIgnoreProperties("room")
	@OneToMany(mappedBy = "room")
	private Set<Comment> comments = new HashSet<Comment>();
	
	@PostLoad
	private void calculateRating() {
		rating = 5;
		if(comments.size() > 0) {
			for(Comment c: comments) {
				this.rating += c.getRating();
			}
			rating /= comments.size() + 1;
		}
	}
	
	@PreRemove
	public void preRemove() {
		for(Booking booking: bookings) {
			booking.setRoom(null);
		}
	}

	

}
