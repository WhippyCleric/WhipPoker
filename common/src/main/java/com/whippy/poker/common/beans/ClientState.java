//J-
package com.whippy.poker.common.beans;

public class ClientState {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private ClientTable table;
        private Hand hand;

        public ClientState(ClientTable table, Hand hand) {
                this.table = table;
                this.hand = hand;
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

}
//J+
