//J-
package com.whippy.poker.state.beans;

import java.util.Collections;
import java.util.Stack;


public class Deck {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        public Stack<Card> deck = new Stack<Card>();

        public Deck(){
                for(Suit suit : Suit.values()){
                        for(Value value : Value.values()){
                                deck.push(new Card(suit, value));
                        }
                }
        }

        public void shuffle(){
                Collections.shuffle(deck);
        }

        public Card getTopCard(){
                return deck.pop();
        }
}
//J+
