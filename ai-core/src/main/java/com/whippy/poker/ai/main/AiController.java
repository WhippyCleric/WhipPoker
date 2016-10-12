//J-
package com.whippy.poker.ai.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.whippy.poker.ai.utils.DistributionBuilder;
import com.whippy.poker.ai.utils.PostFlopWinPercentageBuilder;
import com.whippy.poker.ai.utils.WinningCalculator;
import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.ai.utils.beans.FullWinPercentages;
import com.whippy.poker.client.WhipPokerClient;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.ClientSeat;
import com.whippy.poker.common.beans.ClientState;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.SeatState;
import com.whippy.poker.common.beans.TableState;

public class AiController {

        private static final String WHIP_BOT = "WhipBot";
        private static WinningCalculator winningCalculator;

        public static void main(String[] args) throws Exception {
                WhipPokerClient client = new WhipPokerClient("pmdunn-new", 8080);
                client.register(WHIP_BOT);
                winningCalculator = new WinningCalculator();
                while(true){
                        Thread.sleep(1000);
                        ClientState state = client.getState(WHIP_BOT);
                        TableState tableState = state.getTable().getState();
                        ClientSeat mySeat = getMySeat(WHIP_BOT, state);
                        if(!tableState.equals(TableState.CLOSING) && ! tableState.equals(TableState.PENDING_START) && !tableState.equals(TableState.PENDING_DEAL) && !tableState.equals(TableState.SHOWDOWN) && mySeat.getState().equals(SeatState.OCCUPIED_ACTION)){
                                processState(state, mySeat, client);
                        }
                }
        }


        private static void processState(ClientState state, ClientSeat mySeat, WhipPokerClient client) throws Exception{
                //Maybe we have to do something...
                Hand myHand = state.getHand();

                FullWinPercentages myChances = winningCalculator.getAllHandsFullWin().get(myHand);
                //Time to do something

                if(state.getTable().getState().equals(TableState.PRE_TURN) || state.getTable().getState().equals(TableState.PRE_RIVER) || state.getTable().getState().equals(TableState.POST_RIVER)){
                        //We have a flop. Therefore we need to recalculate our win chances
                        List<Card> knownCards = new ArrayList<Card>();
                        knownCards.addAll(Arrays.asList(myHand.getHand()));
                        knownCards.addAll(state.getTable().getCurrentCards());
                        //This distribution represents all possible hands the opponent could have weighted equally
                        Distribution opponentHandDistribution = DistributionBuilder.buildBasicDistribution(knownCards);
                        myChances = PostFlopWinPercentageBuilder.calculateWinPercentages(myHand, state.getTable().getCurrentCards(), opponentHandDistribution);
                }

                if(myChances!=null){
                        double trueCurrentPot = getTrueCurrentPot(state);
                        double callCost = getCallCost(state);

                        //Calculate expected value of call
                        double postCallPot = trueCurrentPot + callCost;
                        Float callEv = new Float((myChances.getChances().getWinChance() * postCallPot) - callCost);

                        if(callCost==0){
                                System.out.println("Can check or raise...");
                                double optimumBet = getOptimumEvBet(state, mySeat, myChances.getChances().getWinChance());
                                if(optimumBet>0){
                                        client.bet(WHIP_BOT, (int)(optimumBet));
                                }else{
                                        client.call(WHIP_BOT);
                                }
                        }else{
                                if(callEv>0.5){
                                        System.out.println(myHand + " preflop chance of win " + myChances.getChances().getWinChance() + " with ev of " + callEv + " call");
                                        client.call(WHIP_BOT);
                                }else{
                                        System.out.println(myHand + " preflop chance of win " + myChances.getChances().getWinChance() + " with ev of " + callEv + " fold");
                                        client.fold(WHIP_BOT);
                                }
                        }


                }else{
                        System.out.println("not yet got data for this hand " + myHand);
                        client.fold(WHIP_BOT);
                }

        }

