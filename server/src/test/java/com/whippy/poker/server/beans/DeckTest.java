//J-
package com.whippy.poker.server.beans;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Deck;
import com.whippy.poker.common.beans.Suit;
import com.whippy.poker.common.beans.Value;


public class DeckTest {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Test
        public void testShuffle() {
                Deck deck = new Deck();
                deck.shuffle();
                if(deck.getTopCard().equals(new Card(Suit.SPADES, Value.ACE))){
                        if(deck.getTopCard().equals(new Card(Suit.SPADES, Value.KING))){
                                fail("Top 2 cards are as if deck not shuffled");
                        }
                }
        }

}
//J+
