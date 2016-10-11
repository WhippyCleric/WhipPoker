//J-
package com.whippy.poker.ai.utils;

import java.util.ArrayList;
import java.util.List;

import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Deck;
import com.whippy.poker.common.beans.Hand;

public class DistributionBuilder {

        public static Distribution buildBasicDistribution(List<Card> exclusions){
                Distribution distribution = new Distribution();
                List<Hand> hands = generateEveryHand(exclusions);
                Float prob = new Float(hands.size());
                prob = 1/prob;
                for (Hand hand : hands) {
                        distribution.addHand(hand, prob);
                }

                return distribution;
        }

        public static List<Hand> generateEveryHand(List<Card> exclusions){
                List<Hand> hands = new ArrayList<Hand>();
                List<Card> cards = new ArrayList<Card>();
                Deck deck = new Deck();
                for(int i=0;i<52;i++){
                        Card card = deck.getTopCard();
                        if(!exclusions.contains(card)){
                                cards.add(card);
                        }
                }
                List<Card> otherCards = new ArrayList<Card>(cards);
                for (Card card : cards) {
                        for (Card card2 : otherCards) {
                                if(!card.equals(card2)){
                                        hands.add(new Hand(card, card2));
                                }
                        }

                }

                return hands;
        }

}
//J+
