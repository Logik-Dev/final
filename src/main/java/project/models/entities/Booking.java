package project.models.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<TimeSlot> slots;

	private double price;

	@ManyToOne
	@JsonIgnoreProperties("bookings")
	private User client;

	@ManyToOne
	@JsonIgnoreProperties(value = { "bookings", "owner", "comments" }, allowSetters = true)
	private Room room;


}
