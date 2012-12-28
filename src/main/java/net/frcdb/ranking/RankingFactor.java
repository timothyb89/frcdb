package net.frcdb.ranking;

import net.frcdb.api.team.Team;

/**
 *
 * @author tim
 */
public abstract class RankingFactor<T extends Object> {

	private double weight;
	private boolean enabled;
	
	public RankingFactor() {
		weight = getDefaultWeight();
	}

	public RankingFactor(boolean enabled) {
		weight = getDefaultWeight();
		this.enabled = enabled;
	}

	public abstract String getName();
	
	public abstract double getDefaultWeight();

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public abstract double getScore(T obj);
}
