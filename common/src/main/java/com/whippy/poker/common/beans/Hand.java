//J-
package com.whippy.poker.common.beans;

/**
 *
 * Represents a hand of 2 cards
 *
 * @author mdunn
 *
 */
public class Hand {

	//~ ----------------------------------------------------------------------------------------------------------------
	//~ Instance fields
	//~ ----------------------------------------------------------------------------------------------------------------

	private Card[] hand;

	//~ ----------------------------------------------------------------------------------------------------------------
	//~ Constructors
	//~ ----------------------------------------------------------------------------------------------------------------

	/**
	 * Create a new hand given 2 cards
	 *
	 * @param card1 the first card
	 * @param card2 the second card
	 */
	public Hand(Card card1, Card card2) {
		hand = new Card[2];
		hand[0] = card1;
		hand[1] = card2;
	}

	/**
	 *
	 * @return the cards in the hand
	 */
	public Card[] getCards(){
		return this.hand;
	}

	@Override
	public String toString(){
		return hand[0].toString() + " " + hand[1].toString();
	}
}
//J=