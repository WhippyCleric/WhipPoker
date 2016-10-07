//J-
package com.whippy.poker.server.orchestrators;

import javax.enterprise.context.ApplicationScoped;

import com.whippy.poker.common.beans.ClientSeat;
import com.whippy.poker.common.beans.ClientState;
import com.whippy.poker.common.beans.ClientTable;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.Player;
import com.whippy.poker.common.beans.TableState;
import com.whippy.poker.common.events.PokerEvent;
import com.whippy.poker.server.beans.Seat;
import com.whippy.poker.server.beans.Table;

@ApplicationScoped
public class GlobalOrchestrator {

        //~ ----------------------------------------------------------------------------------------------------------------
        //~ Instance fields
        //~ ----------------------------------------------------------------------------------------------------------------

        private Dealer dealer;
        private Table table;

        public GlobalOrchestrator() {
                table = new Table(10, 1);
                dealer = new Dealer(table);
        }

        public void processEvent(PokerEvent event){
                dealer.processAction(event);
        }

        public void registerPlayer(Player player){
                table.seatPlayer(player);
        }

        public void start(){
                Thread dealerThread = new Thread(dealer);
                table.setState(TableState.PENDING_DEAL);
                table.seatDealer();
                dealerThread.start();
        }

        public ClientState getState(String player){
                Seat[] seats = table.getSeats();
                ClientSeat[] clientSeats = new ClientSeat[seats.length];
                Hand hand = null;
                double currentBet = 0;
                for(int i=0; i<clientSeats.length;i++){
                        if(table.getState().equals(TableState.SHOWDOWN)){
                                clientSeats[i] = new ClientSeat(seats[i].getPlayer(), seats[i].getState(), seats[i].getNumber(), seats[i].getCurrentBet(), seats[i].getHand());
                        }else{
                                clientSeats[i] = new ClientSeat(seats[i].getPlayer(), seats[i].getState(), seats[i].getNumber(), seats[i].getCurrentBet(), null);
                        }
                        if(seats[i].getPlayer()!=null && seats[i].getPlayer().getAlias().equals(player)){
                                hand = seats[i].getHand();
                                currentBet = seats[i].getCurrentBet();
                        }
                }
                ClientTable clientTable = new ClientTable(clientSeats, table.getId(), table.getState(), table.getDealerPosition(), table.getPot(), table.getCentreCards(), table.getPendingBet());
                return new ClientState(clientTable, hand, currentBet);
        }

}
//J+
