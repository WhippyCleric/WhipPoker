//J-
package com.whippy.poker.server.beans;

import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.Player;
import com.whippy.poker.common.beans.SeatState;

/**
 * Represents a seat at a table
 *
 * @author mdunn
 */
public class Seat {

        private Player player;
        private SeatState state;
        private Hand hand;

        private final int number;
        private double currentBet = 0;


        /**
         *
         * @return the id of the seat number
         */
        public int getNumber() {
                return number;
        }

        public void setHand(Hand hand) {
                this.hand = hand;
        }


        /**
         * Create an empty seat
         *
         * @param number The number of the seat (should be unique per table)
         */
        public Seat(int number){
                this.number = number;
                this.state = SeatState.EMPTY;
        }

        /**
         * Seat the specified player, changes state of seat to OCCUPIED_NOHAND
         * @param player
         * @throws IllegalArgumentException if the seat is already occupied
         */
        public void seatPlayer(Player player){
                if(!this.state.equals(SeatState.EMPTY)){
                        throw new IllegalArgumentException("Seat already occupied");
                }else{
                        this.player = player;
                        this.state = SeatState.OCCUPIED_NOHAND;
                }
        }



        public double getCurrentBet() {
                return currentBet;
        }

        public void setCurrentBet(double currentBet) {
                this.currentBet = currentBet;
        }

        /**
         * Gives the seat a hand, and changes the state to OCCUPIED_WAITING
         *
         * @param hand
         * @throws IllegalArgumentException if the seat already has a hand or no player is sat there
         */
        public void giveHand(Hand hand){
                if(!this.state.equals(SeatState.OCCUPIED_NOHAND)){
                        throw new IllegalArgumentException("Seat is either not occupied or already has a hand");
                }else{
                        this.hand = hand;
                        this.state = SeatState.OCCUPIED_WAITING;
                }
        }




        public void setState(SeatState state) {
                this.state = state;
        }

        /**
         *
         * @return the current SeatState
         */
        public SeatState getState() {
                return state;
        }

        /**
         * Changes the state of the seat to OCCUPIED_ACTION to represent it is this seats turn to act
         *
         * @throws  IllegalArgumentException if the seat is not OCCUPIED_WAITING
         */
        public void triggerAction(){
                if(!this.state.equals(SeatState.OCCUPIED_WAITING)){
                        throw new IllegalArgumentException("Seat is not current waiting, can't trigger action");
                }else{
                        this.state = SeatState.OCCUPIED_ACTION;
                }
        }

        @Override
        public String toString(){
                StringBuilder seatString = new StringBuilder();
                seatString.append("Number: ");
                seatString.append(this.number);
                seatString.append("\nState: ");
                seatString.append(this.state);
                seatString.append("\nPlayer: ");
                if(this.player!=null){
                        seatString.append(this.player.toString());
                }
                seatString.append("\nHand: ");
                if(this.hand!=null){
                        seatString.append(this.hand.toString());
                }
                return seatString.toString();
        }

        public Player getPlayer() {
                return this.player;
        }

        public Hand getHand() {
                return this.hand;
        }

}
//J+
