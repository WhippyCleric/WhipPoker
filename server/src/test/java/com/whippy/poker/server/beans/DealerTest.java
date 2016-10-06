//J-
package com.whippy.poker.server.beans;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whippy.poker.common.beans.Player;
import com.whippy.poker.common.beans.TableState;
import com.whippy.poker.server.orchestrators.Dealer;

public class DealerTest {

	//~ ----------------------------------------------------------------------------------------------------------------
	//~ Methods
	//~ ----------------------------------------------------------------------------------------------------------------

	private static final int TABLE_ID = 0;
	private static final int TABLE_SIZE = 10;
	private static final String PLAYER_1 = "player1";
	private static final String PLAYER_2 = "player2";
	private static final String PLAYER_3 = "player3";
	private static final String PLAYER_4 = "player4";
	private static final String PLAYER_5 = "player5";
	private static final int INIT_CHIPS = 1000;

	private Table table;

	@Before
	public void setup(){
		this.table = new Table(TABLE_SIZE, TABLE_ID);
	}

	@Test
	public void testDealer() throws InterruptedException {
		//Create a table with 10 empty seats


		//Create a couple of players
		Player player1 = new Player(PLAYER_1);
		Player player2 = new Player(PLAYER_2);
		Player player3 = new Player(PLAYER_3);
		Player player4 = new Player(PLAYER_4);
		Player player5 = new Player(PLAYER_5);

		//Sit the players at the table
		table.seatPlayer(player1);
		table.seatPlayer(player2);
		table.seatPlayer(player3);
		table.seatPlayer(player4);
		table.seatPlayer(player5);

		//Create a new dealer for the table
		Dealer dealer = new Dealer(table);

		//Start the dealer
		Thread dealerThread = new Thread(dealer);
		table.setState(TableState.PENDING_DEAL);
		dealerThread.start();

		Thread.sleep(1500);
		if(table.getState().equals(TableState.PRE_FLOP)){
			System.out.println(table.toString());
		}else{
			fail("Table should be in a hand");
		}


	}

	@After
	public void shutdown(){
		this.table.updateTableState(TableState.CLOSING);
	}

}
//J=