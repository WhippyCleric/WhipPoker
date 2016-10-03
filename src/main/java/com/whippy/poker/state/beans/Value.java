//J-
package com.whippy.poker.state.beans;

/**
 * Represents the value of a card, starting with TWO and going up to ACE. Each card has a numeric value ranging from 2 to 14 respectively
 * @author mdunn
 *
 */
public enum Value {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Enum constants
        //~ ----------------------------------------------------------------------------------------------------------------

        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private int numericValue;

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        private Value(int numericValue) {
                this.numericValue = numericValue;
        }

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        /**
         * Gets the numeric value of the card value
         *
         * @return the numeric value associated with a card
         */
        public int getNumericValue() {
                return numericValue;
        }

}
//J+
