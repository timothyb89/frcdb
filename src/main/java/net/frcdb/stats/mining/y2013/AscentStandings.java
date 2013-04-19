package net.frcdb.stats.mining.y2013;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.standing.AscentStanding;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class AscentStandings {
	
	public static final String TEST_URL =
			"http://www2.usfirst.org/2013comp/events/NHMA/rankings.html";
	
	private Game game;
	private String url;

	private Logger logger = LoggerFactory.getLogger(AscentStanding.class);
	
	public AscentStandings(Game game) {
		this.game = game;
		
		url = game.getStandingsURL();
	}

	public List<Standing> parse() throws IOException {
		List<Standing> ret = new ArrayList<Standing>();

		Database db = Database.getInstance();
		
		Document doc;
		try {
			doc = Jsoup.parse(new URL(url), 5000);
		} catch (Exception ex) {
			logger.warn("Failed fetching standings at " + url, ex);
			return ret;
		}
		
		Elements rows = doc.select("div.Section1 table");
		
		Iterator<Element> tableIter = rows.iterator();
		tableIter.next(); // skip 2, selector won't work for some reason
		tableIter.next();
		
		Element table = tableIter.next();
		
		for (Element row : table.select("tr:gt(1)")) { // skip first 2 headers
			AscentStanding s = (AscentStanding) game.createStanding();
			
			// 0: rank
			// 1: team #
			// 2: qualification score
			// 3: AP (?)
			// 4: coopertition points
			// 5: teleop points
			// 6: record (wins-losses-ties)
			// 7 disqualifications
			// 8: matches played

			s.setRank(Integer.parseInt(row.child(0).text()));
			
			Team team = db.getTeam(Integer.parseInt(row.child(1).text()));
			if (team == null) {
				logger.warn("Unknown team " + row.child(1).text() + ", skipping");
				continue;
			}
			s.setTeam(game.getEntry(team));

			s.setQualificationScore(Float.parseFloat(row.child(2).text()));
			// todo: old/invalid!
			// s.setHybridPoints(Float.parseFloat(row.child(3).text()));
			// s.setBridgePoints(Float.parseFloat(row.child(4).text()));
			s.setTeleopPoints(Float.parseFloat(row.child(5).text()));

			s.setCoopertitionPoints((int) Float.parseFloat(row.child(4).text()));

			String[] wlt = StringUtils.split(row.child(6).text(), '-');
			s.setWins(Integer.parseInt(wlt[0]));
			s.setLosses(Integer.parseInt(wlt[1]));
			s.setTies(Integer.parseInt(wlt[2]));

			s.setDisqualifications(Integer.parseInt(row.child(7).text()));
			s.setMatchesPlayed(Integer.parseInt(row.child(8).text()));

			ret.add(s);
		}

		return ret;
	}
	
}
