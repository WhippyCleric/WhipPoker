//J-
package com.whippy.poker.ai.utils.beans;

import com.whippy.poker.common.beans.Hand;

public class HandToHandResult {

	private Hand myHand;
	private Hand opposingHand;
	private PossibleOutcomes possibleOutcomes;
	public HandToHandResult(Hand myHand, Hand opposingHand, PossibleOutcomes possibleOutcomes) {
		super();
		this.myHand = myHand;
		this.opposingHand = opposingHand;
		this.possibleOutcomes = possibleOutcomes;
	}
	public Hand getMyHand() {
		return myHand;
	}
	public Hand getOpposingHand() {
		return opposingHand;
	}
	public PossibleOutcomes getPossibleOutcomes() {
		return possibleOutcomes;
	}

}
