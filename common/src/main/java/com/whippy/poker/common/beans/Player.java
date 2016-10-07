//J-
package com.whippy.poker.common.beans;

/**
 * Represents a player for poker. A player only has an alias (i.e. name / id) and a stack of chips stored as numeric value
 *
 * @author mdunn
 *
 */
public class Player {

	//~ ----------------------------------------------------------------------------------------------------------------
	//~ Instance fields
	//~ ----------------------------------------------------------------------------------------------------------------

	private String alias;
	private double chipCount;

	/**
	 * Creates a Player
	 *
	 * @param alias The unique id of the player
	 * @param initialChipCount The number of chips the player should be created with
	 */
	public Player(String alias){
		this.alias = alias;
	}

	public Player(){
	}

	/**
	 *
	 * @return The unique id of the player
	 */
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setChipCount(double chipCount) {
		this.chipCount = chipCount;
	}

	/**
	 *
	 * @return The number of chips the player has
	 */
	public double getChipCount() {
		return chipCount;
	}

	/**
	 * Increase the players chip count by the number given
	 *
	 * @param chips Number of chips to give the player
	 */
	public void giveChips(double chips){
		this.chipCount+=chips;
	}

	/**
	 * Remove a specified number of chips from the players chip count, will stop at 0
	 *
	 * @param chips Number of chips to take from the player
	 */
	public void deductChips(double chips){
		if(chips>this.chipCount){
			this.chipCount=0;
		}
		this.chipCount-=chips;
	}

	@Override
	public String toString(){
		StringBuilder playerString = new StringBuilder();
		playerString.append("Name :");
		playerString.append(this.alias);
		playerString.append("\nChips :");
		playerString.append(this.chipCount);
		return playerString.toString();
	}

}
//J=