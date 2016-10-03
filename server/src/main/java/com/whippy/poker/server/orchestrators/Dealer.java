//J-
package com.whippy.poker.server.orchestrators;

import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.SeatState;
import com.whippy.poker.common.beans.TableState;
import com.whippy.poker.server.beans.Deck;
import com.whippy.poker.server.beans.Seat;
import com.whippy.poker.server.beans.Table;

/**
 * Acts as the dealer for a given table
 *
 * @author mdunn
 */
public class Dealer implements Runnable {

        private Table table;
        private Deck deck;


        /**
         * Create a dealer at a given table
         *
         * @param table The table the dealer will manage
         */
        public Dealer(Table table){
                this.table = table;
                table.seatDealer();
        }

        /**
         *
         * Deal a new round of cards to the table.
         *
         * @throws IllegalArgumentException if the table is IN_HAND, or there's less than 2 players
         *
         */
        public void deal(){
                if(table.getState().equals(TableState.IN_HAND)){
                        throw new IllegalArgumentException("Hand is currently in play");
                }else if(table.getSeatedPlayers()<2){
                        throw new IllegalArgumentException("not enough players to deal");
                }else{
                        table.updateTableState(TableState.IN_HAND);
                        deck = new Deck();
                        deck.shuffle();
                        for(int i=0 ; i< table.getSize(); i++){
                                Seat seat = table.getSeat(i);
                                if(seat.getState().equals(SeatState.OCCUPIED_NOHAND)){
                                        seat.giveHand(new Hand(deck.getTopCard(), deck.getTopCard()));
                                }
                        }
                        int firstToAct = findNextSeat(table.getDealerPosition(), 2);
                        table.getSeat(firstToAct).triggerAction();
                }
        }

        @Override
        public void run() {
                //While the table is not being closed
                while(!table.getState().equals(TableState.CLOSING)){
                        if(table.getState().equals(TableState.PENDING_DEAL)){
                                deal();
                        }
                }

        }

        private int findNextSeat(int currentSeat, int offset){
                int next = currentSeat+1;
                while(true){
                        if(next>=table.getSize()){
                                next = next - table.getSize();
                        }
                        if(table.getSeat(next).getState().equals(SeatState.OCCUPIED_WAITING)){
                                if(offset==0){
                                        return next;
                                }else{
                                        offset--;
                                }
                        }
                        next++;
                }

        }
}
//J+
