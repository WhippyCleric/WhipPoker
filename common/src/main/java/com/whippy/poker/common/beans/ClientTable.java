//J-
package com.whippy.poker.common.beans;

import java.util.List;

public class ClientTable {

        private ClientSeat[] seats;
        private int id;
        private TableState state;
        private int dealerPosition;
        private int currentPot;
        private List<Card> currentCards;
        private int pendingBet;

        public ClientTable(ClientSeat[] seats, int id, TableState state, int dealerPosition, int currentPot, List<Card> currentCards, int pendingBet) {
                this.seats = seats;
                this.id = id;
                this.state = state;
                this.dealerPosition = dealerPosition;
                this.pendingBet = pendingBet;
                this.setCurrentPot(currentPot);
                this.currentCards = currentCards;
        }

        public void setSeats(ClientSeat[] seats) {
                this.seats = seats;
        }

        public void setId(int id) {
                this.id = id;
        }

        public void setState(TableState state) {
                this.state = state;
        }

        public void setDealerPosition(int dealerPosition) {
                this.dealerPosition = dealerPosition;
        }

        public ClientTable(){

        }

        public ClientSeat[] getSeats() {
                return seats;
        }

        public int getId() {
                return id;
        }

        public TableState getState() {
                return state;
        }

        public int getDealerPosition() {
                return dealerPosition;
        }

        public int getCurrentPot() {
                return currentPot;
        }

        public void setCurrentPot(int currentPot) {
                this.currentPot = currentPot;
        }

        public List<Card> getCurrentCards() {
                return currentCards;
        }

        public void setCurrentCards(List<Card> currentCards) {
                this.currentCards = currentCards;
        }

        public int getPendingBet() {
                return pendingBet;
        }

        public void setPendingBet(int pendingBet) {
                this.pendingBet = pendingBet;
        }

}
//J+
