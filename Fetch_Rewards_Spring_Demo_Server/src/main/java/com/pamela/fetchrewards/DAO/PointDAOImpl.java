package com.pamela.fetchrewards.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.pamela.fetchrewards.persistence.NotEnoughPoints;
import com.pamela.fetchrewards.persistence.PayerBalance;
import com.pamela.fetchrewards.persistence.PayerPointRecords;
import com.pamela.fetchrewards.persistence.TimestampPayerPoints;

@Component
public class PointDAOImpl implements PointDAO {
	
	private PayerPointRecords payerPointRecords;
	
	@Autowired
	public PointDAOImpl (PayerPointRecords thePayerPointRecords) {
		payerPointRecords = thePayerPointRecords;
	}

	@Override
	public void addPoints(TimestampPayerPoints tpp) throws NotEnoughPoints {
		payerPointRecords.addPoints(tpp);
		return;
	}

	@Override
	public List<PayerBalance> getBalance() {
		return payerPointRecords.getBalance();
	}

	@Override
	public List<PayerBalance> spendPoints(int pointsToSpend) throws NotEnoughPoints {
		return payerPointRecords.spendPoints(pointsToSpend);
	}
}
