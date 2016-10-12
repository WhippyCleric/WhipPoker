//J-
package com.whippy.poker.ai.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whippy.poker.ai.utils.beans.FullWinPercentages;
import com.whippy.poker.ai.utils.beans.PossibleOutcomes;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.Suit;
import com.whippy.poker.common.beans.Value;

public class WinningCalculator {

	private static final String RAW_PROBS = "probabilities/rawPreFlop/";
	private static final Float handChance = new Float(0.0008163265306);

	private Map<Hand, FullWinPercentages> allHandsFullWin = new HashMap<Hand, FullWinPercentages>();

	public WinningCalculator() {
		List<Hand> eachHand = General.generateEveryHand(new ArrayList<Card>());

		for (Hand hand : eachHand) {
			InputStream is = getClass().getClassLoader().getResourceAsStream(RAW_PROBS + hand.getHand()[0] + " " + hand.getHand()[1] + ".csv");
			if(is==null){
				is = getClass().getClassLoader().getResourceAsStream(RAW_PROBS + hand.getHand()[1] + " " + hand.getHand()[0] + ".csv");
			}
			if(is==null){
				System.err.println("Unable to find raw data for: " + hand);
			}else{
				FullWinPercentages winForHand = new FullWinPercentages();
				winForHand.setMyHand(hand);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				Float winningChance = new Float(0.0);
				Float drawChance = new Float(0.0);
				Float lossChance = new Float(0.0);
				String line;
				try {
					line = br.readLine();
					while(line!=null){
						String[] items  = line.split(",");
						if(items.length==4){
							String[] cardStrings = items[0].split(" ");
							if(cardStrings.length==4){
								Hand opponentHand = new Hand(new Card(Suit.valueOf(cardStrings[0]), Value.valueOf(cardStrings[1])), new Card(Suit.valueOf(cardStrings[2]), Value.valueOf(cardStrings[3])));
								PossibleOutcomes outcomes = new PossibleOutcomes(new Float(items[1]), new Float(items[2]), new Float(items[3]));
								winningChance = winningChance + (handChance * new Float(items[1]));
								drawChance = drawChance + (handChance * new Float(items[2]));
								lossChance = lossChance + (handChance * new Float(items[3]));
								winForHand.pushHand(opponentHand, outcomes);
							}
						}
						line=br.readLine();
					}
					PossibleOutcomes chances = new PossibleOutcomes(winningChance, drawChance, lossChance);
					winForHand.setChances(chances );
					getAllHandsFullWin().put(hand, winForHand);
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
		}

	}

	public Map<Hand, FullWinPercentages> getAllHandsFullWin() {
		return allHandsFullWin;
	}

}
