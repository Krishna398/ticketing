package com.walmart.ticketing;

import java.util.ArrayList;
import java.util.List;

import com.walmart.ticketing.TicketServiceImpl.SeatInfo;

public class SeatHold {
	
	private List<SeatInfo> seatsInfo;
	private String emailId;
	public SeatHold() {
		this.seatsInfo = new ArrayList<SeatInfo>();
	}
	
	public boolean addSeatInfo(SeatInfo info) {
		this.seatsInfo.add(info);
		return true;
	}
	
	public void setCustEmailId(String emailId) {
		this.emailId = emailId;
	}
}
