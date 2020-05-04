package project.models.entities;

import java.util.HashSet;
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
import javax.persistence.PostLoad;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	
	private String name;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	
	private int maxCapacity;
	
	@ElementCollection
	private Set<String> availableDays;
		
	@OneToOne
	private RoomType type;
	
	@ManyToMany
	private Set<Equipment> equipments = new HashSet<Equipment>();
	
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
	@OnDelete(action = OnDeleteAction.NO_ACTION)
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

	

}
