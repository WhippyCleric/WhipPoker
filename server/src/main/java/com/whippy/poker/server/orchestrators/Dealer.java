//J-
package com.whippy.poker.server.orchestrators;

import java.util.ArrayList;
import java.util.List;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Deck;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.SeatState;
import com.whippy.poker.common.beans.TableState;
import com.whippy.poker.common.events.BetEvent;
import com.whippy.poker.common.events.PokerEvent;
import com.whippy.poker.common.events.PokerEventType;
import com.whippy.poker.server.beans.DealerState;
import com.whippy.poker.server.beans.Seat;
import com.whippy.poker.server.beans.Table;
import com.whippy.poker.sever.analyser.HandAnalyser;

/**
 * Acts as the dealer for a given table
 *
 * @author mdunn
 */
public class Dealer {

        private Table table;
        private Deck deck;
        private DealerState state;
        private String playerToAct = "";
        int firstToAct = -1;
        private final static int BIG_BLIND = 10;
        private final static int SMALL_BLIND = 5;
        private final static int STARTING_STACK = 1000;
        /**
         * Create a dealer at a given table
         *
         * @param table The table the dealer will manage
         */
        public Dealer(Table table){
                this.table = table;
                state = DealerState.ACTING;
        }

        public boolean activeTable(){
                int count = 0;
                for (Seat seat : table.getSeats()) {
                        if(!seat.getState().equals(SeatState.EMPTY)){
                                if(seat.getPlayer()!=null){
                                        if(seat.getPlayer().getChipCount()>0){
                                                count++;
                                                System.out.println("adding seat " + seat + " to the count");

                                        }
                                }
                        }

                }
                return count>1;
        }


        /**
         *
         * Deal a new round of cards to the table.
         *
         * @throws IllegalArgumentException if the table is IN_HAND, or there's less than 2 players
         *
         */
        public void deal(){
                System.out.println("dealing");
                if(!table.getState().equals(TableState.PENDING_DEAL)){
                        throw new IllegalArgumentException("Hand is currently in play");
                }else if(!activeTable()){
                        table.setState(TableState.CLOSING);
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
                        firstToAct = findNextSeat(table.getDealerPosition(), 2);
                        state = DealerState.WAITING_ON_PLAYER;
                        playerToAct = table.getSeat(firstToAct).getPlayer().getAlias();
                        table.setPendingBet(BIG_BLIND);
                        table.getSeat(firstToAct).triggerAction();
                }
        }

        private void setupNextPlayer(int nextToAct){
                playerToAct = table.getSeat(nextToAct).getPlayer().getAlias();
                System.out.println("Next player in the loop is " + playerToAct);
                //Check if we have circled the table
                if(table.getSeat(nextToAct).getCurrentBet()==table.getPendingBet() && table.getState().equals(TableState.PRE_FLOP)){
                        //Is the next player the original player...
                        if(nextToAct == firstToAct){
                                collectPot();
                                triggerNextStep();
                        }else{
                                if(table.getSeat(nextToAct).getCurrentBet()!=BIG_BLIND){
                                        collectPot();
                                        triggerNextStep();
                                }else{
                                        table.getSeat(nextToAct).triggerAction();
                                }
                        }
                }else if(table.getSeat(nextToAct).getCurrentBet()==table.getPendingBet() && table.getPendingBet()!=0){
                        //we have the same amount in as required to call therefore the round is over
                        System.out.println("The round is over");
                        collectPot();
                        triggerNextStep();
                }else if(table.getSeat(nextToAct).getCurrentBet()==table.getPendingBet() && table.getPendingBet()==0){
                        //check if we are the first to act
                        if(nextToAct == firstToAct){
                                System.out.println("The round is over");
                                collectPot();
                                triggerNextStep();
                        }else{
                                System.out.println("Trigering next player");
                                table.getSeat(nextToAct).triggerAction();
                        }
                }else if(table.getSeat(nextToAct).getCurrentBet()>table.getPendingBet()){
                        //Logically this means everyone else is ALL IN
                        System.out.println("Should give some money back...");
                        table.getSeat(nextToAct).getPlayer().giveChips(table.getSeat(nextToAct).getCurrentBet()-table.getPendingBet());
                        table.getSeat(nextToAct).setCurrentBet(table.getPendingBet());
                        runAllIn();
                }
                else{
                        System.out.println("Trigering next player");
                        table.getSeat(nextToAct).triggerAction();
                }
        }

