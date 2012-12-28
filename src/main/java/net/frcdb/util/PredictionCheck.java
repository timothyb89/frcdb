package net.frcdb.util;

import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.Alliance;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.match.MatchType;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;

/**
 * Just to check a theory...
 * @author tim
 */
public class PredictionCheck {

	public static void main(String[] args) {
		Database db = new Database();

		double qualTotal = 0;
		double qualCorrect = 0;
		
		double qFinalTotal = 0;
		double qFinalCorrect = 0;
		
		double sFinalTotal = 0;
		double sFinalCorrect = 0;
		
		double finalTotal = 0;
		double finalCorrect = 0;

		for (Event e : db.getEvents()) {
			if (!e.getShortName().equals("michigan")) {
				continue;
			}
			
			Game g = e.getGame(2011);
			if (g == null) {
				continue;
			}

			for (Match m : g.getMatches(MatchType.QUALIFICATION)) {
				qualTotal++;
				qualCorrect += predict(g, m);
			}
			
			for (Match m : g.getMatches(MatchType.QUARTERFINAL)) {
				qFinalTotal++;
				qFinalCorrect += predict(g, m);
			}
			
			for (Match m : g.getMatches(MatchType.SEMIFINAL)) {
				sFinalTotal++;
				sFinalCorrect += predict(g, m);
			}
			
			for (Match m : g.getMatches(MatchType.FINAL)) {
				finalTotal++;
				finalCorrect += predict(g, m);
			}
		}

		double globalTotal = qFinalTotal + sFinalTotal + finalTotal;
		double globalCorrect = qFinalCorrect + sFinalCorrect + finalCorrect;
		
		System.out.println("Qualifications:");
		System.out.println("--------------");
		System.out.println("Total Matches: " + qualTotal);
		System.out.println("# correct:     " + qualCorrect);
		System.out.println("% correct:     " + ((qualCorrect / qualTotal) * 100));
		
		System.out.println();
		
		System.out.println("Quarterfinals:");
		System.out.println("--------------");
		System.out.println("Total Matches: " + qFinalTotal);
		System.out.println("# correct:     " + qFinalCorrect);
		System.out.println("% correct:     " + ((qFinalCorrect / qFinalTotal) * 100));
		
		System.out.println();
		
		System.out.println("Semifinals:");
		System.out.println("--------------");
		System.out.println("Total Matches: " + sFinalTotal);
		System.out.println("# correct:     " + sFinalCorrect);
		System.out.println("% correct:     " + ((sFinalCorrect / sFinalTotal) * 100));

		System.out.println();
		
		System.out.println("Finals:");
		System.out.println("--------------");
		System.out.println("Total Matches: " + finalTotal);
		System.out.println("# correct:     " + finalCorrect);
		System.out.println("% correct:     " + ((finalCorrect / finalTotal) * 100));
		
		System.out.println();
		
		System.out.println("Global Elims:");
		System.out.println("--------------");
		System.out.println("Total Matches: " + globalTotal);
		System.out.println("# correct:     " + globalCorrect);
		System.out.println("% correct:     " + ((globalCorrect / globalTotal) * 100));
	}
	
	private static int predict(Game g, Match m) {
		// calculate prediction
		double redPrediction = 0;
		double bluePrediction = 0;
		
		for (Team t : m.getRedTeams()) {
			TeamEntry entry = g.getEntry(t);
			if (entry != null) {
				redPrediction += ((OPRProvider) entry).getOPR();
				bluePrediction += ((OPRProvider) entry).getDPR();
			}
		}

		for (Team t : m.getBlueTeams()) {
			TeamEntry entry = g.getEntry(t);
			if (entry != null) {
				bluePrediction += ((OPRProvider) entry).getOPR();
				redPrediction += ((OPRProvider) entry).getDPR();
			}
		}

		Alliance a = getWinner(redPrediction, bluePrediction);
		if (a.equals(m.getWinningAlliance())) {
			return 1;
		} else {
			return 0;
		}
	}

	private static Alliance getWinner(double red, double blue) {
		if (red > blue) {
			return Alliance.RED;
		} else if (red < blue) {
			return Alliance.BLUE;
		} else {
			return Alliance.TIE;
		}
	}
}
