//J-
package com.whippy.poker.state.beans;

public class Player {

	//~ ----------------------------------------------------------------------------------------------------------------
	//~ Instance fields 
	//~ ----------------------------------------------------------------------------------------------------------------

	private String alias;
	private int chipCount;

	public Player(String alias, int initialChipCount){
		this.alias = alias;
		this.chipCount = initialChipCount;
	}

	public String getAlias() {
		return alias;
	}

	public int getChipCount() {
		return chipCount;
	}

	public void giveChips(int chips){
		this.chipCount+=chips;
	}

	public void deductChips(int chips) throws IllegalAccessException{
		if(chips>this.chipCount){
			throw new IllegalAccessException("Player " + alias + " only has " +  this.chipCount + " chips, can't deduct " + chips + " chips.");
		}
		this.chipCount-=chips;
	}

}
//J=