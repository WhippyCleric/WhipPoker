//J-
package com.whippy.poker.common.events;

/**
 * A PokerEvent representing a call action
 * @author mdunn
 *
 */
public class CallEvent implements PokerEvent {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private String playerAlias;

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        /**
         * Create a new call event
         *
         * @param playerAlias The alias of the player performing the action
         */
        public CallEvent(String playerAlias) {
                this.playerAlias = playerAlias;
        }

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Override
        public PokerEventType getEventType() {
                return PokerEventType.CALL;
        }

        @Override
        public String getPlayerAlias() {
                return playerAlias;
        }

}
//J+
