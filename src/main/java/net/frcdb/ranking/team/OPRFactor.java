package net.frcdb.ranking.team;

import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.team.Team;
import net.frcdb.api.team.TeamStatistics;
import net.frcdb.ranking.RankingFactor;

/**
 * Ranks teams by their OPR Z scores.
 * @author tim
 */
public class OPRFactor extends TeamRankingFactor {

	public OPRFactor() {
	}

	public OPRFactor(boolean enabled) {
		super(enabled);
	}
	
	@Override
	public String getName() {
		return "OPR";
	}

	@Override
	public double getDefaultWeight() {
		return 1;
	}

	@Override
	public double getScore(Team obj) {
		double opr = 0;
		
		TeamStatistics stats = obj.getStatistics(getYear());
		if (stats != null) {
			opr = obj.getStatistics(getYear()).getOprZMean();
		}
		
		return getWeight() * opr;
	}
	
}
