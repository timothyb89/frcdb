package net.frcdb.stats.mining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * A shiny new team miner that grabs things without overwriting old stuff.
 * 
 * Team history is now outdated and should be superceded by our own data. In
 * theory at least.
 * 
 * This TeamMiner should replace the '10 one and work in future years, assuming
 * nothing changes on the FIRST end.
 * @author tim
 */
public class TeamMiner {

	public static final String TEAMS_URL = 
			"https://my.usfirst.org/frc/scoring/index.lasso?page=teamlist";
	
	public static final int SKIP_LINES = 2;
	
	private List<Team> teamCache;
	
	public TeamMiner() throws IOException {
		URL url = new URL(TEAMS_URL);
		InputStream is = url.openStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		Database db = new Database();
		
		teamCache = new ArrayList<Team>();
		for (Team t : db.getTeams()) {
			teamCache.add(t);
		}
		
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
			
			System.out.format("[Debug] "
					+ "id:0=\"%s\", number:1=\"%s\", name:2=\"%s\", "
					+ "sname:3=\"%s\", city:4=\"%s\", state:5=\"%s\", "
					+ "country:6=\"%s\", nickname:7=\"%s\", ryear:8=\"%s\", "
					+ "rname:9=\"%s\"\n",
					fields[0],
					fields[1],
					fields[2],
					fields[3],
					fields[4],
					fields[5],
					fields[6],
					fields[7],
					fields[8],
					fields[9]);
			
			String name;
			String city;
			String state;
			String country;
			String nickname;
			int rookieYear;
			
			if (number == 3811) {
				// FIRST fail :(
				name = fields[2];
				city = fields[4];
				state = fields[5];
				country = fields[6];
				nickname = fields[8]; // bad FIRST! bad!
				
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
			
			Team t = getTeam(number);
			if (t == null) { // new team
				t = new Team(name, number);
				t.setCity(city);
				t.setState(state);
				t.setCountry(country);
				t.setNickname(nickname);
				t.setRookieSeason(rookieYear);
				
				System.out.println("New team: #" + number + ": " + nickname);
			} else { // update old
				t.setName(name);
				t.setCity(city);
				t.setState(state);
				t.setCountry(country);
				t.setNickname(nickname);
				t.setRookieSeason(rookieYear);
				
				System.out.println("Updating: #" + number);
			}
			
			db.store(t);
		}
	}
	
	public Team getTeam(int number) {
		for (Team t : teamCache) {
			if (t.getNumber() == number) {
				return t;
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) 
			throws MalformedURLException, IOException {
		
		new TeamMiner();
	}
	
}
