package com.pamela.fetchrewards.persistence;
import java.util.PriorityQueue;

/*
 * This class keeps payer-level timestamp point records.
 */
public class PayerPoints {
	private String payer;
	private int totalPoints;
	private PriorityQueue<TimestampPayerPoints> timestampPoints;
	private PriorityQueue<TimestampPayerPoints> negativePoints;
	
	public PayerPoints (String payer) {
		this.payer = payer;
		totalPoints = 0;
		timestampPoints = new PriorityQueue<TimestampPayerPoints>();
		negativePoints = new PriorityQueue<TimestampPayerPoints>();
	}
	
	public void deletePoints(int points) {
		totalPoints -= points;
	}
	
	public int getPoints() {
		return totalPoints;
	}
	
	public void addPoints(TimestampPayerPoints tpp) {
		// Increment total points first, as we might adjust the tpp ...
		totalPoints += tpp.getPoints();
		// Possible adjustment of tpp if we have negative points?
		if (! negativePoints.isEmpty()) {
			TimestampPayerPoints negative = negativePoints.peek();
			if (tpp.compareTo(negative) < 0) {
				/* Handle case of less than, more than, equal ... */
				int positivePoints = -1 * negative.getPoints();
				if (positivePoints <= tpp.getPoints()) {
					tpp.deletePoints(positivePoints);
					negativePoints.remove();
				}
				else {
					negative.deletePoints(-1 * tpp.getPoints());
					tpp.deletePoints(positivePoints);
				}
			}
		}
		if (tpp.getPoints() > 0) {
			timestampPoints.add(tpp);
		}
	}
	
	/* These must be adjustments to older points.  It might be that the 
	 * timestamp record to which these apply have not yet been submitted.
	 * If that is the case, 
	 */
	public void addNegativePoints(TimestampPayerPoints tpp) {
		if (! timestampPoints.isEmpty()) {
			TimestampPayerPoints opp = timestampPoints.peek();
			if ((opp.compareTo(tpp) < 0)) {
				int positivePoints = -1 * tpp.getPoints();
				
				/* positive larger than negative */
				if (opp.getPoints() >= positivePoints) {
					opp.deletePoints(positivePoints);
					if (opp.getPoints() == 0) {
						deleteOldest();
					}
				}
				else { /* negative larger than positive */
					tpp.deletePoints(-1 * opp.getPoints());
					negativePoints.add(tpp);
				}
			}
			else {
				negativePoints.add(tpp);
			}
		}
		else {
			negativePoints.add(tpp);
		}
		totalPoints += tpp.getPoints();
	}
	
	public String getPayer() {
		return payer;
	}
	
	public TimestampPayerPoints getOldest() {
		if (! timestampPoints.isEmpty()) {
			return timestampPoints.peek();
		}
		else {
			return null;
		}
	}
	
	public void deleteOldest() {
		if (! timestampPoints.isEmpty()) {
			timestampPoints.remove();
		}
	}

	@Override
	public String toString() {
		return "PayerPoints [payer=" + payer + ", totalPoints=" + totalPoints + ", timestampPoints=" + timestampPoints
				+ "]";
	}
}
