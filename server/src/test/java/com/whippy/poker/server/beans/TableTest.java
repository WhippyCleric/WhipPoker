//J-
package com.whippy.poker.server.beans;

import static org.junit.Assert.fail;
import org.junit.Test;

import com.whippy.poker.server.beans.Table;


public class TableTest {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Methods
        //~ ----------------------------------------------------------------------------------------------------------------

        @Test
        public void testInvalidSeat() {
                Table table = new Table(10, 0);
                try {
                        table.getSeat(10);
                        fail("Seat should not exist");
                } catch (IllegalArgumentException e) {
                        //expected
                }
        }

}
//J+
