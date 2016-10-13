//J-
package com.whippy.poker.ai.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.ai.utils.beans.FullWinPercentages;
import com.whippy.poker.ai.utils.beans.HandToHandResult;
import com.whippy.poker.ai.utils.beans.PossibleOutcomes;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;

public class PostFlopWinPercentageBuilder {


	public static FullWinPercentages calculateWinPercentages(Hand hand, List<Card> centerCards, Distribution dist) throws Exception{
		FullWinPercentages winPercentages = new FullWinPercentages();
		winPercentages.setMyHand(hand);

		List<Card> knownCards = new ArrayList<Card>();
		knownCards.addAll(Arrays.asList(hand.getHand()));
		knownCards.addAll(centerCards);

		List<Card> remainingCards = General.getCards(knownCards);

		Float globalWinningChance = new Float(0.0);
		Float globalDrawChance = new Float(0.0);
		Float globalLossChance = new Float(0.0);

		Float handChance = new Float(1);
		handChance = handChance / dist.getHandToProbability().keySet().size();

		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<HandToHandResult>> allResults = new ArrayList<Future<HandToHandResult>>();

		for (Hand opposingHand : dist.getHandToProbability().keySet()) {


			HandToHandChecker handToHandChecker = new HandToHandChecker(hand, remainingCards, opposingHand, centerCards);
			Future<HandToHandResult> future = executor.submit(handToHandChecker);
			allResults.add(future);
		}

		for (Future<HandToHandResult> future : allResults) {
			HandToHandResult result = future.get();
			globalWinningChance = globalWinningChance + (handChance * result.getPossibleOutcomes().getWinChance());
			globalDrawChance = globalDrawChance + (handChance * result.getPossibleOutcomes().getDrawChance());
			globalLossChance = globalLossChance + (handChance * result.getPossibleOutcomes().getLossChance());

			winPercentages.pushHand(result.getOpposingHand(),  result.getPossibleOutcomes());
		}
		try {
			executor.shutdown();
			executor.awaitTermination(120, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		}
		finally {
			if (!executor.isTerminated()) {
				System.err.println("cancel non-finished tasks");
			}
			executor.shutdownNow();
		}
		winPercentages.setChances(new PossibleOutcomes(globalWinningChance, globalDrawChance, globalLossChance));
		return winPercentages;
	}
}
