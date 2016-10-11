//J-
package com.whippy.poker.client;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import com.whippy.poker.client.exceptions.WhipPokerRequestException;
import com.whippy.poker.common.beans.ClientState;
import com.whippy.poker.common.beans.TableState;


public class WhipPokerClientTest {

        @Test
        public void testRegister() throws ClientProtocolException, IOException, UnsupportedOperationException, WhipPokerRequestException{
                WhipPokerClient client = new WhipPokerClient("pmdunn-new", 8080);
                client.register("Player1");
        }

        @Test
        public void testGetState() throws ClientProtocolException, IOException, UnsupportedOperationException, WhipPokerRequestException{
                WhipPokerClient client = new WhipPokerClient("pmdunn-new", 8080);
                client.register("Player2");
                ClientState state = client.getState("Player2");
                assertTrue(state.getTable().getState().equals(TableState.PENDING_START));
        }

}
//J+
