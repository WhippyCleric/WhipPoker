//J-
package com.whippy.poker.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whippy.poker.client.exceptions.WhipPokerRequestException;
import com.whippy.poker.common.beans.ClientState;

public class WhipPokerClient {

        private String host;
        private int port;
        private ObjectMapper mapper = new ObjectMapper();

        public WhipPokerClient(String host, int port){
                this.host = host;
                this.port = port;
        }

        public void register(String alias) throws WhipPokerRequestException{
                try{

                        HttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost postRequest = new HttpPost(
                                        "http://" + host + ":" + port + "/whip-poker-server-0.1/register/registerPlayer");
                        StringEntity input = new StringEntity("{\"alias\":\"Player1\"}");
                        input.setContentType("application/json");
                        postRequest.setEntity(input);
                        HttpResponse response = httpClient.execute(postRequest);
                        if(response.getStatusLine().getStatusCode()!=200){
                                throw new WhipPokerRequestException(Utils.convert(response.getEntity().getContent()));
                        }
                }catch(IOException e){
                        throw new WhipPokerRequestException(e);
                }
        }

        public void bet(String alias, int amount) throws WhipPokerRequestException{
                sendEmptyPostRequest("http://" + host + ":" + port + "/whip-poker-server-0.1/event/bet?id=" + alias + "&amount=" + amount);
        }

        public void call(String alias) throws WhipPokerRequestException{
                sendEmptyPostRequest("http://" + host + ":" + port + "/whip-poker-server-0.1/event/call?id=" + alias);
        }

        public void fold(String alias) throws WhipPokerRequestException{
                sendEmptyPostRequest("http://" + host + ":" + port + "/whip-poker-server-0.1/event/fold?id=" + alias);
        }

        public ClientState getState(String alias) throws WhipPokerRequestException{
                try{

                        HttpClient httpClient = HttpClientBuilder.create().build();
                        HttpGet getRequest = new HttpGet(
                                        "http://" + host + ":" + port + "/whip-poker-server-0.1/state/currentState");
                        getRequest.addHeader("accept", "application/json");
                        HttpResponse response = httpClient.execute(getRequest);
                        if(response.getStatusLine().getStatusCode()!=200){
                                throw new WhipPokerRequestException(Utils.convert(response.getEntity().getContent()));
                        }
                        return mapper.readValue(Utils.convert(response.getEntity().getContent()), ClientState.class);
                }catch(IOException e){
                        throw new WhipPokerRequestException(e);
                }
        }


        private void sendEmptyPostRequest(String url) throws WhipPokerRequestException{
                try{
                        HttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost postRequest = new HttpPost(url);
                        StringEntity input = new StringEntity("{}");
                        input.setContentType("application/json");
                        postRequest.setEntity(input);
                        HttpResponse response = httpClient.execute(postRequest);
                        if(response.getStatusLine().getStatusCode()!=200){
                                throw new WhipPokerRequestException(Utils.convert(response.getEntity().getContent()));
                        }
                }catch(IOException e){
                        throw new WhipPokerRequestException(e);
                }
        }

}
//J+
