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

public class HandAnalyser {

        public static int compareHands(Hand hand1, Hand hand2, List<Card> centreCards){
                return 0;
        }

        public static HandValue getBestHand(Hand hand, List<Card> centreCards){
                List<Card> cards = new ArrayList<Card>();
                cards.addAll(Arrays.asList(hand.getCards()));
                cards.addAll(centreCards);
                if(centreCards.size()>=5){
                        if(hasStraightFlush(cards)){
                                return HandValue.STRAIGHT_FLUSH;
                        }else if(hasFourOfAKind(cards)){
                                return HandValue.FOUR_OF_A_KIND;
                        }else if(hasFullHouse(cards)){
                                return HandValue.FULL_HOUSE;
                        }else if(hasFlush(cards)!=null){
                                return HandValue.FLUSH;
                        }else if(hasStraight(cards, null)){
                                return HandValue.STRAIGHT;
                        }else if(hasThreeOfAKind(cards)){
                                return HandValue.THREE_OF_A_KIND;
                        }else if(hasTwoPair(cards)){
                                return HandValue.TWO_PAIR;
                        }else if(hasPair(cards)){
                                return HandValue.PAIR;
                        }else{
                                return HandValue.HIGH_CARD;
                        }
                }else{
                        //Can only be 2 cards
                        if(cards.get(0).getValue().equals(cards.get(1).getValue())){
                                return HandValue.PAIR;
                        }else{
                                return HandValue.HIGH_CARD;
                        }
                }
        }



        public static boolean hasPair(List<Card> cards) {
                Map<Value, Integer> ValueToInt = new HashMap<Value, Integer>();
                for (Card card : cards) {
                        if(ValueToInt.containsKey(card.getValue())){
                                ValueToInt.put(card.getValue(), ValueToInt.get(card.getValue()) + 1);
                        }else{
                                ValueToInt.put(card.getValue(), 1);
                        }
                }

                for(Value value : ValueToInt.keySet()){
                        if(ValueToInt.get(value)==2){
                                return true;
                        }
                }
                return false;
        }


        public static boolean hasTwoPair(List<Card> cards) {
                Map<Value, Integer> ValueToInt = new HashMap<Value, Integer>();
                for (Card card : cards) {
                        if(ValueToInt.containsKey(card.getValue())){
                                ValueToInt.put(card.getValue(), ValueToInt.get(card.getValue()) + 1);
                        }else{
                                ValueToInt.put(card.getValue(), 1);
                        }
                }

                int pairCount = 0;
                for(Value value : ValueToInt.keySet()){
                        if(ValueToInt.get(value)==2){
                                pairCount++;
                                if(pairCount==2){
                                        return true;
                                }
                        }
                }
                return false;
        }

        public static boolean hasThreeOfAKind(List<Card> cards) {
                Map<Value, Integer> ValueToInt = new HashMap<Value, Integer>();
                for (Card card : cards) {
                        if(ValueToInt.containsKey(card.getValue())){
                                ValueToInt.put(card.getValue(), ValueToInt.get(card.getValue()) + 1);
                        }else{
                                ValueToInt.put(card.getValue(), 1);
                        }
                }

                for(Value value : ValueToInt.keySet()){
                        if(ValueToInt.get(value)==3){
                                return true;
                        }
                }
                return false;
        }

        public static boolean hasFullHouse(List<Card> cards) {
                Map<Value, Integer> ValueToInt = new HashMap<Value, Integer>();
                for (Card card : cards) {
                        if(ValueToInt.containsKey(card.getValue())){
                                ValueToInt.put(card.getValue(), ValueToInt.get(card.getValue()) + 1);
                        }else{
                                ValueToInt.put(card.getValue(), 1);
                        }
                }

                boolean hasTrips = false;
                boolean hasPair = false;
                for(Value value : ValueToInt.keySet()){
                        if(ValueToInt.get(value)==3){
                                hasTrips = true;
                        }else if(ValueToInt.get(value)==2){
                                hasPair = true;
                        }
                }
                return hasTrips && hasPair;
        }

        public static boolean hasFourOfAKind(List<Card> cards) {
                Map<Value, Integer> ValueToInt = new HashMap<Value, Integer>();
                for (Card card : cards) {
                        if(ValueToInt.containsKey(card.getValue())){
                                ValueToInt.put(card.getValue(), ValueToInt.get(card.getValue()) + 1);
                        }else{
                                ValueToInt.put(card.getValue(), 1);
                        }
                }

                for(Value value : ValueToInt.keySet()){
                        if(ValueToInt.get(value)>=4){
                                return true;
                        }
                }
                return false;
        }

        public static boolean hasStraightFlush(List<Card> cards){
                Suit flushSuit = hasFlush(cards);
                if(flushSuit!=null){
                        if(hasStraight(cards, flushSuit)){
                                return true;
                        }else{
                                return false;
                        }
                }else{
                        return false;
                }
        }

        public static boolean hasStraight(List<Card> cards, Suit suit){
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
                List<Integer> values = new ArrayList<Integer>();
                for (Card card : toCheck) {
                        values.add(card.getValue().getNumericValue());
                }

                Collections.sort(values);
                if(has5RunningValues(values)){
                        return true;
                }else{
                        //Change 14's to 1's to handle Aces
                        List<Integer> swappedAces = new ArrayList<Integer>();
                        for (Integer integer : values) {
                                if(integer==14){
                                        swappedAces.add(1);
                                }else{
                                        swappedAces.add(integer);
                                }
                        }
                        Collections.sort(swappedAces);
                        return has5RunningValues(swappedAces);
                }
        }

        private static boolean has5RunningValues(List<Integer> values){
                int runningCount = 0;
                int prev = -1;
                for (Integer integer : values) {
                        if(runningCount==5){
                                return true;
                        }
                        if(prev==-1){
                                prev=integer;
                                runningCount++;
                        }else{
                                if(integer == prev){
                                        //do nothing
                                }else if(integer == prev+1){
                                        runningCount++;
                                        prev = integer;
                                }else{
                                        prev = integer;
                                        runningCount=1;
                                }
                        }
                }
                return runningCount==5;
        }

        public static Suit hasFlush(List<Card> cards){
                Map<Suit, Integer> suitToInt = new HashMap<Suit, Integer>();
                for (Card card : cards) {
                        if(suitToInt.containsKey(card.getSuit())){
                                suitToInt.put(card.getSuit(), suitToInt.get(card.getSuit()) + 1);
                        }else{
                                suitToInt.put(card.getSuit(), 1);
                        }
                }

                for(Suit suit : suitToInt.keySet()){
                        if(suitToInt.get(suit)>=5){
                                return suit;
                        }
                }
                return null;
        }




}
//J+
