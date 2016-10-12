//J-
package com.whippy.poker.ai.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.ai.utils.beans.FullWinPercentages;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;

public class PostFlopBuilderTest {


	@Test
	public void testIt() throws Exception{
		/*
		 * This will take an extreeeeeemly long time to execute....
		 */
		List<Hand> allMyHands = General.generateEveryHand(new ArrayList<Card>());

		List<Card> allCards = General.getCards(new ArrayList<Card>());
		for(int i=0 ;i <allMyHands.size(); i++){
			List<Card> remainingCards = new ArrayList<Card>();
			remainingCards.addAll(allCards);
			remainingCards.remove(allMyHands.get(0).getHand());
			List<List<Card>> everyFlop = getEveryFlopCardCombination(remainingCards);
			for (List<Card> flop : everyFlop) {
				List<Card> excludedCards = new ArrayList<Card>();
				excludedCards.addAll(flop);
				excludedCards.addAll(Arrays.asList(allMyHands.get(0).getHand()));
				Distribution opponentDistribution = DistributionBuilder.buildBasicDistribution(excludedCards);
				FullWinPercentages winPercentage = PostFlopWinPercentageBuilder.calculateWinPercentages(allMyHands.get(0), flop, opponentDistribution);
			}
			System.out.println(i);
		}

	}


	public static List<List<Card>> getEveryFlopCardCombination(List<Card> cards){
		List<List<Card>> everyPostFlopCombination = new ArrayList<List<Card>>();
		List<Integer> indexes = new ArrayList<Integer>();
		for(int i=0; i< cards.size(); i++){
			indexes.add(i);
		}

		List<Set<Integer>> allSets = General.getSubsets(indexes, 3);
		for (Set<Integer> setOfIndexes : allSets) {
			List<Card> cardSet = new ArrayList<Card>();
			for (Integer index : setOfIndexes) {
				cardSet.add(cards.get(index));
			}
			everyPostFlopCombination.add(cardSet);
		}

		return everyPostFlopCombination;
	}

}
