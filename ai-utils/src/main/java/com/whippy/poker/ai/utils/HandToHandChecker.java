//J-
package com.whippy.poker.ai.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.whippy.poker.ai.utils.beans.HandToHandResult;
import com.whippy.poker.ai.utils.beans.PossibleOutcomes;
import com.whippy.poker.common.analyser.HandAnalyser;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;

public class HandToHandChecker implements Callable<HandToHandResult>{

	private List<Card> remainingCards;
	private Hand opposingHand;
	private List<Card> centerCards;
	private Hand hand;

	public HandToHandChecker(Hand hand, List<Card> remainingCards, Hand opposingHand, List<Card> centerCards ){
		this.hand = hand;
		this.remainingCards = remainingCards;
		this.opposingHand = opposingHand;
		this.centerCards = centerCards;

	}

	@Override
	public HandToHandResult call() throws Exception {
		HandToHandResult result  = null;
		//For each possible opposing hand
		//Remove opposing cards from the list
		List<Card> trueRemainingCards = new ArrayList<Card>();
		trueRemainingCards.addAll(remainingCards);
		trueRemainingCards.removeAll(Arrays.asList(opposingHand.getHand()));

		int wins = 0;
		int loses = 0;
		int draws = 0;
		List<List<Card>> everyCenterHand = General.getEveryPostFlopCardCombination(centerCards, trueRemainingCards);
		//For every center hand combination, do we win lose or draw

		for(int i=0; i<everyCenterHand.size(); i++){
			List<Card> centerHand = everyCenterHand.get(i);
			if(!centerHand.contains(hand.getHand()[0]) && !centerHand.contains(hand.getHand()[1]) && !centerHand.contains(opposingHand.getHand()[0]) && !centerHand.contains(opposingHand.getHand()[1])){
				int winningHand = HandAnalyser.compareHands(hand, opposingHand, centerHand);
				if(winningHand<0){
					wins++;
				}else if(winningHand>0){
					loses++;
				}else{
					draws++;
				}
			}else{
				i--;
			}
		}
		//Calculate chance of winning if oponent has this hand
		Float winningChance = new Float(wins);
		Float drawChance = new Float(draws);
		Float lostChance = new Float(loses);
		winningChance = winningChance /  (wins+loses+draws);
		drawChance = drawChance /  (wins+loses+draws);
		lostChance = lostChance /  (wins+loses+draws);

		result = new HandToHandResult(hand, opposingHand,  new PossibleOutcomes(winningChance, drawChance, lostChance));
		return result;
	}

}
