package net.frcdb.stats.mining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A shiny new team miner that grabs things without overwriting old stuff.
 * 
 * Team history is now outdated and should be superceded by our own data. In
 * theory at least.
 * 
 * This TeamMiner should replace the '10 one and work in future years, assuming
 * nothing changes on the FIRST end.
 * 
 * Note: this doesn't seem to fetch team websites. We'll need something custom
 * for that ...
 * @author tim
 */
public class TeamMiner {

	public static final String TEAMS_URL = 
			"https://my.usfirst.org/frc/scoring/index.lasso?page=teamlist";
	
	private Logger logger = LoggerFactory.getLogger(TeamMiner.class);
	
	public static final int SKIP_LINES = 2;
	
	public TeamMiner() throws IOException {
		URL url = new URL(TEAMS_URL);
		InputStream is = url.openStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		Database db = new Database();
		
		String input;
		while ((input = br.readLine()) != null) {
			// ignore blank / html lines
			if (input.startsWith("<")
					|| input.trim().isEmpty()
					|| !Character.isDigit(input.charAt(0))) {
				continue;
			}
			
			String[] fields = input.split("\t", 10);
			for (int i = 0; i < fields.length; i++) {
				fields[i] = StringEscapeUtils.unescapeHtml(fields[i]);
			}
			
			int number = Integer.parseInt(fields[1]);
			
			String name;
			String city;
			String state;
			String country;
			String nickname;
			int rookieYear;
			
			if (number == 3811) {
				// FIRST fail :( still needed in 2013, wow.
				name = fields[2];
				city = fields[4];
				state = fields[5];
				country = fields[6];
				nickname = fields[8]; // bad FIRST! bad!
				
				logger.debug("Team #3811 fix...");
				
				// sigh...
				String[] badRName = fields[9].split("\t", 2);
				rookieYear = Integer.parseInt(badRName[0]);
			} else {
				name = fields[2];
				city = fields[4];
				state = fields[5];
				country = fields[6];
				nickname = fields[7];
				rookieYear = Integer.parseInt(fields[8]);
			}
			
			Team t = db.getTeam(number);
			if (t == null) { // new team
				t = new Team(name, number);
				t.setCity(city);
				t.setState(state);
				t.setCountry(country);
				t.setNickname(nickname);
				t.setRookieSeason(rookieYear);
				
				logger.info("New team: #" + number + ": " + nickname);
			} else { // update old
				t.setName(name);
				t.setCity(city);
				t.setState(state);
				t.setCountry(country);
				t.setNickname(nickname);
				t.setRookieSeason(rookieYear);
				
				logger.info("Updated team: #" + number);
			}
			
			Database.save().entity(t).now();
		}
	}
	
}
