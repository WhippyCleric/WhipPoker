//J-
package com.whippy.poker.state.orchestrators;

import com.whippy.poker.state.beans.Deck;
import com.whippy.poker.state.beans.Hand;
import com.whippy.poker.state.beans.Seat;
import com.whippy.poker.state.beans.SeatState;
import com.whippy.poker.state.beans.Table;
import com.whippy.poker.state.beans.TableState;

/**
 * Acts as the dealer for a given table
 *
 * @author mdunn
 */
public class Dealer {

        private Table table;
        private Deck deck;

        /**
         * Create a dealer at a given table
         *
         * @param table The table the dealer will manage
         */
        public Dealer(Table table){
                this.table = table;
        }

        /**
         *
         * Deal a new round of cards to the table.
         *
         * @throws IllegalArgumentException if the table is IN_HAND
         *
         */
        public void deal(){
                if(table.getState().equals(TableState.IN_HAND)){
                        throw new IllegalArgumentException("Hand is currently in play");
                }else{
                        deck = new Deck();
                        deck.shuffle();
                        for(int i=0 ; i< table.getSize(); i++){
                                Seat seat = table.getSeat(i);
                                if(seat.getState().equals(SeatState.OCCUPIED_NOHAND)){
                                        seat.giveHand(new Hand(deck.getTopCard(), deck.getTopCard()));
                                }
                        }
                }
        }


}
//J+
