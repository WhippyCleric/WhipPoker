//J-
package com.whippy.poker.ai.utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.ai.utils.beans.WinPercentages;
import com.whippy.poker.common.analyser.HandAnalyser;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;

public class WinPercentageBuilder {

	public static WinPercentages calculateWinPercentages(Hand hand, Distribution dist, PrintWriter writer){
		WinPercentages winPercentages = new WinPercentages(hand);
		List<Card> remainingCards = General.getCards(Arrays.asList(hand.getHand()));
		int count = 0;
		for (Hand opposingHand : dist.getHandToProbability().keySet()) {
			count ++;
			//For each possible opposing hand
			//Remove opposing cards from the list
			List<Card> trueRemainingCards = new ArrayList<Card>();
			trueRemainingCards.addAll(remainingCards);
			trueRemainingCards.removeAll(Arrays.asList(opposingHand.getHand()));

			int wins = 0;
			int loses = 0;
			int draws = 0;
			List<List<Card>> everyCenterHand = General.getEvery5CardCombination();
			//For every center hand combination, do we win lose or draw

			//Monte carlo of 5000 random center sets
			for(int i=0; i<5000; i++){
				int index = (int)(Math.random() * (everyCenterHand.size()));
				List<Card> centerHand = everyCenterHand.get(index);
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
			winPercentages.addHand(opposingHand, winningChance);
			writer.println(opposingHand + "," + winningChance+"," + drawChance + "," + lostChance);
		}

		return winPercentages;
	}

}
