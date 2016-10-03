//J-
package com.whippy.poker.state.beans;

/**
 * Represents a table of seats
 *
 * @author mdunn
 */
public class Table {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private Seat[] seats;

        /**
         * Create a table with a selection of empty seats
         * @param numberOfSeats The number of seats at the table
         */
        public Table(int numberOfSeats){
                seats = new Seat[numberOfSeats];
                for(int i=0; i<numberOfSeats; i++){
                        seats[i] = new Seat(i);
                }
        }

        /**
         * Get the specified seat at the table
         *
         * @param seatNumber
         * @return The seat at the specified number
         * @throws IllegalArgumentException if there is no seat at the given number
         */
        public Seat getSeat(int seatNumber){
                if(seatNumber>=seats.length){
                        throw new IllegalArgumentException("Table only has " + seats.length + " seats. Invalid request seat number " + seatNumber);
                }else{
                        return seats[seatNumber];
                }
        }

}
//J+
