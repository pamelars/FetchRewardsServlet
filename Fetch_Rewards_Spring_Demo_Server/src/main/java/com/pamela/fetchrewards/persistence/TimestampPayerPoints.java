package com.pamela.fetchrewards.persistence;
import java.time.ZonedDateTime;

public class TimestampPayerPoints implements Comparable<TimestampPayerPoints> {
	
	private String payer;
	private int points;
	private ZonedDateTime timestamp;
	
	public TimestampPayerPoints() {
		
	}
	
	public void setPayer(String payer) {
		this.payer = payer;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public TimestampPayerPoints(String payer, int points, String timestampString) {
		this.payer = payer;
		this.points = points; 
		timestamp = ZonedDateTime.parse(timestampString);
	}
	
	public String getPayer() {
		return payer;
	}

	public int getPoints() {
		return points;
	}
	
	public void deletePoints(int amountToSubtract) {
		points -= amountToSubtract;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public int compareTo(TimestampPayerPoints o) {
		return this.timestamp.compareTo(o.timestamp);
	}

	@Override
	public String toString() {
		return "TimestampPayerPoints [payer=" + payer + ", points=" + points + ", timestamp=" + timestamp + "]";
	}
}
