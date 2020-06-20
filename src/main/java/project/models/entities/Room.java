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
	
	@Column(unique = true) 
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
	private Set<EventType> eventTypes = new HashSet<>();
		
	@OneToOne
	private RoomType type;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<RoomEquipment> equipments = new HashSet<>();

	@Column(columnDefinition = "tinyint default 5")
	private int rating = 5;
	
	@JsonIgnoreProperties("room")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Photo> photos = new HashSet<>();
	
	@JsonIgnoreProperties({"rooms", "bookings"})
	@ManyToOne
	private User owner;
	
	@JsonIgnoreProperties({"room", "client"})
	@OneToMany(mappedBy = "room")
	private Set<Booking> bookings = new HashSet<>();
	
	@JsonIgnoreProperties("room")
	@OneToMany(mappedBy = "room")
	private Set<Comment> comments = new HashSet<>();
	
	@PostLoad
	private void calculateRating() {
		rating = 5;
		if(!comments.isEmpty()) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return id == other.id;
	}



	

}
