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

	public Hand() {
		super();
	}



	public void setHand(Card[] hand) {
		this.hand = hand;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Hand){
			Hand toCompare = (Hand) obj;
			if(toCompare.getHand()[0].equals(this.hand[0]) && toCompare.getHand()[1].equals(this.hand[1])){
				return true;
			}else{
				return toCompare.getHand()[0].equals(this.hand[1]) && toCompare.getHand()[1].equals(this.hand[0]);
			}
		}
		return false;
	}

	@Override
	public int hashCode(){
		int card1Hash = this.hand[0].hashCode();
		int card2Hash = this.hand[1].hashCode();
		if(card1Hash<card2Hash){
			return Integer.valueOf(card1Hash + "" + card2Hash);
		}else{
			return Integer.valueOf(card2Hash + "" + card1Hash);
		}
	}

	/**
	 *
	 * @return the cards in the hand
	 */
	public Card[] getHand(){
		return this.hand;
	}

	@Override
	public String toString(){
		return hand[0].toString() + " " + hand[1].toString();
	}

}
//J=