/**
 *  Copyright Murex S.A.S., 2003-2016. All Rights Reserved.
 * 
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.whippy.poker.analyser;

import java.util.ArrayList;
import java.util.List;

import com.whippy.poker.common.analyser.HandAnalyser;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.Suit;
import com.whippy.poker.common.beans.Value;

import org.junit.Test;


public class HandAnalyserTest {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    @Test
    public void timingsTest() {
        Hand hand1 = new Hand(new Card(Suit.CLUBS, Value.ACE), new Card(Suit.DIAMONDS, Value.KING));
        Hand hand2 = new Hand(new Card(Suit.CLUBS, Value.THREE), new Card(Suit.CLUBS, Value.JACK));
        List<Card> centerCards = new ArrayList<Card>();
        centerCards.add(new Card(Suit.HEARTS, Value.FIVE));
        centerCards.add(new Card(Suit.CLUBS, Value.NINE));
        centerCards.add(new Card(Suit.HEARTS, Value.TWO));
        centerCards.add(new Card(Suit.HEARTS, Value.QUEEN));
        centerCards.add(new Card(Suit.HEARTS, Value.TEN));
        HandAnalyser.compareHands(hand1, hand2, centerCards);
        long time = System.nanoTime();
        System.out.println(time);
    }

}
