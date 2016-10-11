//J-
package com.whippy.poker.common.beans;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents a full deck of cards (52 cards)
 *
 * @author mdunn
 *
 */
public class Deck {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private Stack<Card> deck = new Stack<Card>();

        /**
         * Create a new deck of cards in order of Diamonds, Hearts, Clubs, Spades. Each suit also in order from TWO to ACE
         */
        public Deck(){
                for(Suit suit : Suit.values()){
                        for(Value value : Value.values()){
                                deck.push(new Card(suit, value));
                        }
                }
        }

        /**
         * Shuffle the deck of cards into a random order
         */
        public void shuffle(){
                Collections.shuffle(deck);
        }

        /**
         * Will return and remove the top card from the deck
         *
         * @return The top card of the deck
         */
        public Card getTopCard(){
                return deck.pop();
        }
}
//J+
