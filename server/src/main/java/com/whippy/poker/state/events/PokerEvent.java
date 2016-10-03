//J-
package com.whippy.poker.state.events;

/**
 *
 * Representation of Poker Event which could be sent by a player
 *
 * @author mdunn
 *
 */
public interface PokerEvent {

        /**
         * Returns the type of Event
         * @return the type of event
         */
        public PokerEventType getEventType();

        /**
         * Returns the alias of the player performing the action
         * @return the alias of the player performing the action
         */
        public String getPlayerAlias();

}
//J+
