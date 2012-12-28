package net.frcdb.ranking.team;

import net.frcdb.api.team.Team;
import net.frcdb.ranking.RankingFactor;

/**
 * A ranking factor for teams
 * @author tim
 */
public abstract class TeamRankingFactor extends RankingFactor<Team> {

	private int year;

	public TeamRankingFactor() {
	}

	public TeamRankingFactor(boolean enabled) {
		super(enabled);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
}
