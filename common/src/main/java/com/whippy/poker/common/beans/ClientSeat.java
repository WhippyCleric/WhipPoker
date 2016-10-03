//J-
package com.whippy.poker.common.beans;

public class ClientSeat {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private Player player;
        private SeatState state;
        private int number;

        public ClientSeat(Player player, SeatState state, int number) {
                this.player = player;
                this.state = state;
                this.number = number;
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

}
//J+