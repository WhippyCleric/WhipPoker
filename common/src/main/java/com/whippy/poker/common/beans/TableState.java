//J-
package com.whippy.poker.common.beans;

/**
 * Represents the state of a table, either IN_HAND or PENDING
 *
 * @author mdunn
 *
 */
public enum TableState {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Enum constants
        //~ ----------------------------------------------------------------------------------------------------------------

        PENDING_DEAL, PRE_FLOP, PRE_TURN, PRE_RIVER, POST_RIVER, CLOSING;

}
//J+
