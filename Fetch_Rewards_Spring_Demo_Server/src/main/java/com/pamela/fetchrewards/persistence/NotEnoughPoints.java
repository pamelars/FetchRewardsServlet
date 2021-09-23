package com.pamela.fetchrewards.persistence;

public class NotEnoughPoints extends Exception {
	
	public NotEnoughPoints(String why) {
		super(why);
	}
}
