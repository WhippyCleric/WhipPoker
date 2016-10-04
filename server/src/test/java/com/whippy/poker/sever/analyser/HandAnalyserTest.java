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
import com.whippy.poker.server.beans.FiveCardHand;

public class HandAnalyserTest {

	@Test
	public void testStraightDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CLUBS, Value.NINE));
		cards.add(new Card(Suit.HEARTS, Value.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Value.QUEEN));
		cards.add(new Card(Suit.SPADES, Value.JACK));
		cards.add(new Card(Suit.DIAMONDS, Value.EIGHT));
		cards.add(new Card(Suit.DIAMONDS, Value.TEN));
		FiveCardHand hasStraight = HandAnalyser.hasStraight(cards, null);
		assertTrue(hasStraight!=null);
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
		assertFalse(HandAnalyser.hasStraight(cards, null)!=null);
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
		assertTrue(HandAnalyser.hasStraight(cards, null)!=null);
	}

	@Test
	public void testFlushDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.NINE));
		cards.add(new Card(Suit.DIAMONDS, Value.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		cards.add(new Card(Suit.DIAMONDS, Value.KING));
		cards.add(new Card(Suit.DIAMONDS, Value.TEN));

		FiveCardHand hasFlush = HandAnalyser.hasFlush(cards);
		assertTrue(hasFlush.getCards().get(0).getSuit().equals(Suit.DIAMONDS));
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

	@Test
	public void testNoStraightFlushDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.NINE));
		cards.add(new Card(Suit.CLUBS, Value.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.SPADES, Value.JACK));
		cards.add(new Card(Suit.DIAMONDS, Value.KING));
		cards.add(new Card(Suit.DIAMONDS, Value.TEN));
		assertFalse(HandAnalyser.hasStraightFlush(cards)!=null);
	}

	@Test
	public void testStraightFlushDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Value.FIVE));
		cards.add(new Card(Suit.DIAMONDS, Value.THREE));
		cards.add(new Card(Suit.DIAMONDS, Value.FOUR));
		cards.add(new Card(Suit.DIAMONDS, Value.SIX));
		assertTrue(HandAnalyser.hasStraightFlush(cards)!=null);
	}

	@Test
	public void testFourOfAKindDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.TWO));
		cards.add(new Card(Suit.DIAMONDS, Value.FIVE));
		cards.add(new Card(Suit.SPADES, Value.TWO));
		cards.add(new Card(Suit.HEARTS, Value.TWO));
		cards.add(new Card(Suit.DIAMONDS, Value.THREE));
		FiveCardHand resultCards = HandAnalyser.hasFourOfAKind(cards);
		assertTrue(resultCards!=null);
	}

	@Test
	public void testFourOfAKindNoDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.TWO));
		cards.add(new Card(Suit.DIAMONDS, Value.FIVE));
		cards.add(new Card(Suit.SPADES, Value.TWO));
		cards.add(new Card(Suit.HEARTS, Value.KING));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		assertFalse(HandAnalyser.hasFourOfAKind(cards)!=null);
	}

	@Test
	public void testFullHouseNoDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.TWO));
		cards.add(new Card(Suit.DIAMONDS, Value.FIVE));
		cards.add(new Card(Suit.SPADES, Value.KING));
		cards.add(new Card(Suit.HEARTS, Value.ACE));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		assertFalse(HandAnalyser.hasFullHouse(cards)!=null);
	}
	@Test
	public void testFullHouseDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.ACE));
		cards.add(new Card(Suit.SPADES, Value.KING));
		cards.add(new Card(Suit.HEARTS, Value.ACE));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		assertTrue(HandAnalyser.hasFullHouse(cards)!=null);
	}

	@Test
	public void testFullHouseDetectionWith2Pairs(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.THREE));
		cards.add(new Card(Suit.SPADES, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.THREE));
		cards.add(new Card(Suit.CLUBS, Value.ACE));
		cards.add(new Card(Suit.SPADES, Value.TWO));
		cards.add(new Card(Suit.HEARTS, Value.ACE));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		FiveCardHand hasFullHouse = HandAnalyser.hasFullHouse(cards);
		assertTrue(hasFullHouse!=null);
	}

	@Test
	public void testThreeOfAKindDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.JACK));
		cards.add(new Card(Suit.CLUBS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.ACE));
		cards.add(new Card(Suit.SPADES, Value.KING));
		cards.add(new Card(Suit.HEARTS, Value.ACE));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		assertTrue(HandAnalyser.hasThreeOfAKind(cards)!=null);
	}

	@Test
	public void testThreeOfAKindNoDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.JACK));
		cards.add(new Card(Suit.CLUBS, Value.ACE));
		cards.add(new Card(Suit.SPADES, Value.KING));
		cards.add(new Card(Suit.HEARTS, Value.QUEEN));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		assertFalse(HandAnalyser.hasThreeOfAKind(cards)!=null);
	}
	@Test
	public void testTwoPairDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.KING));
		cards.add(new Card(Suit.CLUBS, Value.KING));
		cards.add(new Card(Suit.CLUBS, Value.ACE));
		cards.add(new Card(Suit.SPADES, Value.QUEEN));
		cards.add(new Card(Suit.HEARTS, Value.THREE));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		cards.add(new Card(Suit.CLUBS, Value.THREE));
		assertTrue(HandAnalyser.hasTwoPair(cards)!=null);
	}
	@Test
	public void testTwoPairNoDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.CLUBS, Value.JACK));
		cards.add(new Card(Suit.CLUBS, Value.ACE));
		cards.add(new Card(Suit.SPADES, Value.KING));
		cards.add(new Card(Suit.HEARTS, Value.QUEEN));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		assertFalse(HandAnalyser.hasTwoPair(cards)!=null);
	}

	@Test
	public void testPairDetection(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CLUBS, Value.JACK));
		cards.add(new Card(Suit.CLUBS, Value.ACE));
		cards.add(new Card(Suit.SPADES, Value.KING));
		cards.add(new Card(Suit.DIAMONDS, Value.TWO));
		cards.add(new Card(Suit.HEARTS, Value.QUEEN));
		cards.add(new Card(Suit.DIAMONDS, Value.ACE));
		assertTrue(HandAnalyser.hasPair(cards)!=null);
	}
}
//J=