package net.frcdb.stats.calc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.util.Pair;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author tim
 */
public class GameSummaryCalc implements Statistic {

	@Override
	public String[] getNames() {
		return new String[] {"gamesummary"};
	}

	@Override
	public void calculate(Game game, List<Team> allTeams, Database db) {
		
		SummaryStatistics oprStats = new SummaryStatistics();
		
		Collection<TeamEntry> teams = game.getTeams();
		for (TeamEntry t : teams) {
			if (t instanceof OPRProvider) {
				OPRProvider opr = (OPRProvider) t;
				
				oprStats.addValue(opr.getOPR());
			}
		}
		
		System.out.println(oprStats);
		
		System.out.println("X-values:");
		
		// calculate x values
		
		List<Pair<TeamEntry, Double>> sorted = new ArrayList<Pair<TeamEntry, Double>>();
		for (TeamEntry t : teams) {
			if (t instanceof OPRProvider) {
				OPRProvider opr = (OPRProvider) t;
				double x = (opr.getOPR() - oprStats.getMean()) /
						oprStats.getStandardDeviation();
				
				sorted.add(new Pair<TeamEntry, Double>(t, x));
				
				System.out.println(t.getTeam() + "\t" + x);
			}
			// todo: store this somewhere
		}
		
		Collections.sort(sorted, comparator);
		
		System.out.println("\nSorted:");
		for (Pair<TeamEntry, Double> p : sorted) {
			System.out.println(p.getA().getTeam() + "\t" + p.getB());
		}
	}
	
	private Comparator<Pair<TeamEntry, Double>> comparator
			= new Comparator<Pair<TeamEntry, Double>>() {

		@Override
		public int compare(Pair<TeamEntry, Double> a, Pair<TeamEntry, Double> b) {
			double ret = a.getB() - b.getB();
			if (ret < 0) {
				return -1;
			} else if (ret == 0) {
				return 0;
			} else {
				return 1;
			}
		}
		
	};
	
}
