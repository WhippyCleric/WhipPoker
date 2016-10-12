//J-
package com.whippy.poker.ai.utils;

import java.util.List;

import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;

public class DistributionBuilder {

        public static Distribution buildBasicDistribution(List<Card> exclusions){
                Distribution distribution = new Distribution();
                List<Hand> hands = General.generateEveryHand(exclusions);
                Float prob = new Float(hands.size());
                prob = 1/prob;
                for (Hand hand : hands) {
                        distribution.addHand(hand, prob);
                }

                return distribution;
        }



}
//J+
