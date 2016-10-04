//J-
package com.whippy.poker.sever.analyser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.HandValue;
import com.whippy.poker.common.beans.Suit;
import com.whippy.poker.common.beans.Value;
import com.whippy.poker.server.beans.FiveCardHand;

public class HandAnalyser {

        public static int compareHands(Hand hand1, Hand hand2, List<Card> centreCards){
                FiveCardHand fiveCardHand1 = getBestHand(hand1, centreCards);
                FiveCardHand fiveCardHand2 = getBestHand(hand2, centreCards);
                if(fiveCardHand1.getHandValue().getValue()>fiveCardHand2.getHandValue().getValue()){
                        return -1;
                }else if(fiveCardHand2.getHandValue().getValue()>fiveCardHand1.getHandValue().getValue()){
                        return 1;
                }else{
                        if(fiveCardHand1.getHandValue().equals(HandValue.HIGH_CARD)){
                                return compareHighCard(fiveCardHand1.getCards(), fiveCardHand2.getCards());
                        }
                        if(fiveCardHand1.getHandValue().equals(HandValue.PAIR)){
                                return comparePair(fiveCardHand1.getCards(), fiveCardHand2.getCards());
                        }
                        //TODO handle run offs
                        return 0;
                }
        }

        public static int compareHighCard(List<Card> cards1, List<Card> cards2){
                Collections.sort(cards1, Collections.reverseOrder());
                Collections.sort(cards2, Collections.reverseOrder());
                for (int i=0 ; i<cards1.size();i++) {
                        Card card1 = cards1.get(i);
                        Card card2 = cards2.get(i);
                        if(card1.getValue().getNumericValue()<card2.getValue().getNumericValue()){
                                return 1;
                        }else if(card1.getValue().getNumericValue()>card2.getValue().getNumericValue()){
                                return -1;
                        }
                }
                return 0;
        }
        public static int comparePair(List<Card> cards1, List<Card> cards2){
                int pair1 = 0;
                int pair2 = 0;
                Map<Value, List<Card>> valueToInt1 = createValueToCardMap(cards1);
                Map<Value, List<Card>> valueToInt2 = createValueToCardMap(cards2);

                List<Card> filter1 = new ArrayList<Card>();
                List<Card> filter2 = new ArrayList<Card>();

                for(Value value : valueToInt1.keySet()){
                        if(valueToInt1.get(value).size()==2){
                                pair1 = value.getNumericValue();
                        }else{
                                filter1.addAll(valueToInt1.get(value));
                        }
                }

                for(Value value : valueToInt2.keySet()){
                        if(valueToInt2.get(value).size()==2){
                                pair2 = value.getNumericValue();
                        }else{
                                filter2.addAll(valueToInt2.get(value));
                        }
                }

                if(pair1>pair2){
                        return -1;
                }else if(pair2>pair1){
                        return 1;
                }
                //Same pairs

                Collections.sort(filter1, Collections.reverseOrder());
                Collections.sort(filter2, Collections.reverseOrder());
                for (int i=0 ; i<filter1.size();i++) {
                        Card card1 = filter1.get(i);
                        Card card2 = filter2.get(i);
                        if(card1.getValue().getNumericValue()<card2.getValue().getNumericValue()){
                                return 1;
                        }else if(card1.getValue().getNumericValue()>card2.getValue().getNumericValue()){
                                return -1;
                        }
                }
                return 0;
        }

        private static Map<Value, List<Card>> createValueToCardMap(List<Card> cards) {
                Map<Value, List<Card>> valueToInt = new HashMap<Value, List<Card>>();
                for (Card card : cards) {
                        if(valueToInt.containsKey(card.getValue())){
                                List<Card> toAdd = new ArrayList<Card>();
                                toAdd.addAll(valueToInt.get(card.getValue()));
                                toAdd.add(card);
                                valueToInt.put(card.getValue(), toAdd);
                        }else{
                                valueToInt.put(card.getValue(), new ArrayList<Card>(Arrays.asList(card)));
                        }
                }
                return valueToInt;
        }

