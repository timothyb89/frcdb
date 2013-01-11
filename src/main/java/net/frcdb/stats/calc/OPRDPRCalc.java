package net.frcdb.stats.calc;

import Jama.CholeskyDecomposition;
import Jama.Matrix;
import com.googlecode.objectify.Ref;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.event.element.GameOPRProvider;
import net.frcdb.api.game.event.element.OPRStatistics;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.match.MatchType;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class OPRDPRCalc implements GameStatistic {

	private Matrix opr;
	private Matrix dpr;

	private Logger logger = LoggerFactory.getLogger(OPRDPRCalc.class);
	
	public OPRDPRCalc() {
	}

	private void init(Matrix m) {
		for (int y = 0; y < m.getRowDimension(); y++) {
			for (int x = 0; x < m.getColumnDimension(); x++) {
				m.set(y, x, 0);
			}
		}
	}

	private void calc(Game game, List<Team> teams) {
		Matrix m = new Matrix(teams.size(), teams.size());
		init(m);

		Matrix sFor = new Matrix(teams.size(), 1);
		init(sFor);

		Matrix sAgainst = new Matrix(teams.size(), 1);
		init(sAgainst);
		
		long startTime = System.currentTimeMillis();
		
		logger.info("Initializing matrices...");
		
		// init the m, sFor, and sAgainst matrices
		for (Match match : game.getMatches(MatchType.QUALIFICATION)) {
			// red teams
			for (Team y : match.getRedTeams()) {
				int yLit = teams.indexOf(y);
				
				// each team has played each other, incr matric point
				for (Team x : match.getRedTeams()) {
					int xLit = teams.indexOf(x);
					
					double count = m.get(yLit, xLit) + 1;
					
					m.set(yLit, xLit, count);
				}
				
				// score for red alliance
				double sForNew = sFor.get(yLit, 0) + match.getRedScore();
				sFor.set(yLit, 0, sForNew);
				
				// score against red alliance
				double sAgainstNew = sAgainst.get(yLit, 0) + match.getBlueScore();
				sAgainst.set(yLit, 0, sAgainstNew);
			}
			
			// blue teams
			for (Team y : match.getBlueTeams()) {
				int yLit = teams.indexOf(y);
				
				for (Team x : match.getBlueTeams()) {
					int xLit = teams.indexOf(x);
					
					double count = m.get(yLit, xLit) + 1;
					
					m.set(yLit, xLit, count);
				}
				
				// score for blue alliance
				double sForNew = sFor.get(yLit, 0) + match.getBlueScore();
				sFor.set(yLit, 0, sForNew);
				
				// score against blue alliance
				double sAgainstNew = sAgainst.get(yLit, 0) + match.getRedScore();
				sAgainst.set(yLit, 0, sAgainstNew);
			}
		}
		
		double secs = (System.currentTimeMillis() - startTime) / 1000;
		
		logger.info("Done, finished in " + secs + " seconds.");

		CholeskyDecomposition cd = m.chol();
		
		opr = cd.solve(sFor);
		dpr = cd.solve(sAgainst);
	}

	public static List<Team> getUniqueTeams(Collection<Match> matches) {
		List<Team> ret = new ArrayList<Team>();

		for (Match m : matches) {
			for (Team team : m.getBlueTeams()) {
				if (!ret.contains(team)) {
					ret.add(team);
				}
			}

			for (Team team : m.getRedTeams()) {
				if (!ret.contains(team)) {
					ret.add(team);
				}
			}
		}

		return ret;
	}

	public Matrix getOPR() {
		return opr;
	}

	public Matrix getDPR() {
		return dpr;
	}

	@Override
	public String[] getNames() {
		return new String[] {"OPRDPRCalc", "opr", "dpr"};
	}

	@Override
	public void calculate(Game game) {
		if (!(game instanceof GameOPRProvider)) {
			logger.warn("Game " + game.getGameName() 
					+ " does not support OPR, skipping...");
			return;
		}
		
		if (game.getQualificationMatches().isEmpty()) {
			logger.warn("Skipping due to lack of match data: " + game);
			return;
		}
		
		GameOPRProvider goprp = (GameOPRProvider) game;
		
		logger.info("Updating OPR and DPR for: " +
				game + ", with " 
				+ game.getQualificationMatches().size() + " total matches.");

		List<Team> uTeams = getUniqueTeams(game.getAllMatches());

		try {
			calc(game, uTeams);

			double oprTotal = 0;
			double dprTotal = 0;

			double highestOPR = Double.MIN_VALUE; // haxxy fix
			TeamEntry highestOPRTeam = null;

			double lowestOPR = Double.MAX_VALUE;
			TeamEntry lowestOPRTeam = null;

			SummaryStatistics stats = new SummaryStatistics();

			for (int i = 0; i < uTeams.size(); i++) {
				TeamEntry ent = game.getEntry(uTeams.get(i));
				if (ent instanceof OPRProvider) {
					OPRProvider pEnt = (OPRProvider) ent;

					double o = opr.get(i, 0);
					pEnt.setOPR(o);
					oprTotal += o;

					stats.addValue(o);

					if (o > highestOPR) {
						highestOPR = o;
						highestOPRTeam = ent;
					}

					if (o < lowestOPR) {
						lowestOPR = o;
						lowestOPRTeam = ent;
					}

					double d = dpr.get(i, 0);
					pEnt.setDPR(d);
					dprTotal += d;
				}
			}
			
			// get top teams
			List<TeamEntry> sortedTeams = new ArrayList<TeamEntry>();
			sortedTeams.addAll(game.getTeams());
			Collections.sort(sortedTeams, new Comparator<TeamEntry>() {

				@Override
				public int compare(TeamEntry o1, TeamEntry o2) {
					double a = ((OPRProvider) o1).getOPR();
					double b = ((OPRProvider) o2).getOPR();
					
					if (a < b) {
						return 1;
					} else if (a > b) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			
			List<Ref<TeamEntry>> topTeams = new ArrayList<Ref<TeamEntry>>(5);
			for (int i = 0; i < Math.min(5, sortedTeams.size()); i++) {
				topTeams.add(Ref.create(sortedTeams.get(i)));
			}
			
			OPRStatistics oprStats = new OPRStatistics(stats);
			oprStats.setTopTeams(topTeams);
			goprp.setOPRStatistics(oprStats);

			// set team z scores
			for (int i = 0; i < uTeams.size(); i++) {
				TeamEntry ent = game.getEntry(uTeams.get(i));
				if (ent instanceof OPRProvider) {
					OPRProvider pEnt = (OPRProvider) ent;

					double z = (pEnt.getOPR() - stats.getMean()) /
							stats.getStandardDeviation();

					pEnt.setOPRZ(z);
					Database.save().entity(ent);
				}
			}

			goprp.setAverageOPR(stats.getMean());

			double averageDPR = dprTotal / uTeams.size();
			goprp.setAverageDPR(averageDPR);

			goprp.setTotalOPR(oprTotal);
			goprp.setTotalDPR(dprTotal); 

			//System.out.println("[Debug] highestOPRTeam=" + highestOPRTeam.getTeam());

			goprp.setHighestOPR(highestOPR);
			goprp.setHighestOPRTeam(highestOPRTeam);

			goprp.setLowestOPR(lowestOPR);
			goprp.setLowestOPRTeam(lowestOPRTeam);

			Database.save().entity(game);
		} catch (Exception ex) {
			logger.error("Failed OPR calculation", ex);
		}
	}

	@Override
	public String getBackendName() {
		return "stat-compute";
	}

}
