package project.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime date = LocalDateTime.now();
	
	private String message;
	
	private int statut;

	public ErrorResponse(String message, int statut) {
		this.message = message;
		this.statut = statut;
	}

	
	
}