        public static FiveCardHand getBestHand(Hand hand, List<Card> centreCards){
                List<Card> cards = new ArrayList<Card>();
                cards.addAll(Arrays.asList(hand.getCards()));
                cards.addAll(centreCards);
                if(centreCards.size()>=5){
                        FiveCardHand fiveCardHand = hasStraightFlush(cards);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        fiveCardHand = hasFourOfAKind(cards);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        fiveCardHand = hasFullHouse(cards);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        fiveCardHand = hasFlush(cards);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        fiveCardHand = hasStraight(cards, null);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        fiveCardHand = hasThreeOfAKind(cards);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        fiveCardHand = hasTwoPair(cards);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        fiveCardHand = hasPair(cards);
                        if(fiveCardHand!=null){
                                return fiveCardHand;
                        }
                        List<Card> finalHand = new ArrayList<Card>();
                        Card fifthCard = findHighestCard(cards, finalHand);
                        finalHand.add(fifthCard);
                        Card fourthCard = findHighestCard(cards, finalHand);
                        finalHand.add(fourthCard);
                        Card thirdCard = findHighestCard(cards, finalHand);
                        finalHand.add(thirdCard);
                        Card secondCard = findHighestCard(cards, finalHand);
                        finalHand.add(secondCard);
                        Card firstCard = findHighestCard(cards, finalHand);
                        finalHand.add(firstCard);
                        return new FiveCardHand(finalHand, HandValue.HIGH_CARD);

                }else{
                        if(cards.get(0).getValue().equals(cards.get(1).getValue())){
                                return new FiveCardHand(new ArrayList<Card>(Arrays.asList(cards.get(0), cards.get(1))), HandValue.PAIR);
                        }else{
                                return new FiveCardHand(new ArrayList<Card>(Arrays.asList(cards.get(0), cards.get(1))), HandValue.HIGH_CARD);
                        }
                }
        }



        public static FiveCardHand hasPair(List<Card> cards) {
                Map<Value, List<Card>> valueToInt = createValueToCardMap(cards);

                List<Card> finalHand = new ArrayList<Card>();
                for(Value value : valueToInt.keySet()){
                        if(valueToInt.get(value).size()==2){
                                finalHand.addAll(valueToInt.get(value));
                                Card fifthCard = findHighestCard(cards, finalHand);
                                finalHand.add(fifthCard);
                                Card fourthCard = findHighestCard(cards, finalHand);
                                finalHand.add(fourthCard);
                                Card thirdCard = findHighestCard(cards, finalHand);
                                finalHand.add(thirdCard);
                                return new FiveCardHand(finalHand, HandValue.PAIR);
                        }
                }
                return null;
        }


        public static FiveCardHand hasTwoPair(List<Card> cards) {
                Map<Value, List<Card>> valueToInt = createValueToCardMap(cards);

                int pairCount = 0;
                int lowestPair = 0;
                List<Card> finalHand = new ArrayList<Card>();
                for(Value value : valueToInt.keySet()){
                        if(valueToInt.get(value).size()==2){
                                pairCount++;
                                finalHand.addAll(valueToInt.get(value));
                                if(lowestPair==0){
                                        lowestPair = value.getNumericValue();
                                }else{
                                        if(lowestPair>value.getNumericValue()){
                                                lowestPair = value.getNumericValue();
                                        }
                                }
                        }
                }
                if(pairCount==2){
                        Card fifthCard = findHighestCard(cards, finalHand);
                        finalHand.add(fifthCard);
                        return new FiveCardHand(finalHand, HandValue.TWO_PAIR);
                }else if(pairCount==3){
                        List<Card> removed = new ArrayList<Card>();
                        for (Card card : finalHand) {
                                if(card.getValue().getNumericValue()!=lowestPair){
                                        removed.add(card);
                                }
                        }
                        Card fifthCard = findHighestCard(cards, removed);
                        removed.add(fifthCard);
                        return new FiveCardHand(removed, HandValue.TWO_PAIR);
                }
                return null;
        }

