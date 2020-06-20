package project.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Photo {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Lob
	@JsonIgnore
	private byte[] file;
	
	@JsonIgnore
	private String baseUrl = "http://localhost:8080/api/photos/";
	
	@Formula("concat(base_url,'', id)")
	private String url;
	
	@JsonIgnoreProperties("photos")
	@ManyToOne
	private Room room;

	
	
}
