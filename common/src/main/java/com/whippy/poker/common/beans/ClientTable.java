//J-
package com.whippy.poker.common.beans;


public class ClientTable {

        private ClientSeat[] seats;
        private int id;
        private TableState state;
        private int dealerPosition;

        public ClientTable(ClientSeat[] seats, int id, TableState state, int dealerPosition) {
                this.seats = seats;
                this.id = id;
                this.state = state;
                this.dealerPosition = dealerPosition;
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

}
//J+
