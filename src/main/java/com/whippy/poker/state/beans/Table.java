//J-
package com.whippy.poker.state.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        private int id;
        private TableState state;

        /**
         * Create a table with a selection of empty seats
         * @param numberOfSeats The number of seats at the table
         * @param id the id of the table
         */
        public Table(int numberOfSeats, int id){
                seats = new Seat[numberOfSeats];
                for(int i=0; i<numberOfSeats; i++){
                        seats[i] = new Seat(i);
                }
                state = TableState.PENDING_DEAL;
        }

        /**
         *
         * @return the id of the table
         */
        public int getId() {
                return id;
        }

        /**
         *
         * @return the number of seats the table
         */
        public int getSize(){
                return seats.length;
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


        /**
         * Sit the player in a random free seat at the table
         *
         * @param player The player to sit at the table
         * @throws IllegalArgumentException if the table is full
         */
        public void seatPlayer(Player player){
                List<Seat> freeSeats = new ArrayList<Seat>();
                for(Seat seat : seats){
                        if(seat.getState().equals(SeatState.EMPTY)){
                                freeSeats.add(seat);
                        }
                }
                if(freeSeats.size()>0){
                        Collections.shuffle(freeSeats);
                        freeSeats.get(0).seatPlayer(player);
                }else{
                        throw new IllegalArgumentException("Table full");
                }
        }

        /**
         *
         * @return The current state of the table
         */
        public TableState getState() {
                return state;
        }

        /**
         * Change the state of a table
         * @param state the new state of the table
         */
        public void updateTableState(TableState state){
                this.state = state;
        }

        @Override
        public String toString(){
                StringBuilder tableString = new StringBuilder();
                tableString.append("Number: ");
                tableString.append(this.id);
                tableString.append("\nState: ");
                tableString.append(this.state);
                for(Seat seat : this.seats){
                        tableString.append("\nSeat: ");
                        tableString.append(seat.toString());
                }
                return tableString.toString();
        }

}
//J+
