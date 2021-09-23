package com.pamela.fetchrewards.service;

import java.util.List;

import com.pamela.fetchrewards.persistence.NotEnoughPoints;
import com.pamela.fetchrewards.persistence.PayerBalance;
import com.pamela.fetchrewards.persistence.TimestampPayerPoints;

public interface PointService {
	
	public void addPoints(TimestampPayerPoints tpp) throws NotEnoughPoints;
	
	public List<PayerBalance> getBalance();
	
	public List<PayerBalance> spendPoints(int pointsToSpend) throws NotEnoughPoints;
}
