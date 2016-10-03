//J-
package com.whippy.poker.common.events;

/**
 * A PokerEvent representing a fold action
 * @author mdunn
 *
 */
public class FoldEvent implements PokerEvent {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private String playerAlias;

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Constructors
        //~ ----------------------------------------------------------------------------------------------------------------

        /**
         * Create a new fold event
         *
         * @param playerAlias The alias of the player performing the action
         */
        public FoldEvent(String playerAlias) {
                this.playerAlias = playerAlias;
        }

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Override
        public PokerEventType getEventType() {
                return PokerEventType.FOLD;
        }

        @Override
        public String getPlayerAlias() {
                return playerAlias;
        }

}
//J+
