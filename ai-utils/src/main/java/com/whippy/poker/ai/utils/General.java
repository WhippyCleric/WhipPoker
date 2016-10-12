//J-
package com.whippy.poker.ai.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Deck;
import com.whippy.poker.common.beans.Hand;
import com.whippy.poker.common.beans.Suit;
import com.whippy.poker.common.beans.Value;

public class General {

	private static List<List<Card>> every5CardCombination = null;

	public static List<Card> getCards(List<Card> exclusions){
		List<Card> cards = new ArrayList<Card>();
		for(Suit suit : Suit.values()){
			for(Value value : Value.values()){
				Card toAdd = new Card(suit, value);
				if(!exclusions.contains(toAdd)){
					cards.add(toAdd);
				}
			}
		}
		return cards;
	}

	public static List<Hand> generateEveryHand(List<Card> exclusions){
		List<Hand> hands = new ArrayList<Hand>();
		List<Card> cards = new ArrayList<Card>();
		List<Integer> indexSets = new ArrayList<Integer>();

		Deck deck = new Deck();
		for(int i=0;i<52;i++){
			Card card = deck.getTopCard();
			if(!exclusions.contains(card)){
				cards.add(card);
				indexSets.add(indexSets.size());
			}
		}

		List<Set<Integer>> allSets = getSubsets(indexSets, 2);
		for (Set<Integer> indexSet : allSets) {
			Iterator<Integer> it = indexSet.iterator();
			hands.add(new Hand(cards.get(it.next()), cards.get(it.next())));
		}

		return hands;
	}

	public static List<List<Card>> getEvery5CardCombination(){
		if(every5CardCombination==null){
			List<Card> cards = General.getCards(new ArrayList<Card>());
			every5CardCombination = new ArrayList<List<Card>>();
			List<Integer> indexes = new ArrayList<Integer>();
			for(int i=0; i< cards.size(); i++){
				indexes.add(i);
			}
			List<Set<Integer>> allSets = getSubsets(indexes, 5);
			for (Set<Integer> setOfIndexes : allSets) {
				List<Card> cardSet = new ArrayList<Card>();
				for (Integer index : setOfIndexes) {
					cardSet.add(cards.get(index));
				}
				every5CardCombination.add(cardSet);
			}
		}
		return every5CardCombination;
	}

	public static List<List<Card>> getEveryPostFlopCardCombination(List<Card> floppedCards, List<Card> cards){
		List<List<Card>> everyPostFlopCombination = new ArrayList<List<Card>>();
		List<Integer> indexes = new ArrayList<Integer>();
		for(int i=0; i< cards.size(); i++){
			indexes.add(i);
		}
		if(5-floppedCards.size()==0){
			List<Card> cardSet = new ArrayList<Card>();
			cardSet.addAll(floppedCards);
			everyPostFlopCombination.add(cardSet);
		}else{
			List<Set<Integer>> allSets = getSubsets(indexes, 5-floppedCards.size());
			for (Set<Integer> setOfIndexes : allSets) {
				List<Card> cardSet = new ArrayList<Card>();
				cardSet.addAll(floppedCards);
				for (Integer index : setOfIndexes) {
					cardSet.add(cards.get(index));
				}
				everyPostFlopCombination.add(cardSet);
			}
		}
		return everyPostFlopCombination;
	}

	private static void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current,List<Set<Integer>> solution) {
		//successful stop clause
		if (current.size() == k) {
			solution.add(new HashSet<Integer>(current));
			return;
		}
		//unseccessful stop clause
		if (idx == superSet.size()) return;

		Integer x = superSet.get(idx);

		current.add(x);
		//"guess" x is in the subset
		getSubsets(superSet, k, idx+1, current, solution);

		current.remove(x);
		//"guess" x is not in the subset
		getSubsets(superSet, k, idx+1, current, solution);
	}

	public static List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
		List<Set<Integer>> res = new ArrayList<>();
		getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
		return res;
	}

}