        private static double getOptimumEvBet(ClientState state, ClientSeat mySeat, Float winChance){
                double myChips = mySeat.getPlayer().getChipCount();
                double optimumBet = 0;
                Float prevBest = 0f;
                double trueCurrentPot = getTrueCurrentPot(state);

                for(int i =0; i< myChips && i<=trueCurrentPot; i=i+10){
                        double betSize = i;
                        double postBetPot = trueCurrentPot + betSize;
                        double postBetCallPot = postBetPot + betSize;
                        //We assume a call every time. However we should take into account the fact that a call here implies a different win chance.
                        //For now we limit our bet size to the current pot

                        /*
                         * If opposing player call a pot sized bet it modified the % chance of hands he is holding
                         * The same is true for smaller bets with a smaller shift in those %'s
                         * Therefore when calculating my Ev of a bet P(W) should not be based on an even opponent hand distribution
                         * It opponent hand distribution must be shifted as though they have called a bet of this size
                         * This is only the case once the pot is larger than 3 BB, prior to this it is too small to matter
                         */
                        Float betEv = new Float((winChance * postBetCallPot) - betSize);
                        System.out.println(betEv + " for bet size " + betSize);
                        if(betEv>prevBest){
                                prevBest = betEv;
                                optimumBet = betSize;
                        }

                }
                return optimumBet;
        }

        private static FullWinPercentages shiftedChances(FullWinPercentages original, double potBetPercentage){
                FullWinPercentages updated = new FullWinPercentages();
                updated.setMyHand(original.getMyHand());

                Float globalWinningChance = new Float(0.0);
                Float globalDrawChance = new Float(0.0);
                Float globalLossChance = new Float(0.0);

                for (Hand opponentHand : original.getHandToProbability().keySet()) {

                        Float handChance = 1.0f;
                        handChance = handChance/original.getHandToProbability().keySet().size();

                        /*
                         * In order to decide how to shift the hand chance we need to understand what the opponent would consider a good hand
                         * So firstly we need to calculate, given the opponent hand and the current available cards, what does he view his chance of winning as
                         * We will assume he does not adjust his beliefs based on our bet when doing this calculation
                         * With the result of what he thinks his hand is worth, we can deduce the likely hood of him calling a bet of our size with this hand
                         * To do this we take the % chance of him winning from his perspective called P(OW)
                         *
                         * Lets say P(OW) > 0.5 , opponent beliefs he will win
                         * For a pot sized bet opponent will calculate his Ev as always increasing with a bet, so if he calls it could be any hand where P(OW) > 0.5
                         *
                         * Now for smaller P(OW) his Ev will change.
                         * We should calculate the opponents Ev by the following:
                         * OppEv = P(OW) * (currentPot + (2*betAmount)) - betAmount
                         * If OppEv > 0 then it would be profitable for him to call assuming he is not inferring any information about our hand.
                         *
                         * Now we will remove any hand with an OppEv < 0 assume the player is good at what he does
                         *
                         * Then we can recalculate the handChance based on the resulting number of hands
                         *
                         */

                        globalWinningChance = globalWinningChance + (handChance * original.getHandToProbability().get(opponentHand).getWinChance());
                        globalDrawChance = globalDrawChance + (handChance * original.getHandToProbability().get(opponentHand).getDrawChance());
                        globalLossChance = globalLossChance + (handChance * original.getHandToProbability().get(opponentHand).getLossChance());

                }


                return updated;
        }

        private static double getCallCost(ClientState state){
                double currentBet = state.getCurrentBet();
                double pendingBet = state.getTable().getPendingBet();
                return pendingBet-currentBet;
        }

        private static double getTrueCurrentPot(ClientState state){
                double currentPot = state.getTable().getCurrentPot();
                double currentBet = state.getCurrentBet();
                double pendingBet = state.getTable().getPendingBet();
                return currentPot + currentBet + pendingBet;
        }

        private static ClientSeat getMySeat(String alias, ClientState state){
                for (ClientSeat seat : state.getTable().getSeats()) {
                        if(seat.getPlayer()!=null){
                                if(seat.getPlayer().getAlias().equals(alias)){
                                        return seat;
                                }
                        }
                }
                return null;
        }
}
//J+
