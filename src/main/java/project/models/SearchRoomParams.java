package project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRoomParams {
	
	private String city;
	
	private Integer zipCode;
	
	private String date;
	
	private Double lat;
	
	private Double lon;
	
	private String type;
	
	private String equipment;
	
	private String event;

}
