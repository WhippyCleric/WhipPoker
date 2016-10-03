//J-
package com.whippy.poker.state.events;

/**
 * A poker event representing a bet action
 * @author mdunn
 *
 */
public class BetEvent implements PokerEvent {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private String playerAlias;
        private int chipAmount;

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        /**
         * Create a new bet event
         *
         * @param playerAlias The alias of the player performing the action
         * @param chipAmount the size of the bet
         */
        public BetEvent(String playerAlias, int chipAmount) {
                this.playerAlias = playerAlias;
                this.chipAmount = chipAmount;
        }

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Override
        public PokerEventType getEventType() {
                return PokerEventType.BET;
        }

        @Override
        public String getPlayerAlias() {
                return playerAlias;
        }

        /**
         * Returns the size of the bet
         *
         * @return the amount of the bet
         */
        public int getChipAmount() {
                return this.chipAmount;
        }

}
//J+
