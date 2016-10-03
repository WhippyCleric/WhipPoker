//J-
package com.whippy.poker.state.beans;

/**
 * Represents a Card containing a Suit and a Value
 *
 * @author mdunn
 *
 */
public class Card {

        private Suit suit;
        private Value value;

        /**
         * Create a card with a given suit and value
         *
         * @param suit the suit of the card
         * @param value the value of the card
         */
        public Card(Suit suit, Value value){
                if(suit==null || value == null){
                        throw new IllegalArgumentException("Null value passed for either Suit or Value: Suit: " + suit + " Value: " + value);
                }
                this.suit = suit;
                this.value = value;
        }

        /**
         *
         * @return the suit of the card
         */
        public Suit getSuit() {
                return suit;
        }

        /**
         *
         * @return the value of the card
         */
        public Value getValue() {
                return value;
        }

        @Override
        public boolean equals(Object obj){
                if(obj instanceof Card){
                        if(((Card) obj).getSuit().equals(getSuit()) && ((Card) obj).getValue().equals(getValue())){
                                return true;
                        }
                }
                return false;
        }

        @Override
        public int hashCode(){
                return Integer.valueOf(suit.getNumericValue() + "" + value.getNumericValue());
        }

        @Override
        public String toString(){
                return getSuit().toString() + " " + getValue().toString();
        }

}
//J+
