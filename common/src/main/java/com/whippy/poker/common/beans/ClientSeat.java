//J-
package com.whippy.poker.common.beans;

public class ClientSeat {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private Player player;
        private SeatState state;
        private int number;
        private int currentBet;
        private Hand hand;

        public ClientSeat(Player player, SeatState state, int number, int currentBet, Hand hand) {
                this.player = player;
                this.state = state;
                this.number = number;
                this.currentBet = currentBet;
                this.hand = hand;
        }

        public int getCurrentBet() {
                return currentBet;
        }

        public void setCurrentBet(int currentBet) {
                this.currentBet = currentBet;
        }

        public ClientSeat(){}

        public void setPlayer(Player player) {
                this.player = player;
        }

        public void setState(SeatState state) {
                this.state = state;
        }

        public void setNumber(int number) {
                this.number = number;
        }

        public Player getPlayer() {
                return player;
        }

        public SeatState getState() {
                return state;
        }

        public int getNumber() {
                return number;
        }

        public Hand getHand() {
                return hand;
        }

        public void setHand(Hand hand) {
                this.hand = hand;
        }


}
//J+
