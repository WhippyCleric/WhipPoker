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
	private int chipCount;

	/**
	 * Creates a Player
	 * 
	 * @param alias The unique id of the player
	 * @param initialChipCount The number of chips the player should be created with
	 */
	public Player(String alias, int initialChipCount){
		this.alias = alias;
		this.chipCount = initialChipCount;
	}

	/**
	 * 
	 * @return The unique id of the player
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * 
	 * @return The number of chips the player has
	 */
	public int getChipCount() {
		return chipCount;
	}

	/**
	 * Increase the players chip count by the number given 
	 * 
	 * @param chips Number of chips to give the player
	 */
	public void giveChips(int chips){
		this.chipCount+=chips;
	}

	/**
	 * Remove a specified number of chips from the players chip count
	 * 
	 * @param chips Number of chips to take from the player
	 * @throws IllegalArgumentException if number of chips to deduct is greater than the number of chips the player has
	 */
	public void deductChips(int chips){
		if(chips>this.chipCount){
			throw new IllegalArgumentException("Player " + alias + " only has " +  this.chipCount + " chips, can't deduct " + chips + " chips.");
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