//J-
package com.whippy.poker.ai.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whippy.poker.ai.utils.DistributionBuilder;
import com.whippy.poker.ai.utils.PostFlopWinPercentageBuilder;
import com.whippy.poker.ai.utils.WinningCalculator;
import com.whippy.poker.ai.utils.beans.Distribution;
import com.whippy.poker.ai.utils.beans.FullWinPercentages;
import com.whippy.poker.client.WhipPokerClient;
import com.whippy.poker.common.analyser.HandAnalyser;
import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.ClientSeat;
import com.whippy.poker.common.beans.ClientState;
import com.whippy.poker.common.beans.FiveCardHand;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.HandValue;
import com.whippy.poker.common.beans.SeatState;
import com.whippy.poker.common.beans.Suit;
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
                System.out.println(myHand);
                FullWinPercentages myChances = winningCalculator.getAllHandsFullWin().get(myHand);
                //Time to do something

                if(state.getTable().getState().equals(TableState.PRE_RIVER) || state.getTable().getState().equals(TableState.POST_RIVER)){
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

                        if(callCost==0 && !state.getTable().getState().equals(TableState.PRE_FLOP)){
                                System.out.println("Can check or raise...");
                                //If it's post flop
                                System.out.println("Post Flop Calculations");
                                double optimumBet = getOptimumEvBet(state, mySeat, myHand, state.getTable().getCurrentCards());
                                if(optimumBet>0){
                                        client.bet(WHIP_BOT, (int)(optimumBet));
                                }else{
                                        client.call(WHIP_BOT);
                                }
                        }else if(!state.getTable().getState().equals(TableState.PRE_FLOP)){
                                // Do we want to call the bet?
                                double currentBet = state.getCurrentBet();
                                double pendingBet = state.getTable().getPendingBet();
                                double potPercentage = (state.getTable().getPendingBet()-state.getCurrentBet()) / trueCurrentPot;
                                Distribution  opponentHands = computeLikelyOpponentDistribution(potPercentage, myHand, state.getTable().getCurrentCards());
                                FullWinPercentages myChancesPostFlop = PostFlopWinPercentageBuilder.calculateWinPercentages(myHand, state.getTable().getCurrentCards(), opponentHands);
                                Float winChance = myChancesPostFlop.getChances().getWinChance();
                                Float trueCallEv = new Float((winChance * (trueCurrentPot + pendingBet - state.getCurrentBet())) - (pendingBet - state.getCurrentBet()));
                                System.out.println(callEv + " for calling");
                                if(trueCallEv>0){
                                        System.out.println(myHand + " chance of win " + myChancesPostFlop.getChances().getWinChance() + " with ev of " + trueCallEv + " call");
                                        client.call(WHIP_BOT);
                                }else{
                                        System.out.println(myHand + " chance of win " + myChancesPostFlop.getChances().getWinChance() + " with ev of " + trueCallEv + " fold");
                                        client.fold(WHIP_BOT);
                                }
                        }else{
                                if(callEv>0){
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

        private static double getOptimumEvBet(ClientState state, ClientSeat mySeat, Hand myHand, List<Card> flop) throws Exception{
                double myChips = mySeat.getPlayer().getChipCount();
                double optimumBet = 0;
                Float prevBest = 0f;
                double trueCurrentPot = getTrueCurrentPot(state);


                Distribution  opponentHandsAtHalfPot = computeLikelyOpponentDistribution(0.1, myHand, flop);
                FullWinPercentages myChancesAtHalfPot = PostFlopWinPercentageBuilder.calculateWinPercentages(myHand, state.getTable().getCurrentCards(), opponentHandsAtHalfPot);

                for(int i =0; i< myChips && i<=trueCurrentPot; i=i+(int)(trueCurrentPot/10)){
                        double betSize = i;
                        double postBetPot = trueCurrentPot + betSize;
                        double postBetCallPot = postBetPot + betSize;
                        double potPercentage = i / trueCurrentPot;


                        //We assume a call every time. However we should take into account the fact that a call here implies a different win chance.
                        //For now we limit our bet size to the current pot

                        /*
                         * If opposing player call a pot sized bet it modified the % chance of hands he is holding
                         * The same is true for smaller bets with a smaller shift in those %'s
                         * Therefore when calculating my Ev of a bet P(W) should not be based on an even opponent hand distribution
                         * It opponent hand distribution must be shifted as though they have called a bet of this size
                         * This is only the case once the pot is larger than 3 BB, prior to this it is too small to matter
                         */
                        Float winChance = myChancesAtHalfPot.getChances().getWinChance();
                        if(i>trueCurrentPot/10 && trueCurrentPot>30){
                                Distribution  opponentHands = computeLikelyOpponentDistribution(potPercentage, myHand, flop);
                                FullWinPercentages myChances = PostFlopWinPercentageBuilder.calculateWinPercentages(myHand, state.getTable().getCurrentCards(), opponentHands);
                                winChance = myChances.getChances().getWinChance();
                        }

                        Float betEv = new Float((winChance * postBetCallPot) - betSize);
                        System.out.println(betEv + " for bet size " + betSize);
                        if(betEv>prevBest){
                                prevBest = betEv;
                                optimumBet = betSize;
                        }

                }
                return optimumBet;
        }

        private static double calculatehandStrength(Hand hand, List<Card> centerCards){
                FiveCardHand bestHand = HandAnalyser.getBestHand(hand, centerCards);
                /*
                 * The idea here is to assign a numeric value to represent a hands strength, given it's potential and made amounts
                 *
                 */
                double strength = 0.0;
                Card topCard = null;
                int repeats = 0;
                for (Card card : centerCards) {
                        if(topCard==null){
                                topCard = card;
                        }else if(topCard.getValue().getNumericValue()<card.getValue().getNumericValue()){
                                topCard = card;
                        }else if(topCard.getValue().getNumericValue()==card.getValue().getNumericValue()){
                                repeats++;
                        }
                }
                boolean pairedBoard= repeats==2;
                boolean tripBoard= repeats==3;
                boolean quadBoard= repeats==3;

                Card highCard = null;
                Card otherCard = null;
                for (Card card : hand.getHand()) {
                        if(highCard==null){
                                highCard=card;
                        }else if(highCard.getValue().getNumericValue()<card.getValue().getNumericValue()){
                                otherCard = highCard;
                                highCard = card;
                        }else{
                                otherCard = card;
                        }
                }

                //First case is post flop
                if(centerCards.size()==3){
                        strength = getStrength(centerCards, bestHand, strength, pairedBoard, tripBoard, quadBoard, highCard, otherCard, true, false, false);
                }else if(centerCards.size()==4){
                        strength = getStrength(centerCards, bestHand, strength, pairedBoard, tripBoard, quadBoard, highCard, otherCard, false, true, false);
                        //We are at the turn
                }else{
                        strength = getStrength(centerCards, bestHand, strength, pairedBoard, tripBoard, quadBoard, highCard, otherCard, false, false, true);
                }

                return strength;
        }


        private static double getStrength(List<Card> centerCards, FiveCardHand bestHand, double strength,
                        boolean pairedBoard, boolean tripBoard, boolean quadBoard, Card highCard, Card otherCard, boolean isFlop, boolean isTurn, boolean isRiver) {
                //We have flopped a straight or better
                if(bestHand.getHandValue().getValue()>3){
                        strength =  100;
                }
                //We have flopped trips
                if(bestHand.getHandValue().equals(HandValue.THREE_OF_A_KIND)){
                        if(tripBoard){
                                //Ok, there's trips on the board so it's not the special
                                //We havent got a full house since we checked that already
                                //Our strength is now directly related to our high card...
                                int highCardValue = highCard.getValue().getNumericValue();
                                int secondCardValue = otherCard.getValue().getNumericValue();
                                //Values range from 2 to 14
                                //The highest card is more important than the other
                                //Max strength here is AK giving 61.5 strength with a good high card draw
                                strength = (highCardValue*3) + (secondCardValue*1.5);
                        }else if(pairedBoard){
                                //There's a pair on the board, so we hit three of a kind which is great, but not as good as getting it from pockets
                                int highCardValue = highCard.getValue().getNumericValue();
                                strength = 80 + highCardValue;
                        }else{
                                //We flopped a set, it's going to be call anything time
                                strength = 100;
                        }
                }
                //We have flopped 2 pair
                if(bestHand.getHandValue().equals(HandValue.TWO_PAIR)){
                        if(pairedBoard){
                                //The board is paired which accounts for one of our pairs, hitting the other is good though depending on it's value
                                //Check which card is the other pair;
                                int pairValue = getPairValue(highCard, otherCard, centerCards);
                                strength = 60 + pairValue*2;
                        }else{
                                //We've paired both our cards which is a very good flop
                                strength = 80;
                        }
                }
                if(bestHand.getHandValue().equals(HandValue.PAIR)){
                        //We've flopped a pair
                        if(pairedBoard){
                                //Ok the pair is on the board so we don't care, we should just check for our high card. With a base of 20 since the pair helps a little
                                strength = getHighCardStrength(highCard, otherCard, centerCards, 10);
                        }else{
                                int pairValue = getPairValue(highCard, otherCard, centerCards);
                                strength = 40 + pairValue*2;
                        }
                }
                if(bestHand.getHandValue().equals(HandValue.HIGH_CARD)){
                        strength = getHighCardStrength(highCard, otherCard, centerCards, 0);
                }
                if(hasFlushDraw(highCard, otherCard, centerCards)){
                        //We have a flush draw
                        if(strength<60){
                                strength = 60 + highCard.getValue().getNumericValue();
                        }
                }
                //TODO Straight draw
                return strength;
        }





        public static boolean hasFlushDraw(Card highCard, Card otherCard, List<Card> centerCards){
                if(centerCards.size()>4){
                        return false;
                }
                Map<Suit, List<Card>> suitToInt = new HashMap<Suit, List<Card>>();
                List<Card> cards = new ArrayList<Card>();
                cards.addAll(centerCards);
                cards.add(otherCard);
                cards.add(highCard);
                for (Card card : cards) {
                        if(suitToInt.containsKey(card.getSuit())){
                                List<Card> suitedCards = suitToInt.get(card.getSuit());
                                suitedCards.add(card);
                                suitToInt.put(card.getSuit(), suitedCards);
                        }else{
                                suitToInt.put(card.getSuit(),   new ArrayList<Card>(Arrays.asList(card)));
                        }
                }

                for(Suit suit : suitToInt.keySet()){
                        if(suitToInt.get(suit).size()>=4){
                                return true;
                        }
                }
                return false;
        }

        private static double getHighCardStrength(Card highCard, Card otherCard, List<Card> centerCards, double base){
                double strength = 0;
                boolean highOver = true;
                boolean lowOver = true;
                for (Card card : centerCards) {
                        if(card.getValue().getNumericValue()>highCard.getValue().getNumericValue()){
                                highOver = false;
                        }
                        if(card.getValue().getNumericValue()>otherCard.getValue().getNumericValue()){
                                lowOver = false;
                        }
                }
                if(highOver&&lowOver){
                        //we have 2 over cards
                        strength = base + highCard.getValue().getNumericValue() + otherCard.getValue().getNumericValue();
                }else if(highOver || lowOver){
                        //We have 1 over card
                        strength = base + highCard.getValue().getNumericValue();
                }else{
                        strength = highCard.getValue().getNumericValue();
                }
                return strength;
        }

        private static int getPairValue(Card highCard, Card otherCard, List<Card> centerCards){
                int pairValue = 0;
                for (Card card : centerCards) {
                        if(card.getValue().equals(highCard.getValue())){
                                pairValue = highCard.getValue().getNumericValue();
                                break;
                        }else if(card.getValue().equals(otherCard.getValue())){
                                pairValue = otherCard.getValue().getNumericValue();
                                break;
                        }
                }
                return pairValue;
        }

        /*
         * This method will get a likely opponent hand distribution based on if were to call a bet within a given % pot range
         */
        private static Distribution computeLikelyOpponentDistribution(double potBetPercentage, Hand myHand, List<Card> centerCards){
                List<Card> exclusions = new ArrayList<Card>();
                exclusions.addAll(Arrays.asList(myHand.getHand()));
                exclusions.addAll(centerCards);
                List<Hand> realisticHands = new ArrayList<Hand>();
                Distribution fullDistribution = DistributionBuilder.buildBasicDistribution(exclusions);
                Distribution realisticDistribution = new Distribution();

                for (Hand hand : fullDistribution.getHandToProbability().keySet()) {
                        double strength = calculatehandStrength(hand, centerCards);
                        //So in pot bet we need at least a 70 strength hand to call
                        if(strength>potBetPercentage*70){
                                realisticHands.add(hand);
                        }
                }

                Float prob = 1.0f;
                prob = prob / realisticHands.size();

                for (Hand hand : realisticHands) {
                        realisticDistribution.addHand(hand, prob);
                }

                return realisticDistribution;

        }

        private static FullWinPercentages shiftChancesAtFlop(FullWinPercentages original, double potBetPercentage, List<Card> flop){

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
