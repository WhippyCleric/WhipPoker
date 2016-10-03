//J-
package com.whippy.poker.common.events;

/**
 * A PokerEvent representing a timeout
 * @author mdunn
 *
 */
public class TimeoutEvent implements PokerEvent {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private String playerAlias;

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        /**
         * Create a new timeout event
         *
         * @param playerAlias The alias of the player performing the action
         */
        public TimeoutEvent(String playerAlias) {
                this.playerAlias = playerAlias;
        }

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Override
        public PokerEventType getEventType() {
                return PokerEventType.TIMEOUT;
        }

        @Override
        public String getPlayerAlias() {
                return playerAlias;
        }

}
//J+
