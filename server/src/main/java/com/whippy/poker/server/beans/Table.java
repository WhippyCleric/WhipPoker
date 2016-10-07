//J-
package com.whippy.poker.server.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Player;
import com.whippy.poker.common.beans.SeatState;
import com.whippy.poker.common.beans.TableState;

/**
 * Represents a table of seats
 *
 * @author mdunn
 */
public class Table {



        private Seat[] seats;
        private int id;
        private TableState state;
        private int dealerPosition;
        private int pot = 0;
        private List<Card> centreCards = new ArrayList<Card>();
        private int pendingBet;

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
                state = TableState.PENDING_START;
                dealerPosition = -1;
        }



        public List<Card> getCentreCards() {
                return centreCards;
        }

        public void dealCardToTable(Card card){
                this.centreCards.add(card);
        }

        public void setCentreCards(List<Card> centreCards) {
                this.centreCards = centreCards;
        }



        public Seat[] getSeats() {
                return seats;
        }

        public void putIntoPut(int amount){
                pot += amount;
        }

        public int getPot(){
                return pot;
        }

        public void emptyPot(){
                pot = 0;
        }

        public List<Seat> getSeatsInHand(){
                List<Seat> seatedPlayers = new ArrayList<Seat>();
                for (Seat seat : seats) {
                        if(seat.getState().equals(SeatState.OCCUPIED_ACTION) || seat.getState().equals(SeatState.OCCUPIED_WAITING)){
                                seatedPlayers.add(seat);
                        }
                }
                return seatedPlayers;
        }

        /**
         * Get the number of players sat at the table
         * @return the number of players at the table
         */
        public int getSeatedPlayers(){
                int occupiedSeats = 0;
                for(Seat seat : seats){
                        if(!seat.getState().equals(SeatState.EMPTY)){
                                occupiedSeats++;
                        }
                }
                return occupiedSeats;
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
         *
         * @return The current seat numbr for the dealer button
         */
        public int getDealerPosition() {
                return dealerPosition;
        }

        /**
         *
         * @param dealerPosition Set the current seat number for the dealer position
         */
        public void setDealerPosition(int dealerPosition) {
                this.dealerPosition = dealerPosition;
        }

        /**
         *
         * Will put the dealer button in a random occupied space
         * @throws IllegalArgumentException If the table is empty
         *
         */
        public void seatDealer(){
                List<Seat> occupiedSeats = new ArrayList<Seat>();
                for(Seat seat : seats){
                        if(!seat.getState().equals(SeatState.EMPTY)){
                                occupiedSeats.add(seat);
                        }
                }
                if(occupiedSeats.size()>0){
                        Collections.shuffle(occupiedSeats);
                        this.dealerPosition = occupiedSeats.get(0).getNumber();
                }else{
                        throw new IllegalArgumentException("Table empty");
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

        public Seat getSeatForPlayer(String alias){
                for(Seat seat : seats){
                        if(!seat.getState().equals(SeatState.EMPTY) && seat.getPlayer().getAlias().equals(alias)){
                                return seat;
                        }
                }
                throw new IllegalArgumentException("No such player found at table: " + alias );
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
                tableString.append("\nDealer Position: ");
                tableString.append(dealerPosition);
                for(Seat seat : this.seats){
                        tableString.append("\nSeat: ");
                        tableString.append(seat.toString());
                }
                return tableString.toString();
        }

        public void setState(TableState state) {
                this.state = state;

        }



        public int getPlayersWithCards() {
                int playersWithCards = 0;
                for(Seat seat : seats){
                        if(seat.getState().equals(SeatState.OCCUPIED_WAITING) || seat.getState().equals(SeatState.OCCUPIED_ACTION)){
                                playersWithCards++;
                        }
                }
                return playersWithCards;
        }



        public int getPendingBet() {
                return pendingBet;
        }

        public void setPendingBet(int pendingBet){
                this.pendingBet = pendingBet;
        }

}
//J+
