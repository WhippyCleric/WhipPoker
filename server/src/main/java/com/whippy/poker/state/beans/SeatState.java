//J-
package com.whippy.poker.state.beans;

/**
 * Represents the state of a seat at the table. Either EMPTY (no player), OCCUPIED_WAITING(player sat with a hand waiting for their turn), OCCUPIED_NOHAND (player sat with not hand), or OCCUPIED_ACTION (player sat and it is their turn to act)
 *
 * @author mdunn
 *
 */
public enum SeatState {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Enum constants
        //~ ----------------------------------------------------------------------------------------------------------------

        EMPTY, OCCUPIED_WAITING, OCCUPIED_NOHAND, OCCUPIED_ACTION;

}
//J+
