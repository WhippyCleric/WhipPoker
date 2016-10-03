//J-
package com.whippy.poker.state.beans;

public class Card {

        private Suit suit;
        private Value value;

        public Card(Suit suit, Value value){
                if(suit==null || value == null){
                        throw new IllegalArgumentException("Null value passed for either Suit or Value: Suit: " + suit + " Value: " + value);
                }
                this.suit = suit;
                this.value = value;
        }

        public Suit getSuit() {
                return suit;
        }

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

}
//J+
