package project.models.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
public class TimeSlot {
	
	@Id @GeneratedValue
	private Long id;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime start;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime end;
	
	
}
