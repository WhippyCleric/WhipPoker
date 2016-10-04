//J-
package com.whippy.poker.sever.analyser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.whippy.poker.common.beans.Card;
import com.whippy.poker.common.beans.Suit;
import com.whippy.poker.common.beans.Value;

public class HandAnalyserTest {

	@Test
	public void testStraightDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CLUBS, Value.NINE));
		cards.add(new Card(Suit.HEARTS, Value.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.SPADES, Value.JACK));
		cards.add(new Card(Suit.DIAMONDS, Value.EIGHT));
		cards.add(new Card(Suit.DIAMONDS, Value.TEN));
		assertTrue(HandAnalyser.hasStraight(cards, null));
	}

	@Test
	public void testNoStraightDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CLUBS, Value.NINE));
		cards.add(new Card(Suit.HEARTS, Value.KING));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.SPADES, Value.JACK));
		cards.add(new Card(Suit.DIAMONDS, Value.EIGHT));
		cards.add(new Card(Suit.DIAMONDS, Value.TEN));
		assertFalse(HandAnalyser.hasStraight(cards, null));
	}

	@Test
	public void testStraightLowAceDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CLUBS, Value.FIVE));
		cards.add(new Card(Suit.HEARTS, Value.THREE));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.SPADES, Value.JACK));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		assertTrue(HandAnalyser.hasStraight(cards, null));
	}

	@Test
	public void testFlushDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.NINE));
		cards.add(new Card(Suit.DIAMONDS, Value.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.SPADES, Value.JACK));
		cards.add(new Card(Suit.DIAMONDS, Value.KING));
		cards.add(new Card(Suit.DIAMONDS, Value.TEN));
		assertTrue(HandAnalyser.hasFlush(cards).equals(Suit.DIAMONDS));
	}

	@Test
	public void testNoFlushDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.NINE));
		cards.add(new Card(Suit.CLUBS, Value.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.SPADES, Value.JACK));
		cards.add(new Card(Suit.DIAMONDS, Value.KING));
		cards.add(new Card(Suit.DIAMONDS, Value.TEN));
		assertNull(HandAnalyser.hasFlush(cards));
	}
}
//J=