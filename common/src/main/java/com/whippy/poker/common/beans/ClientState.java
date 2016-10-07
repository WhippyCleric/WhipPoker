//J-
package com.whippy.poker.common.beans;

public class ClientState {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private ClientTable table;
        private Hand hand;
        private double currentBet;

        public ClientState(ClientTable table, Hand hand, double currentBet) {
                this.table = table;
                this.hand = hand;
                this.currentBet = currentBet;
        }

        public ClientState(){

        }

        public void setTable(ClientTable table) {
                this.table = table;
        }

        public void setHand(Hand hand) {
                this.hand = hand;
        }

        public ClientTable getTable() {
                return table;
        }

        public Hand getHand() {
                return hand;
        }

        public double getCurrentBet() {
                return currentBet;
        }

        public void setCurrentBet(double currentBet) {
                this.currentBet = currentBet;
        }

}
//J+
