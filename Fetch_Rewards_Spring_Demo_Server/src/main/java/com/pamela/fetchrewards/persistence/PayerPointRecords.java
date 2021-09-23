package com.pamela.fetchrewards.persistence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.stereotype.Component;

/* This is the respository class for FetchRewards coding exercise.  
 */
@Component
public class PayerPointRecords {
	private Map<String, PayerPoints> pointsByPayer;
	int allPayersTotal;
	
	public PayerPointRecords() {
		pointsByPayer = new HashMap<String, PayerPoints>();
		allPayersTotal = 0;
	}
	
	/*
	 * This can also handle the case of negative points for an add.
	 */
	public void addPoints(TimestampPayerPoints tpp) throws NotEnoughPoints {
		PayerPoints pp = null;
		if (pointsByPayer.containsKey(tpp.getPayer())) {
			pp = pointsByPayer.get(tpp.getPayer());
		}
		else {
			pp = new PayerPoints(tpp.getPayer());
		}
		/* Adding negative points */
		if (tpp.getPoints() < 0) {
			/* Do we have enough? */
			if (pp.getPoints() + tpp.getPoints() < 0) {
				throw new NotEnoughPoints("Payer Balance for " 
					+ tpp.getPayer() + " cannot go negative.");
			}
			else {
				pp.addNegativePoints(tpp);
			}
		}
		else {
			pp.addPoints(tpp);
			pointsByPayer.put(tpp.getPayer(), pp);
		}
		allPayersTotal += tpp.getPoints();
	}
	
	/*
	 * Go through the payer-level records and create a list of payer/balance
	 * records.
	 */
	public List<PayerBalance> getBalance() {
		List<PayerBalance> output = new ArrayList<PayerBalance>();
		for (Map.Entry<String, PayerPoints> entry: pointsByPayer.entrySet()) {
			PayerBalance pb = new PayerBalance(entry.getKey(), 
				entry.getValue().getPoints());
			output.add(pb);
		}
		return output;
	}
	
	/*
	 * We need to get the oldest timestamp-level records for all payers.
	 * Organize a priority queue and add the oldest record from each payer.
	 * If we completely consume the points in a timestamp record, delete
	 * it from its payer and add a new oldest timestamp record (if it exists) for
	 * that payer to the queue.
	 */
	public List<PayerBalance> spendPoints(int pointsToSpend) throws NotEnoughPoints {
		if (pointsToSpend > allPayersTotal) {
			throw new NotEnoughPoints("Point balance " + allPayersTotal +
				" not enough to spend "
				+ pointsToSpend + " points.");
		}
		/* Structure to accumulate points spent per payer */
		Map<String, Integer> pointsUsed = new HashMap<String, Integer>();
		
		/* Keep a priority queue of TimestampPayerPoints from all of the 
		 * payers.  Initialize with one entry from each payer ...
		 */
		PriorityQueue<TimestampPayerPoints> queue = new PriorityQueue<>();
		for (Map.Entry<String, PayerPoints> entry: pointsByPayer.entrySet())
		{
			TimestampPayerPoints tpp = entry.getValue().getOldest();
			if (tpp != null) {
				queue.add(tpp);
			}
		}
		
		/*
		 * Go through the queue of oldest records.  Either a complete 
		 * consumption of the record or a partial consumption of the record
		 * will occur.
		 */
		while (pointsToSpend > 0) {
			TimestampPayerPoints old = queue.peek();
		
			/* Partial consumption of timestamp record */
			if (old.getPoints() > pointsToSpend) {
				// Delete the points at the timestamp level
				old.deletePoints(pointsToSpend);
				// Delete the total points at the payer level.
				pointsByPayer.get(old.getPayer()).deletePoints(pointsToSpend);
				// Add the points and payer for return statement
				addPointsSpent(old.getPayer(), pointsToSpend, pointsUsed);
				pointsToSpend = 0;
			} 
			else {  /* Complete consumption of the timestamp record */
				// Get the payer-level record
				PayerPoints pp = pointsByPayer.get(old.getPayer());
				// Delete points at the payer record level
				pp.deletePoints(old.getPoints());
				// Delete the oldest timestamp record from the payer level
				pp.deleteOldest();
				// Get the next-oldest record from this payer and add to the queue
				TimestampPayerPoints tpp = pp.getOldest();
				if (tpp != null) {
					/* Add new entry from this payer, if it exists */
					queue.add(tpp);
				}
				/* Since we've consumed these points, remove from queue */
				queue.remove();
				addPointsSpent(old.getPayer(), old.getPoints(), pointsUsed);
				pointsToSpend -= old.getPoints();
			}
		}
		allPayersTotal -= pointsToSpend;
		
		return formatResults(pointsUsed);
	}
	
	/*
	 * This adds points spent and payer for the return ...
	 */
	private void addPointsSpent(String payer, int points, 
		Map<String, Integer> pointsUsed) {
		if (pointsUsed.containsKey(payer)) {
			int totalPointsUsed = pointsUsed.get(payer);
			totalPointsUsed += points;
			pointsUsed.put(payer,  totalPointsUsed);	
		}
		else {
			pointsUsed.put(payer,  points);
		}
	}
	
	private List<PayerBalance> formatResults (Map<String, Integer> pointsUsed) {
	
		List<PayerBalance> results = new ArrayList<PayerBalance>();	
		/* Create a list with negative numbers of PayerBalance to return */
		for (Map.Entry<String, Integer> entry: pointsUsed.entrySet()) {
			PayerBalance pb = new PayerBalance(entry.getKey(), -1 * entry.getValue());
			results.add(pb);
		}
		return results;
	}
}
