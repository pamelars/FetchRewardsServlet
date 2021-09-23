package com.pamela.fetchrewards.persistence;

public class PayerBalance {
	private String payer;
	private int points;
	
	public PayerBalance() {
		
	}
	
	public PayerBalance (String payer, int points) {
		this.payer = payer;
		this.points = points;
	}

	public String getPayer() {
		return payer;
	}
	
	public void setPayer(String payer) {
		this.payer = payer;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "PayerBalance [payer=" + payer + ", points=" + points + "]";
	}
}