        public static FiveCardHand hasThreeOfAKind(List<Card> cards) {
                Map<Value, List<Card>> valueToInt = new HashMap<Value, List<Card>>();
                for (Card card : cards) {
                        if(valueToInt.containsKey(card.getValue())){
                                List<Card> toAdd =  valueToInt.get(card.getValue());
                                toAdd.add(card);
                                valueToInt.put(card.getValue(), toAdd);
                        }else{
                                valueToInt.put(card.getValue(), new ArrayList<Card>(Arrays.asList(card)));
                        }
                }

                for(Value value : valueToInt.keySet()){
                        if(valueToInt.get(value).size()==3){
                                List<Card> toReturn = valueToInt.get(value);
                                Card fourth = findHighestCard(cards, valueToInt.get(value));
                                toReturn.add(fourth);
                                Card fifth = findHighestCard(cards, toReturn);
                                toReturn.add(fifth);
                                return new FiveCardHand(toReturn, HandValue.THREE_OF_A_KIND);
                        }
                }
                return null;
        }

        public static Card findHighestCard(List<Card> cards, List<Card> exclusions){
                Card highest = null;
                for (Card card : cards) {
                        if(!exclusions.contains(card)){
                                if(highest==null){
                                        highest=card;
                                }else{
                                        if(highest.getValue().getNumericValue()<card.getValue().getNumericValue()){
                                                highest = card;
                                        }
                                }
                        }
                }
                return highest;
        }

        public static FiveCardHand hasFullHouse(List<Card> cards) {
                Map<Value, List<Card>> valueToInt = createValueToCardMap(cards);

                List<Card> hand = new ArrayList<Card>();
                int pair1=0;
                int pair2=0;
                for(Value value : valueToInt.keySet()){
                        if(valueToInt.get(value).size()==3){
                                hand.addAll(valueToInt.get(value));
                        }else if(valueToInt.get(value).size()==2){
                                if(pair1==0){
                                        pair1 = valueToInt.get(value).get(0).getValue().getNumericValue();
                                }else{
                                        pair2 = valueToInt.get(value).get(0).getValue().getNumericValue();
                                }
                                hand.addAll(valueToInt.get(value));
                        }
                }
                if(hand.size()==5){
                        return new FiveCardHand(hand, HandValue.FULL_HOUSE);
                }else if(hand.size()==7){
                        int toRemove = 0;
                        if(pair1<pair2){
                                toRemove = pair1;
                        }else{
                                toRemove = pair2;
                        }
                        List<Card> finalHand = new ArrayList<Card>();
                        for (Card card : hand) {
                                if(card.getValue().getNumericValue()!=toRemove){
                                        finalHand.add(card);
                                }
                        }
                        return new FiveCardHand(finalHand, HandValue.FULL_HOUSE);
                }
                return null;
        }

        public static FiveCardHand hasFourOfAKind(List<Card> cards) {
                Map<Value, List<Card>> valueToInt = new HashMap<Value, List<Card>>();
                for (Card card : cards) {
                        if(valueToInt.containsKey(card.getValue())){
                                List<Card> toAdd =  valueToInt.get(card.getValue());
                                toAdd.add(card);
                                valueToInt.put(card.getValue(), toAdd);
                        }else{
                                valueToInt.put(card.getValue(), new ArrayList<Card>(Arrays.asList(card)));
                        }
                }

                for(Value value : valueToInt.keySet()){
                        if(valueToInt.get(value).size()==4){
                                Card fifthCard = findHighestCard(cards, valueToInt.get(value));
                                List<Card> toReturn = valueToInt.get(value);
                                toReturn.add(fifthCard);
                                return new FiveCardHand(toReturn, HandValue.FOUR_OF_A_KIND);
                        }
                }
                return null;
        }

