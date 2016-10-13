//J-
package com.whippy.poker.ai.beans;

import com.whippy.poker.ai.utils.beans.Distribution;

public class BetToDistribution {

	private double bet;
	private Distribution distribution;



	public BetToDistribution(double bet, Distribution distribution) {
		this.bet = bet;
		this.distribution = distribution;
	}
	public double getBet() {
		return bet;
	}
	public void setBet(double bet) {
		this.bet = bet;
	}
	public Distribution getDistribution() {
		return distribution;
	}
	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

}
