//J-
package com.whippy.poker.ai.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whippy.poker.ai.beans.BetToDistribution;
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

        private static final int SMALL_BLIND = 5;
        private static double tightness = 70;
        private static final String WHIP_BOT = "WhipBot";
        private static WinningCalculator winningCalculator;
        private static Distribution currentOpponentDistribution = new Distribution();

        public static void main(String[] args) throws Exception {
                winningCalculator = new WinningCalculator();
                WhipPokerClient client = new WhipPokerClient("pmdunn-new", 8080);
                client.register(WHIP_BOT);
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



        private static boolean checkInstead(Hand myHand, List<Card> centerCards){
                FiveCardHand myBestHand = HandAnalyser.getBestHand(myHand, centerCards);
                if(centerCards.size()<5){
                        if(myBestHand.getHandValue().getValue()>HandValue.STRAIGHT.getValue()){
                                //We have a made hand with no concern of draws
                                if(Math.random()<0.5){
                                        return true;
                                }else{
                                        return false;
                                }
                        }else if(HandAnalyser.hasSuitMatchings(centerCards, 3)){
                                return false;
                        }else if(HandAnalyser.hasSuitMatchings(centerCards, 2) && centerCards.size()==3){
                                //We have some concern over draws since the flop has 2 of the same suit
                                if(Math.random()<0.15){
                                        return true;
                                }else{
                                        return false;
                                }
                        }else if(HandAnalyser.hasSuitMatchings(centerCards, 2) && centerCards.size()==4){
                                //We have a minor concern over draws
                                if(Math.random()<0.35){
                                        return true;
                                }else{
                                        return false;
                                }
                        }else{
                                if(Math.random()<0.5){
                                        return true;
                                }else{
                                        return false;
                                }
                        }
                }else{
                        //dont check bet on the river
                        return false;
                }
        }


        private static void processState(ClientState state, ClientSeat mySeat, WhipPokerClient client) throws Exception{
                //Maybe we have to do something...
                Hand myHand = state.getHand();
                System.out.println(myHand);
                FullWinPercentages myChances = winningCalculator.getAllHandsFullWin().get(myHand);
                //Time to do something

                if(state.getTable().getState().equals(TableState.PRE_FLOP)){
                        //Reset the opponent hand distribution
                        currentOpponentDistribution = DistributionBuilder.buildBasicDistribution(Arrays.asList(myHand.getHand()));
                        //Randomise how tight we will play
                        tightness = 40 + (Math.random()*40);
                        System.out.println("Tightness: " + tightness);
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
                                BetToDistribution optimumBetDist = getOptimumEvBet(state, mySeat, myHand, state.getTable().getCurrentCards());
                                if(optimumBetDist!=null && optimumBetDist.getBet()>0){
                                        currentOpponentDistribution = optimumBetDist.getDistribution();
                                        if(checkInstead(myHand, state.getTable().getCurrentCards())){
                                                client.call(WHIP_BOT);
                                        }else{
                                                if(optimumBetDist.getBet()>mySeat.getPlayer().getChipCount()){
                                                        client.bet(WHIP_BOT, mySeat.getPlayer().getChipCount());
                                                }else{
                                                        client.bet(WHIP_BOT, (int)(optimumBetDist.getBet()));
                                                }
                                        }
                                }else{
                                        client.call(WHIP_BOT);
                                }
                        }else if(!state.getTable().getState().equals(TableState.PRE_FLOP)){
                                // Do we want to call the bet?
                                double pendingBet = state.getTable().getPendingBet();
                                double potPercentage = (state.getTable().getPendingBet()-state.getCurrentBet()) / trueCurrentPot;
                                Distribution  opponentHands = computeLikelyOpponentDistribution(potPercentage, myHand, state.getTable().getCurrentCards());
                                FullWinPercentages myChancesPostFlop = PostFlopWinPercentageBuilder.calculateWinPercentages(myHand, state.getTable().getCurrentCards(), opponentHands);
                                Float winChance = myChancesPostFlop.getChances().getWinChance();
                                Float trueCallEv = new Float((winChance * (trueCurrentPot + pendingBet - state.getCurrentBet())) - (pendingBet - state.getCurrentBet()));
                                System.out.println(callEv + " for calling");
                                if(trueCallEv>0){
                                        currentOpponentDistribution = opponentHands;
                                        System.out.println(myHand + " chance of win " + myChancesPostFlop.getChances().getWinChance() + " with ev of " + trueCallEv + " call");
                                        client.call(WHIP_BOT);
                                }else{
                                        System.out.println(myHand + " chance of win " + myChancesPostFlop.getChances().getWinChance() + " with ev of " + trueCallEv + " fold");
                                        client.fold(WHIP_BOT);
                                }
                        }else{
                                //It's before the flop. We call or fold simply based on our ev.
                                //TODO this is where we need to improve to handle calling large bets, and raising it ourselves, and infering their hand based on bet
                                Float preFlopWinChance = myChances.getChances().getWinChance();
                                if(callCost == SMALL_BLIND){
                                        //Call or raise
                                        //We will raise when we have at least a 50% chance of winning
                                        if(preFlopWinChance<0.5){
                                                client.call(WHIP_BOT);
                                        }else{
                                                //for every 10% over we are that's the raise
                                                double bet = ((preFlopWinChance*100) - 50)/10;
                                                bet = (Math.round(bet/10) * 10) + 10;
                                                bet = SMALL_BLIND + SMALL_BLIND*2 + bet;
                                                if(bet>mySeat.getPlayer().getChipCount()){
                                                        client.bet(WHIP_BOT, mySeat.getPlayer().getChipCount());
                                                }else{
                                                        client.bet(WHIP_BOT, (int) bet);
                                                }
                                        }
                                }else if(callCost == 0){
                                        //Check or raise
                                        if(preFlopWinChance<0.5){
                                                client.call(WHIP_BOT);
                                        }else{
                                                //for every 10% over we are that's the raise
                                                double bet = ((preFlopWinChance*100) - 50)/10;
                                                bet = (Math.round(bet/10) * 10) + 10;
                                                bet = SMALL_BLIND*2 + bet;
                                                if(bet>mySeat.getPlayer().getChipCount()){
                                                        client.bet(WHIP_BOT, mySeat.getPlayer().getChipCount());
                                                }else{
                                                        client.bet(WHIP_BOT, (int) bet);
                                                }
                                        }
                                }else{
                                        //Call, raise or fold
                                        if(callEv>0){
                                                System.out.println(myHand + " preflop chance of win " + myChances.getChances().getWinChance() + " with ev of " + callEv + " call");
                                                client.call(WHIP_BOT);
                                        }else{
                                                System.out.println(myHand + " preflop chance of win " + myChances.getChances().getWinChance() + " with ev of " + callEv + " fold");
                                                client.fold(WHIP_BOT);
                                        }
                                }
                        }


                }else{
                        System.err.println("not yet got data for this hand " + myHand);
                        client.fold(WHIP_BOT);
                }

        }

        private static Distribution cleanDistributionbasedOnPrevious(Distribution toClean){
                Distribution cleaned = new Distribution();
                List<Hand> handsToAdd = new ArrayList<Hand>();
                for (Hand hand : toClean.getHandToProbability().keySet()) {
                        if(currentOpponentDistribution.getHandToProbability().keySet().contains(hand)){
                                handsToAdd.add(hand);
                        }
                }
                Float prob = 1.0f;
                prob = prob / handsToAdd.size();
                for (Hand hand : handsToAdd) {
                        cleaned.addHand(hand, prob);
                }
                return cleaned;
        }

        private static BetToDistribution getOptimumEvBet(ClientState state, ClientSeat mySeat, Hand myHand, List<Card> flop) throws Exception{
                double myChips = mySeat.getPlayer().getChipCount();
                double optimumBet = 0;
                Float prevBest = 0f;
                double trueCurrentPot = getTrueCurrentPot(state);
                BetToDistribution toReturn = null;


                Distribution  opponentHandsAtTenthPot = computeLikelyOpponentDistribution(0.1, myHand, flop);
                FullWinPercentages myChancesAtTenthPot = PostFlopWinPercentageBuilder.calculateWinPercentages(myHand, state.getTable().getCurrentCards(), opponentHandsAtTenthPot);

                for(int i =0; i< myChips && i<=trueCurrentPot; i=i+(int)(trueCurrentPot/10)){
                        if(i!=0 && (i < 10 || i < (trueCurrentPot/10))){
                                //Do nothing since this would be weird sized bet
                        }else{
                                double betSize = i;
                                double postBetPot = trueCurrentPot + betSize;
                                double postBetCallPot = postBetPot + betSize;
                                double potPercentage = i / trueCurrentPot;

                                /*
                                 * If opposing player call a pot sized bet it modified the % chance of hands he is holding
                                 * The same is true for smaller bets with a smaller shift in those %'s
                                 * Therefore when calculating my Ev of a bet P(W) should not be based on an even opponent hand distribution
                                 * It opponent hand distribution must be shifted as though they have called a bet of this size
                                 * This is only the case once the pot is larger than 3 BB, prior to this it is too small to matter
                                 */
                                Float winChance = myChancesAtTenthPot.getChances().getWinChance();
                                if(i>trueCurrentPot/10 && trueCurrentPot>30){
                                        Distribution  opponentHands = computeLikelyOpponentDistribution(potPercentage, myHand, flop);
                                        FullWinPercentages myChances = PostFlopWinPercentageBuilder.calculateWinPercentages(myHand, state.getTable().getCurrentCards(), opponentHands);
                                        winChance = myChances.getChances().getWinChance();
                                        Float betEv = new Float((winChance * postBetCallPot) - betSize);
                                        System.out.println(betEv + " for bet size " + betSize);
                                                        if(betEv>prevBest){
                                                                prevBest = betEv;
                                                                optimumBet = betSize;
                                                                toReturn = new BetToDistribution(optimumBet, opponentHands);
                                                        }
                                }else{
                                        Float betEv = new Float((winChance * postBetCallPot) - betSize);
                                        System.out.println(betEv + " for bet size " + betSize);
                                        if(betEv>prevBest){
                                                prevBest = betEv;
                                                optimumBet = betSize;
                                                toReturn = new BetToDistribution(optimumBet, opponentHandsAtTenthPot);
                                        }
                                }
                        }
                }

                return toReturn;
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

                if(centerCards.size()==3){
                        //First case is post flop
                        strength = getStrength(centerCards, bestHand, strength, pairedBoard, tripBoard, quadBoard, highCard, otherCard, true, false, false);
                }else if(centerCards.size()==4){
                        //We are at the turn
                        strength = getStrength(centerCards, bestHand, strength, pairedBoard, tripBoard, quadBoard, highCard, otherCard, false, true, false);
                }else{
                        //Its the river
                        strength = getStrength(centerCards, bestHand, strength, pairedBoard, tripBoard, quadBoard, highCard, otherCard, false, false, true);
                }

                return strength;
        }


        private static double getStrength(List<Card> centerCards, FiveCardHand bestHand, double strength,
                        boolean pairedBoard, boolean tripBoard, boolean quadBoard, Card highCard, Card otherCard, boolean isFlop, boolean isTurn, boolean isRiver) {
                //TODO change strength based on number of shown cards
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
                                //The exception to the above is on the turn. Since although there is a set out
                                //If we have a flush draw we still have some additional strength
                                if(isTurn){
                                        if(hasFlushDraw(highCard, otherCard, centerCards)){
                                                strength += 20;
                                        }
                                }else if(isRiver){
                                        //If we're on the river then we've missed all our draws and are playing the board with our high card, strength is not as good
                                        strength = (highCardValue*2) + (secondCardValue*1);
                                }
                        }else if(pairedBoard){
                                //There's a pair on the board, so we hit three of a kind which is great, but not as good as getting it from pockets
                                int highCardValue = highCard.getValue().getNumericValue();
                                strength = 80 + highCardValue;
                        }else{
                                //We have a set, it's going to be call anything time
                                strength = 100;
                        }
                }
                //We have flopped 2 pair
                if(bestHand.getHandValue().equals(HandValue.TWO_PAIR)){
                        if(pairedBoard){
                                //The board is paired which accounts for one of our pairs, hitting the other is good though depending on it's value
                                int pairValue = getPairValue(highCard, otherCard, centerCards);
                                if(isFlop){
                                        strength = 50 + pairValue*2;
                                }else if(isTurn){
                                        strength = 40 + pairValue*2;
                                }else{
                                        strength = 30 + pairValue*2;
                                }
                        }else{
                                //We've paired both our cards which is good
                                if(isFlop){
                                        strength = 80;
                                }else if(isTurn){
                                        strength = 75;
                                }else{
                                        strength = 70;
                                }
                        }
                }
                if(bestHand.getHandValue().equals(HandValue.PAIR)){
                        //We've flopped a pair
                        if(pairedBoard){
                                //Ok the pair is on the board so we don't care, we should just check for our high card. With a base of 20 since the pair helps a little
                                strength = getHighCardStrength(highCard, otherCard, centerCards, 10);
                        }else{
                                int pairValue = getPairValue(highCard, otherCard, centerCards);
                                if(isFlop){
                                        strength = 40 + pairValue*2;
                                }else if(isTurn){
                                        strength = 30 + pairValue*2;
                                }else{
                                        strength = 20 + pairValue*2;
                                }
                        }
                }
                if(bestHand.getHandValue().equals(HandValue.HIGH_CARD)){
                        strength = getHighCardStrength(highCard, otherCard, centerCards, 0);
                }
                if(hasFlushDraw(highCard, otherCard, centerCards)){
                        //We have a flush draw
                        if(isFlop){
                                if(strength<60){
                                        strength = 60 + highCard.getValue().getNumericValue();
                                }
                        }else if(isTurn){
                                if(strength<50){
                                        strength = 50 + highCard.getValue().getNumericValue();
                                }
                        }
                }

                //We have not hit a flush or better
                if(bestHand.getHandValue().getValue()<5) {
                        //It's the river
                        if(isRiver){
                                //Lets check if there's 4 of one suit on the board
                                if(hasFlushDraw(centerCards.get(0), centerCards.get(1), centerCards.subList(2, centerCards.size()))){
                                        //Ok given there's 4 of one suit on the board and we have not hit a flush we must modify our strength accordingly
                                        strength = strength /2;
                                }
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
                        //So in pot bet we need at least a tightness strength hand to call
                        if(strength>potBetPercentage*tightness){
                                realisticHands.add(hand);
                        }
                }

                Float prob = 1.0f;
                prob = prob / realisticHands.size();

                for (Hand hand : realisticHands) {
                        realisticDistribution.addHand(hand, prob);
                }

                return cleanDistributionbasedOnPrevious(realisticDistribution);

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
