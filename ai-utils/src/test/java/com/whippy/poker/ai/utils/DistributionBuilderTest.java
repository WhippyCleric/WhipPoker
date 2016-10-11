//J-
package com.whippy.poker.ai.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Suit;
import com.whippy.poker.common.beans.Value;

public class DistributionBuilderTest {

        @Test
        public void testFullDistribution(){
                List<Card> exclusions = new ArrayList<Card>();
                exclusions.add(new Card(Suit.DIAMONDS, Value.ACE));
                exclusions.add(new Card(Suit.HEARTS, Value.ACE));
                Distribution distribution = DistributionBuilder.buildBasicDistribution(exclusions);
                System.out.println(distribution);
        }

}
//J+
