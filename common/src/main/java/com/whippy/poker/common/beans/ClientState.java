//J-
package com.whippy.poker.common.beans;

public class ClientState {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private ClientTable table;
        private Hand hand;
        private int currentBet;

        public ClientState(ClientTable table, Hand hand, int currentBet) {
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

        public int getCurrentBet() {
                return currentBet;
        }

        public void setCurrentBet(int currentBet) {
                this.currentBet = currentBet;
        }

}
//J+
