package com.pamela.fetchrewards.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pamela.fetchrewards.DAO.PointDAO;
import com.pamela.fetchrewards.persistence.NotEnoughPoints;
import com.pamela.fetchrewards.persistence.PayerBalance;
import com.pamela.fetchrewards.persistence.TimestampPayerPoints;

@Service
public class PointServiceImpl implements PointService {
	
	PointDAO pointDAO;
	
	@Autowired
	public PointServiceImpl(PointDAO thePointDAO) {
		pointDAO = thePointDAO;
	}

	@Override
	public void addPoints(TimestampPayerPoints tpp) throws NotEnoughPoints {
		pointDAO.addPoints(tpp);
	}

	@Override
	public List<PayerBalance> getBalance() {
		return pointDAO.getBalance();
	}

	@Override
	public List<PayerBalance> spendPoints(int pointsToSpend) throws NotEnoughPoints {
		return pointDAO.spendPoints(pointsToSpend);
	}
}
