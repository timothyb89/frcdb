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
 * TODO: need a YearStatistic
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
			SummaryStatistics zSummary = new SummaryStatistics();
			for (TeamEntry entry : entries) {
				if (entry instanceof OPRProvider) {
					OPRProvider opr = (OPRProvider) entry;
					summary.addValue(opr.getOPR());
					zSummary.addValue(opr.getOPRZ());
				}
			}
			
			stats.setOprMin(summary.getMin());
			stats.setOprMax(summary.getMax());
			stats.setOprMean(summary.getMean());
			stats.setOprSum(summary.getSum());
			stats.setOprVariance(summary.getMean());
			stats.setOprStandardDeviation(summary.getStandardDeviation());
			
			stats.setOprZMin(zSummary.getMin());
			stats.setOprZMax(zSummary.getMax());
			stats.setOprZMean(zSummary.getMean());
			stats.setOprZSum(zSummary.getSum());
			stats.setOprZVariance(zSummary.getMean());
			stats.setOprZStandardDeviation(zSummary.getStandardDeviation());
			
			Database.save().entity(team).now();
			Database.ofy().clear();
		}
	}

	@Override
	public String getBackendName() {
		return "stat-compute";
	}
	
}
