package net.frcdb.stats.mining.y2012;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.match.MatchType;
import net.frcdb.db.Database;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author tim
 */
public class ReboundMatchResults {
	
	private Game game;

	public ReboundMatchResults(Game game) {
		this.game = game;
	}
	
	public List<Match> parse() {
		List<Match> ret = new ArrayList<Match>();
		
		Database db = Database.getInstance();
		
		Document doc;
		try {
			doc = Jsoup.parse(new URL(game.getResultsURL()), 5000);
		} catch (Exception ex) {
			System.err.println("Failed to get match results at "
					+ game.getResultsURL() + ": " + ex.getMessage());
			return ret;
		}
		
		Elements eles = doc.select("table");
		
		Iterator<Element> tableIter = eles.iterator();
		tableIter.next(); // skip 2
		tableIter.next();
		
		// CMP only has elim matches, skip qualifications table
		if (!game.getEvent().isChampionship()) {
			Element qualsTable = tableIter.next();
			for (Element row : qualsTable.select("tr:gt(2)")) { // ')'?
				Match m = new Match(game);
				m.setType(MatchType.QUALIFICATION);

				m.setTime(row.child(0).text());
				m.setNumber(Integer.parseInt(row.child(1).text()));

				m.addRedTeam(db.getTeam(Integer.parseInt(row.child(2).text())));
				m.addRedTeam(db.getTeam(Integer.parseInt(row.child(3).text())));
				m.addRedTeam(db.getTeam(Integer.parseInt(row.child(4).text())));

				m.addBlueTeam(db.getTeam(Integer.parseInt(row.child(5).text())));
				m.addBlueTeam(db.getTeam(Integer.parseInt(row.child(6).text())));
				m.addBlueTeam(db.getTeam(Integer.parseInt(row.child(7).text())));

				String redText = row.child(8).text();
				try {
					m.setRedScore(Integer.parseInt(redText));
				} catch (NumberFormatException ex) {
					if (redText.equalsIgnoreCase("DQ")) {
						m.setRedScore(Match.SCORE_DISQUALIFIED);
					} else {
						m.setRedScore(Match.SCORE_UNKNOWN);
					}
				}

				String blueText = row.child(9).text();
				try {
					m.setBlueScore(Integer.parseInt(blueText));
				} catch (NumberFormatException ex) {
					if (blueText.equalsIgnoreCase("DQ")) {
						m.setBlueScore(Match.SCORE_DISQUALIFIED);
					} else {
						m.setBlueScore(Match.SCORE_UNKNOWN);
					}
				}

				ret.add(m);
			}
		}
		
		Element elimsTable = tableIter.next();
		for (Element row : elimsTable.select("tr:gt(2)")) {
			Match m = new Match(game);
			
			m.setTime(row.child(0).text());
			m.setType(getElimMatchType(row.child(1).text()));
			m.setNumber(Integer.parseInt(row.child(2).text()));

			m.addRedTeam(db.getTeam(Integer.parseInt(row.child(3).text())));
			m.addRedTeam(db.getTeam(Integer.parseInt(row.child(4).text())));
			m.addRedTeam(db.getTeam(Integer.parseInt(row.child(5).text())));

			m.addBlueTeam(db.getTeam(Integer.parseInt(row.child(6).text())));
			m.addBlueTeam(db.getTeam(Integer.parseInt(row.child(7).text())));
			m.addBlueTeam(db.getTeam(Integer.parseInt(row.child(8).text())));
			
			String redText = row.child(9).text();
			try {
				m.setRedScore(Integer.parseInt(redText));
			} catch (NumberFormatException ex) {
				if (redText.equalsIgnoreCase("DQ")) {
					m.setRedScore(Match.SCORE_DISQUALIFIED);
				} else {
					m.setRedScore(Match.SCORE_UNKNOWN);
				}
			}

			String blueText = row.child(10).text();
			try {
				m.setBlueScore(Integer.parseInt(blueText));
			} catch (NumberFormatException ex) {
				if (blueText.equalsIgnoreCase("DQ")) {
					m.setBlueScore(Match.SCORE_DISQUALIFIED);
				} else {
					m.setBlueScore(Match.SCORE_UNKNOWN);
				}
			}
			
			ret.add(m);
		}
		
		return ret;
	}
	
	private MatchType getElimMatchType(String desc) {
		desc = desc.toLowerCase();

		if (desc.startsWith("qtr")) {
			return MatchType.QUARTERFINAL;
		} else if (desc.startsWith("semi")) {
			return MatchType.SEMIFINAL;
		} else {
			return MatchType.FINAL;
		}
	}
	
}