        private void pause(int miliseconds){
                try {
                        Thread.sleep(miliseconds);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        private void runAllIn(){
                System.out.println("ALLIN");
                if(table.getState().equals(TableState.PRE_FLOP)){
                        dealFlop();
                        pause(2000);
                        dealTurn();
                        pause(2000);
                        dealRiver();
                        pause(2000);
                        table.setPendingBet(0);
                }else if(table.getState().equals(TableState.PRE_TURN)){
                        dealTurn();
                        pause(2000);
                        dealRiver();
                        pause(2000);
                        table.setPendingBet(0);
                }else if(table.getState().equals(TableState.PRE_RIVER)){
                        dealRiver();
                        pause(2000);
                        table.setPendingBet(0);
                }

                List<Seat> seatsInHand = table.getSeatsInHand();
                List<Seat> winningSeats = getWinningSeats(seatsInHand);
                collectPot();
                table.setState(TableState.SHOWDOWN);
                //Give the clients a few seconds to get the latest state:
                pause(6000);
                for (Seat winningSeat : winningSeats) {
                        winningSeat.getPlayer().giveChips(table.getPot()/winningSeats.size());
                }
                for (Seat seat : seatsInHand) {
                        seat.setState(SeatState.OCCUPIED_NOHAND);
                        seat.setHand(null);
                }
                for(Seat seat : table.getSeats()){
                        if(seat.getPlayer()!=null){
                                if(seat.getPlayer().getChipCount()==0){
                                        seat.setState(SeatState.EMPTY);
                                        seat.seatPlayer(null);
                                }
                        }
                }
                table.emptyPot();
                table.setCentreCards(new ArrayList<Card>());
                table.setDealerPosition(findNextDealer());
                table.setState(TableState.PENDING_DEAL);
                pause(1500);
                deal();
        }

        private void processCall(String playerAlias){
                Seat playerSeat = table.getSeatForPlayer(playerAlias);
                double amount = table.getPendingBet()-playerSeat.getCurrentBet();
                if(playerSeat.getPlayer().getChipCount()<=amount){
                        amount = playerSeat.getPlayer().getChipCount();
                        playerSeat.setCurrentBet(playerSeat.getCurrentBet()+amount);
                        table.setPendingBet(playerSeat.getCurrentBet());
                }else{
                        playerSeat.setCurrentBet(table.getPendingBet());
                }
                playerSeat.getPlayer().deductChips(amount);
                playerSeat.setState(SeatState.OCCUPIED_WAITING);
                int nextToAct = findNextSeat(playerSeat.getNumber(), 0);
                setupNextPlayer(nextToAct);
        }

        private void processBet(String playerAlias, double amount){
                if(amount>table.getSeatForPlayer(playerAlias).getPlayer().getChipCount()){
                        throw new IllegalArgumentException("Can not bet more chips than you have");
                }
                Seat playerSeat = table.getSeatForPlayer(playerAlias);
                if(playerSeat.getPlayer().getChipCount()<=amount){
                        amount = playerSeat.getPlayer().getChipCount();
                }
                playerSeat.getPlayer().deductChips(amount);
                table.setPendingBet(playerSeat.getCurrentBet() + amount);
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
                                        pause(1500);
                                        deal();
                                        break;
                                }
                        }
                }
        }

        private List<Seat> getWinningSeats(List<Seat> seatsInHand){
                Seat winningSeat = null;
                List<Seat> additionalWinners = new ArrayList<Seat>();
                for (Seat seat : seatsInHand) {
                        if(winningSeat==null){
                                winningSeat=seat;
                        }else{
                                int winner = HandAnalyser.compareHands(winningSeat.getHand(), seat.getHand(), table.getCentreCards());
                                if(winner>0){
                                        winningSeat = seat;
                                        additionalWinners = new ArrayList<Seat>();
                                }else if(winner==0){
                                        //Multiple winners
                                        additionalWinners.add(seat);
                                }
                        }
                }
                additionalWinners.add(winningSeat);
                return additionalWinners;
        }

        private void triggerNextStep(){

                int remainingChippedPlayers = 0;
                for (Seat seat : table.getSeatsInHand()) {
                        if(seat.getPlayer().getChipCount()>0){
                                remainingChippedPlayers++;
                        }
                }
                if(remainingChippedPlayers<2){
                        runAllIn();
                }else if(table.getState().equals(TableState.PRE_FLOP)){
                        dealFlop().triggerAction();
                        table.setPendingBet(0);
                }else if(table.getState().equals(TableState.PRE_TURN)){
                        dealTurn().triggerAction();
                        table.setPendingBet(0);
                }else if(table.getState().equals(TableState.PRE_RIVER)){
                        dealRiver().triggerAction();
                        table.setPendingBet(0);
                }else{

                        List<Seat> seatsInHand = table.getSeatsInHand();
                        List<Seat> winningSeats = getWinningSeats(seatsInHand);
                        collectPot();
                        table.setState(TableState.SHOWDOWN);
                        //Give the clients a few seconds to get the latest state:
                                try {
                                        Thread.sleep(6000);
                                } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                                for (Seat winningSeat : winningSeats) {
                                        winningSeat.getPlayer().giveChips(table.getPot()/winningSeats.size());
                                }
                                for (Seat seat : seatsInHand) {
                                        seat.setState(SeatState.OCCUPIED_NOHAND);
                                        seat.setHand(null);
                                }
                                for(Seat seat : table.getSeats()){
                                        if(seat.getPlayer()!=null){
                                                if(seat.getPlayer().getChipCount()==0){
                                                        seat.setState(SeatState.EMPTY);
                                                        seat.seatPlayer(null);
                                                }
                                        }
                                }
                                table.emptyPot();
                                table.setCentreCards(new ArrayList<Card>());
                                table.setDealerPosition(findNextDealer());
                                table.setState(TableState.PENDING_DEAL);
                                pause(1500);
                                deal();
                }
        }

        private Seat dealRiver(){
                table.setState(TableState.POST_RIVER);

                table.dealCardToTable(deck.getTopCard());
                firstToAct = findNextSeat(table.getDealerPosition(), 0);
                Seat nextSeat = table.getSeat(firstToAct);
                playerToAct = nextSeat.getPlayer().getAlias();
                System.out.println("Frist to act after river: " + playerToAct);
                return nextSeat;
        }

        private Seat dealTurn(){
                table.setState(TableState.PRE_RIVER);

                table.dealCardToTable(deck.getTopCard());
                firstToAct = findNextSeat(table.getDealerPosition(), 0);
                Seat nextSeat = table.getSeat(firstToAct);
                playerToAct = nextSeat.getPlayer().getAlias();
                System.out.println("Frist to act after turn: " + playerToAct);
                return nextSeat;
        }

        private Seat dealFlop(){
                table.setState(TableState.PRE_TURN);
                for(int i=0;i<3;i++){
                        table.dealCardToTable(deck.getTopCard());
                }
                int nextSeatNumber = findNextSeat(table.getDealerPosition(), 0);
                firstToAct = nextSeatNumber;
                Seat nextSeat = table.getSeat(nextSeatNumber);
                playerToAct = nextSeat.getPlayer().getAlias();
                System.out.println("Frist to act after flop: " + playerToAct);
                return nextSeat;
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



        public void run() {
                //Give starting stacks
                giveStartingStack(STARTING_STACK);

                deal();


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
