package project.models.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime publishedOn = LocalDateTime.now();
	
	private int rating;
	
	@JsonIgnoreProperties({"comments", "bookings", "rooms", "email", "address", "roles"})
	@ManyToOne
	private User author;
	
	@JsonIgnoreProperties("comments")
	@ManyToOne
	private Room room;


}
