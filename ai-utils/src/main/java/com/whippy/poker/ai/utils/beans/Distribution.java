//J-
package com.whippy.poker.ai.utils.beans;

import java.util.HashMap;
import java.util.Map;

import com.whippy.poker.common.beans.Hand;

public class Distribution {

        private Map<Hand, Float> handToProbability = new HashMap<Hand, Float>();

        public void addHand(Hand hand, Float prob){
                handToProbability.put(hand, prob);
        }

        @Override
        public String toString(){
                StringBuilder distString = new StringBuilder();
                for (Hand hand : handToProbability.keySet()) {
                        distString.append(hand);
                        distString.append(" : ");
                        distString.append(handToProbability.get(hand));
                        distString.append("\n");
                }
                return distString.toString();
        }

}
//J+
