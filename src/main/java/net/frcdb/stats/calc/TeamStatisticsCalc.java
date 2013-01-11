package net.frcdb.stats.calc;

import java.util.Collection;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.team.Team;
import net.frcdb.api.team.TeamStatistics;
import net.frcdb.db.Database;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author tim
 */
public class TeamStatisticsCalc implements GameStatistic {

	@Override
	public String[] getNames() {
		return new String[] {"teamstats", "teamstatistics"};
	}

	@Override
	public void calculate(Game game) {
		// TODO: this may be too expensive...
		int year = game.getGameYear();
		for (Team team : Database.getInstance().getTeams()) {
			TeamStatistics stats = team.getStatistics(year);
			if (stats == null) {
				stats = new TeamStatistics();
				stats.setYear(year);
				team.addStatistics(stats);
			}
			
			Collection<TeamEntry> entries
					= Database.getInstance().getEntries(team, year);
			
			SummaryStatistics summary = new SummaryStatistics();
			for (TeamEntry entry : entries) {
				if (entry instanceof OPRProvider) {
					OPRProvider opr = (OPRProvider) entry;
					summary.addValue(opr.getOPRZ());
				}
			}
			
			stats.setOprZMin(summary.getMin());
			stats.setOprZMax(summary.getMax());
			stats.setOprZMean(summary.getMean());
			stats.setOprZSum(summary.getSum());
			stats.setOprZVariance(summary.getMean());
			stats.setOprZStandardDeviation(summary.getStandardDeviation());
			
			Database.save().entity(team);
		}
	}
	
}
