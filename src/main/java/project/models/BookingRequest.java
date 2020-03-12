package project.models;

import lombok.Getter;

@Getter
public class BookingRequest {
	
	private String startDate;
	
	private String endDate;
	
	private String startTime;
	
	private String endTime;
	
	private int roomId;
	
	private int clientId;
	
	private int weekRepetition;
	
}