        public static FiveCardHand hasStraightFlush(List<Card> cards){
                FiveCardHand hand = hasFlush(cards);

                if(hand!=null){
                        hand = hasStraight(cards, hand.getCards().get(0).getSuit());
                        if(hand!=null){
                                return new FiveCardHand(hand.getCards(), HandValue.STRAIGHT_FLUSH);
                        }else{
                                return null;
                        }
                }else{
                        return null;
                }

        }

        public static FiveCardHand hasStraight(List<Card> cards, Suit suit){
                List<Card> toCheck = new ArrayList<Card>();
                if(suit==null){
                        toCheck.addAll(cards);
                }else{
                        for (Card card : cards) {
                                if(card.getSuit().equals(suit)){
                                        toCheck.add(card);
                                }
                        }
                }
                Collections.sort(toCheck, Collections.reverseOrder());
                FiveCardHand hand = has5RunningValues(toCheck);
                if(hand!=null){
                        return hand;
                }else{
                        hand = has5RunningValuesLowAce(toCheck);
                        if(hand!=null){
                                return hand;
                        }else{
                                return null;
                        }
                }
        }

        private static FiveCardHand has5RunningValues(List<Card> cards){
                int runningCount = 0;
                int prev = -1;
                List<Card> resultCards = new ArrayList<Card>();
                for (Card card : cards) {
                        Integer integer = card.getValue().getNumericValue();
                        if(runningCount==5){
                                return new FiveCardHand(resultCards, HandValue.STRAIGHT);
                        }
                        if(prev==-1){
                                prev=integer;
                                runningCount++;
                                resultCards.add(card);
                        }else{
                                if(integer == prev){
                                        //do nothing
                                }else if(integer == prev-1){
                                        runningCount++;
                                        prev = integer;
                                        resultCards.add(card);
                                }else{
                                        prev = integer;
                                        runningCount=1;
                                        resultCards = new ArrayList<Card>();
                                        resultCards.add(card);
                                }
                        }
                }
                if(runningCount==5){
                        return new FiveCardHand(resultCards, HandValue.STRAIGHT);
                }
                return null;
        }

        private static FiveCardHand has5RunningValuesLowAce(List<Card> cards){
                int runningCount = 0;
                int prev = -1;
                List<Card> reOrdered = new ArrayList<Card>();
                List<Card> aces = new ArrayList<Card>();
                for (Card card : cards) {
                        if(card.getValue().equals(Value.ACE)){
                                aces.add(card);
                        }else{
                                reOrdered.add(card);
                        }
                }
                reOrdered.addAll(aces);

                List<Card> resultCards = new ArrayList<Card>();
                for (Card card : reOrdered) {
                        Integer integer = card.getValue().getNumericValue();
                        if(integer==14){
                                integer = 1;
                        }
                        if(runningCount==5){
                                return new FiveCardHand(resultCards, HandValue.STRAIGHT);
                        }
                        if(prev==-1){
                                prev=integer;
                                runningCount++;
                                resultCards.add(card);
                        }else{
                                if(integer == prev){
                                        //do nothing
                                }else if(integer == prev-1){
                                        runningCount++;
                                        prev = integer;
                                        resultCards.add(card);
                                }else{
                                        prev = integer;
                                        runningCount=1;
                                        resultCards = new ArrayList<Card>();
                                        resultCards.add(card);
                                }
                        }
                }
                if(runningCount==5){
                        return new FiveCardHand(resultCards, HandValue.STRAIGHT);
                }
                return null;
        }

        public static FiveCardHand hasFlush(List<Card> cards){
                Map<Suit, List<Card>> suitToInt = new HashMap<Suit, List<Card>>();
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
                        if(suitToInt.get(suit).size()>=5){
                                List<Card> toReturn = suitToInt.get(suit);
                                Collections.sort(toReturn, Collections.reverseOrder());
                                return new FiveCardHand(toReturn.subList(0, 5), HandValue.FLUSH);
                        }
                }
                return null;
        }




}
//J+
