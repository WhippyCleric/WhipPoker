//J-
package com.whippy.poker.ai.utils.beans;

public class PossibleOutcomes {

	private Float winChance;
	private Float drawChance;
	private Float lossChance;

	public PossibleOutcomes(Float winChance, Float drawChance, Float lossChance) {
		super();
		this.winChance = winChance;
		this.drawChance = drawChance;
		this.lossChance = lossChance;
	}
	public Float getWinChance() {
		return winChance;
	}
	public void setWinChance(Float winChance) {
		this.winChance = winChance;
	}
	public Float getDrawChance() {
		return drawChance;
	}
	public void setDrawChance(Float drawChance) {
		this.drawChance = drawChance;
	}
	public Float getLossChance() {
		return lossChance;
	}
	public void setLossChance(Float lossChance) {
		this.lossChance = lossChance;
	}


}
