//J-
package com.whippy.poker.server.orchestrators;

import java.util.ArrayList;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.SeatState;
import com.whippy.poker.common.beans.TableState;
import com.whippy.poker.common.events.BetEvent;
import com.whippy.poker.common.events.PokerEvent;
import com.whippy.poker.common.events.PokerEventType;
import com.whippy.poker.server.beans.DealerState;
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
        private DealerState state;
        private String playerToAct = "";
        private int pendingBet = 0;
        private final static int BIG_BLIND = 10;
        private final static int SMALL_BLIND = 5;
        private final static int STARTING_STACK = 1000;
        private int bigBlindSeat = -1;

        /**
         * Create a dealer at a given table
         *
         * @param table The table the dealer will manage
         */
        public Dealer(Table table){
                this.table = table;
                state = DealerState.ACTING;
        }

        /**
         *
         * Deal a new round of cards to the table.
         *
         * @throws IllegalArgumentException if the table is IN_HAND, or there's less than 2 players
         *
         */
        public void deal(){
                if(!table.getState().equals(TableState.PENDING_DEAL)){
                        throw new IllegalArgumentException("Hand is currently in play");
                }else if(table.getSeatedPlayers()<2){
                        throw new IllegalArgumentException("not enough players to deal");
                }else{
                        table.updateTableState(TableState.PRE_FLOP);
                        deck = new Deck();
                        deck.shuffle();
                        for(int i=0 ; i< table.getSize(); i++){
                                Seat seat = table.getSeat(i);
                                if(seat.getState().equals(SeatState.OCCUPIED_NOHAND)){
                                        seat.giveHand(new Hand(deck.getTopCard(), deck.getTopCard()));
                                }
                        }
                        int smallBlindPosition = findNextSeat(table.getDealerPosition(), 0);
                        table.getSeat(smallBlindPosition).setCurrentBet(SMALL_BLIND);
                        table.getSeat(smallBlindPosition).getPlayer().deductChips(SMALL_BLIND);
                        int bigBlindPosition = findNextSeat(table.getDealerPosition(), 1);
                        table.getSeat(bigBlindPosition).setCurrentBet(BIG_BLIND);
                        table.getSeat(bigBlindPosition).getPlayer().deductChips(BIG_BLIND);
                        bigBlindSeat = bigBlindPosition;
                        int firstToAct = findNextSeat(table.getDealerPosition(), 2);
                        state = DealerState.WAITING_ON_PLAYER;
                        playerToAct = table.getSeat(firstToAct).getPlayer().getAlias();
                        pendingBet = BIG_BLIND;
                        table.getSeat(firstToAct).triggerAction();
                }
        }

        private void setupNextPlayer(int nextToAct){
                playerToAct = table.getSeat(nextToAct).getPlayer().getAlias();
                //Check if we have circled the table
                if(table.getSeat(nextToAct).getCurrentBet()==pendingBet && pendingBet!=0){
                        if(nextToAct == bigBlindSeat){
                                //incase we are on the first circle
                                bigBlindSeat = -1;
                                //OPTION TO RAISE
                                table.getSeat(nextToAct).triggerAction();
                        }else{
                                collectPot();
                                triggerNextStep();
                        }
                }else if(pendingBet==0){
                        if(nextToAct == bigBlindSeat){
                                bigBlindSeat = -1;
                                //OPTION TO RAISE
                                table.getSeat(nextToAct).triggerAction();
                        }else{
                                table.getSeat(nextToAct).triggerAction();
                        }
                }else{
                        table.getSeat(nextToAct).triggerAction();
                }
        }

        private void processCall(String playerAlias){
                Seat playerSeat = table.getSeatForPlayer(playerAlias);
                playerSeat.getPlayer().deductChips(pendingBet-playerSeat.getCurrentBet());
                playerSeat.setCurrentBet(pendingBet);
                playerSeat.setState(SeatState.OCCUPIED_WAITING);
                int nextToAct = findNextSeat(playerSeat.getNumber(), 0);
                setupNextPlayer(nextToAct);
        }

        private void processBet(String playerAlias, int amount){
                if(amount>table.getSeatForPlayer(playerAlias).getPlayer().getChipCount()){
                        throw new IllegalArgumentException("Can not bet more chips than you have");
                }
                Seat playerSeat = table.getSeatForPlayer(playerAlias);
                playerSeat.getPlayer().deductChips(amount + playerSeat.getCurrentBet());
                pendingBet = playerSeat.getCurrentBet() + amount;
                playerSeat.setCurrentBet(playerSeat.getCurrentBet() + amount);
                playerSeat.setState(SeatState.OCCUPIED_WAITING);
                int nextToAct = findNextSeat(playerSeat.getNumber(), 0);
                setupNextPlayer(nextToAct);
        }

        private void processFold(String playerAlias){
                Seat playerSeat = table.getSeatForPlayer(playerAlias);
                playerSeat.setState(SeatState.OCCUPIED_NOHAND);

                int nextToAct = findNextSeat(playerSeat.getNumber(), 0);
                int numberinHand = table.getPlayersWithCards();
                if(numberinHand>1){
                        setupNextPlayer(nextToAct);
                }else{

                        collectPot();
                        for(Seat seat: table.getSeats()){
                                if(seat.getState().equals(SeatState.OCCUPIED_WAITING) || seat.getState().equals(SeatState.OCCUPIED_ACTION)){
                                        seat.setState(SeatState.OCCUPIED_NOHAND);
                                        seat.getPlayer().giveChips(table.getPot());
                                        table.emptyPot();
                                        table.setCentreCards(new ArrayList<Card>());
                                        table.setDealerPosition(findNextDealer());
                                        table.setState(TableState.PENDING_DEAL);
                                        break;
                                }
                        }
                }
        }

        private void triggerNextStep(){
                if(table.getState().equals(TableState.PRE_FLOP)){
                        dealFlop();
                }else if(table.getState().equals(TableState.PRE_TURN)){
                        dealTurn();
                }else if(table.getState().equals(TableState.PRE_RIVER)){
                        dealRiver();
                }else{
                        Seat[] seats = table.getSeats();
                        throw new UnsupportedOperationException("dunno what to do: " + table.getState());
                }
                bigBlindSeat = table.getDealerPosition();
        }

        private void dealRiver(){
                table.setState(TableState.POST_RIVER);
                table.dealCardToTable(deck.getTopCard());
                Seat nextSeat = table.getSeat(findNextSeat(table.getDealerPosition(), 0));
                playerToAct = nextSeat.getPlayer().getAlias();
                nextSeat.triggerAction();
        }

        private void dealTurn(){
                table.setState(TableState.PRE_RIVER);
                table.dealCardToTable(deck.getTopCard());
                Seat nextSeat = table.getSeat(findNextSeat(table.getDealerPosition(), 0));
                playerToAct = nextSeat.getPlayer().getAlias();
                nextSeat.triggerAction();
        }

        private void dealFlop(){
                table.setState(TableState.PRE_TURN);
                for(int i=0;i<3;i++){
                        table.dealCardToTable(deck.getTopCard());
                }
                int nextSeatNumber = findNextSeat(table.getDealerPosition(), 0);

                Seat nextSeat = table.getSeat(nextSeatNumber);
                playerToAct = nextSeat.getPlayer().getAlias();
                nextSeat.triggerAction();
        }

        private void collectPot(){
                for(Seat seat : table.getSeats()){
                        table.putIntoPut(seat.getCurrentBet());
                        seat.setCurrentBet(0);
                }
        }

        public void processAction(PokerEvent event){
                String playerAlias = event.getPlayerAlias();
                if(playerAlias.equals(playerToAct)){
                        state = DealerState.ACTING;
                        if(event.getEventType().equals(PokerEventType.CALL)){
                                processCall(playerAlias);
                        }else if(event.getEventType().equals(PokerEventType.FOLD)){
                                processFold(playerAlias);
                        }else if(event.getEventType().equals(PokerEventType.BET)){
                                processBet(playerAlias, ((BetEvent) event).getChipAmount());
                        }
                        state = DealerState.WAITING_ON_PLAYER;
                }else{
                        throw new IllegalArgumentException("Not this players turn to act");
                }
        }


        private void giveStartingStack(int startingStack){
                for(int i=0 ; i< table.getSize(); i++){
                        Seat seat = table.getSeat(i);
                        if(seat.getState().equals(SeatState.OCCUPIED_NOHAND)){
                                seat.getPlayer().giveChips(startingStack);
                        }
                }
        }


        @Override
        public void run() {
                //Give starting stacks
                giveStartingStack(STARTING_STACK);

                //While the table is not being closed
                while(!table.getState().equals(TableState.CLOSING)){
                        try {
                                Thread.sleep(1000);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
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

        private int findNextDealer(){
                int next = table.getDealerPosition()+1;
                while(true){
                        if(next>=table.getSize()){
                                next = next - table.getSize();
                        }
                        if(table.getSeat(next).getState().equals(SeatState.OCCUPIED_NOHAND)){
                                return next;
                        }
                        next++;
                }

        }
}
//J+
