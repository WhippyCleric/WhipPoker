//J-
package com.whippy.poker.ai.utils.beans;

import java.util.HashMap;
import java.util.Map;

import com.whippy.poker.common.beans.Hand;

public class FullWinPercentages {

	private Hand myHand;
	private PossibleOutcomes chances;
	private Map<Hand, PossibleOutcomes> handToProbability = new HashMap<Hand, PossibleOutcomes>();


	public Hand getMyHand() {
		return myHand;
	}

	public void setMyHand(Hand myHand) {
		this.myHand = myHand;
	}

	public PossibleOutcomes getChances() {
		return chances;
	}

	public void setChances(PossibleOutcomes chances) {
		this.chances = chances;
	}

	public Map<Hand, PossibleOutcomes> getHandToProbability() {
		return handToProbability;
	}

	public void setHandToProbability(Map<Hand, PossibleOutcomes> handToProbability) {
		this.handToProbability = handToProbability;
	}

	public void pushHand(Hand opponentHand, PossibleOutcomes outcomes){
		handToProbability.put(opponentHand, outcomes);
	}



}
//J=