//J-
package com.whippy.poker.state.beans;

/**
 * Represents a suit, either Diamonds, Hearts, Clubs or Spades. Each having a numeric value from 0 to 3 respectively associated with them.
 *
 * @author mdunn
 *
 */
public enum Suit {


        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Enum constants
        //~ ----------------------------------------------------------------------------------------------------------------

        DIAMONDS(0), HEARTS(1), CLUBS(2), SPADES(3);


        private int numericValue;

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        private Suit(int numericValue) {
                this.numericValue = numericValue;
        }

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        /**
         * Gets the numeric value of the suit
         *
         * @return the numeric representation of the suit
         */
        public int getNumericValue() {
                return numericValue;
        }

}
//J+
