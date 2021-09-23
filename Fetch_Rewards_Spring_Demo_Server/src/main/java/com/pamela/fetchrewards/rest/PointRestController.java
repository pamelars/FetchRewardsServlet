package com.pamela.fetchrewards.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pamela.fetchrewards.persistence.NotEnoughPoints;
import com.pamela.fetchrewards.persistence.PayerBalance;
import com.pamela.fetchrewards.persistence.Points;
import com.pamela.fetchrewards.persistence.TimestampPayerPoints;
import com.pamela.fetchrewards.service.PointService;

@RestController
@RequestMapping("/api")
public class PointRestController {
	
	private PointService pointService;
	
	// quick and dirty: inject point service
	@Autowired
	public PointRestController(PointService thePointService) {
		pointService = thePointService;
	}
	
	// expose "/points" and return list of payer balance objects
	@GetMapping("/points")
	public List<PayerBalance> getBalance() {
		return pointService.getBalance();
	}
	
	// add mapping for POST /points - add points
	@PostMapping("/points")
	public void addPoints(@RequestBody TimestampPayerPoints tpp) {
		try {
			pointService.addPoints(tpp);
			return;
		}
		catch (NotEnoughPoints ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				ex.getMessage());
		}
	}
		
	// add mapping for DELETE /points - spend points
	@DeleteMapping("/points")
	public List<PayerBalance> spendPoints(@RequestBody Points pointsToSpend) {
		try {
			return pointService.spendPoints(pointsToSpend.getPoints());
		}
		catch (NotEnoughPoints ex) {
			throw new ResponseStatusException (HttpStatus.BAD_REQUEST,
				ex.getMessage());
		} 
	}	
}
