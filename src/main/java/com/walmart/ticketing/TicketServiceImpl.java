package com.walmart.ticketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class TicketServiceImpl implements TicketService {
	
	enum Status {
		AVAILABLE,
		HOLD,
		RESERVED
	}
	
	static class SeatInfo {
		int row;
		int col;
		Status status;
		SeatInfo(int row, int col, Status status) {
			this.row = row;
			this.col = col;
			this.status = status;
		}
	}
	
	private Map<Integer,List<SeatInfo>> seats;
	private static int availableSeatCount;
	private int rows;
	private int cols;

	public TicketServiceImpl(int row, int column) {
	 	initializeSeatMap(row, column);
	 	availableSeatCount = row*column;
	 	this.rows = row;
	 	this.cols = column;
	}
	
	private void initializeSeatMap(int row, int column) {	
		seats = new HashMap<Integer,List<SeatInfo>>();
		List<SeatInfo> rowSeats;	
		for( int i = 0; i< row; i++) {
			rowSeats = new ArrayList<SeatInfo>();
			for( int j = 0; j< column; j++ ) {
				rowSeats.add(new SeatInfo(i,j,Status.AVAILABLE));
			}
			seats.put(i,rowSeats);
		}
	}
	
	public int numSeatsAvailable() {
		// TODO Auto-generated method stub
		return availableSeatCount;
	}

	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		// TODO Auto-generated method stub
		SeatHold seatHoldInfo = new SeatHold();
		findAndHoldSeats(numSeats).stream()
								  .forEach( seatInfo -> seatHoldInfo.addSeatInfo(seatInfo));
		seatHoldInfo.setCustEmailId(customerEmail);
		return seatHoldInfo;
	}
	
	/**
	 * Find the first best fit and return the SeatHold info. 
	 * @param numSeats
	 * @return
	 */
	private List<SeatInfo> findAndHoldSeats(int numSeats) {
		if( numSeats > this.cols + 1) {
			throw new RuntimeException("Invalid Request, number of requested seats should be less than the row limit");
		}
		List<SeatInfo> seatInfoList = null ;
		synchronized(this) {
		for( int i =0;i< seats.size(); i++) {
	       if(null != (seatInfoList = AvailableAndBlock(seats.get(i), numSeats, i)) ) {
	    	  break;
	       }   	
		}
	} return seatInfoList;
	} 
	
	private List<SeatInfo> AvailableAndBlock(List<SeatInfo> info, int numSeats, int row) {
		int count = numSeats;
		for( int k =0;k<=(info.size()-numSeats);k++) {
			 int temp = k;
			 count = numSeats;
			 while( temp < numSeats+k ) {
				 if( ! info.get(temp).status.equals(Status.AVAILABLE)) {
					 break;
				 }
				 count --;
			 }
			 if( count  == 0 ) {
				 availableSeatCount=-numSeats;
				return blockSeats(info,row,k,numSeats); 
			 }	 
		} return null;
	}

	
	private List<SeatInfo> blockSeats(List<SeatInfo> info, int row, int start, int end) { 
		List<SeatInfo> seatInfo = new ArrayList<SeatInfo>();
		for(int l = start;l<start+end;l++) {
			seatInfo.add(new SeatInfo(row, l, Status.HOLD));
		}
		scheduleTimer(seatInfo);
		return seatInfo;
	}
	

	private void scheduleTimer(List<SeatInfo> seatInfo) {
		// TODO Auto-generated method stub
		
	}

	public String reserveSeats(int seatHoldId, String customerEmail) {
		// TODO Auto-generated method stub
		return null;
	}

}
