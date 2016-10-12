//J-
package com.whippy.poker.ai.utils.beans;

import java.util.HashMap;
import java.util.Map;

import com.whippy.poker.common.beans.Hand;

public class WinPercentages {

	private Map<Hand, Float> handToProbability = new HashMap<Hand, Float>();
	public Map<Hand, Float> getHandToProbability() {
		return handToProbability;
	}

	private Hand myHand;

	public WinPercentages(Hand myHand){
		this.myHand=myHand;
	}

	public void addHand(Hand hand, Float prob){
		handToProbability.put(hand, prob);
	}

	@Override
	public String toString(){
		StringBuilder distString = new StringBuilder();
		distString.append("My Hand: ");
		distString.append(myHand);
		distString.append("\n");
		for (Hand hand : handToProbability.keySet()) {
			distString.append(hand);
			distString.append(" : ");
			distString.append(handToProbability.get(hand));
			distString.append("\n");
		}
		return distString.toString();
	}

}
