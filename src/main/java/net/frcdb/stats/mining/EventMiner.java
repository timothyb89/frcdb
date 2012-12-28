package net.frcdb.stats.mining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.event.GameType;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.util.DateUtil;
import net.frcdb.util.ListUtil;
import net.frcdb.util.Sources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Similar to TeamMiner. This grabs the FIRST event list and updates / adds new
 * events as needed. This only works for whatever the current game is at the
 * time of running, though, so watch out.
 * @author tim
 */
public class EventMiner {

	public static final String EVENTS_URL = 
			"https://my.usfirst.org/frc/scoring/index.lasso?page=eventlist";
	
	public static final String BASE_TEAMLIST_URL = 
			"https://my.usfirst.org/frc/scoring/index.lasso?page=event_teamlist&ID_event=";
	
	private List<Team> teams;
	
	private final Logger logger = LoggerFactory.getLogger(EventMiner.class);
	
	public EventMiner() throws MalformedURLException, IOException {
		URL url = new URL(EVENTS_URL);
		
		Document doc;
		try {
			doc = Jsoup.parse(url, 5000);
		} catch (Exception ex) {
			logger.error("Error parsing events list", ex);
			return;
		}
		
		String text = doc.select("pre").text();
		
		Database db = new Database();
		
		// cache teams
		teams = new ArrayList<Team>();
		for (Team t : db.getTeams()) {
			teams.add(t);
		}
		
		GameType current = GameType.getCurrent();
		System.out.println("[Info] Creating new games for events: " 
				+ current.getName() + " " + current.getYear());
		
		for (String input : ListUtil.safeSplit(text, "\n")) {
			// ignore blank / html lines
			if (input.startsWith("<")
					|| input.trim().isEmpty()
					|| !Character.isDigit(input.charAt(0))) {
				continue;
			}
			
			try {
				String[] fields = input.split("\t");
				
				// trim everything
				for (int i = 0; i < fields.length; i++) {
					fields[i] = fields[i].trim();
				}
				
				int eid = Integer.parseInt(fields[0]);
				String name = fields[1];
				String venue = fields[2];
				String city = fields[3];
				String state = fields[4];
				String country = fields[5];
				String startDate = fields[6];
				String endDate = fields[7];
				int year = Integer.parseInt(fields[8]); // meh
				// number of teams: #9
				// team list url: #10
				String id = fields[11];

				// attempt to find the event
				Event e = findEvent(db, name, id);
				if (e == null) {
					if (getBoolean("No event found. Add new?")) {
						e = new Event();
						e.setName(name);
						e.setVenue(venue);
						e.setCity(city);
						e.setState(state);
						e.setCountry(country);
						e.setIdentifier(id);

						String shortName = getInput("Short name for " + name + ":");
						e.setShortName(shortName);
					} else {
						// get existing event by short name
						while (e == null) {
							String sn = getInput("Short name of parent event:");
							e = db.getEventByShortName(sn);

							if (e == null) {
								System.out.println("[Error] No event found: " + sn);
							} else {
								e.addAlias(name);

								// don't update the name - FIRST probably got
								// it wrong
								e.setVenue(venue);
								e.setCity(city);
								e.setState(state);
								e.setCountry(country);
								e.setIdentifier(id);
							}
						}
					}
				} else {
					// found the event successfully, update it
					e.setName(name);
					e.setVenue(venue);
					e.setCity(city);
					e.setState(state);
					e.setCountry(country);
					e.setIdentifier(id);

					System.out.println("[Info ] Updated event: " + e.getShortName());
				}

				// data only applies to current year! :(
				if (year == current.getYear()) {
					Game g = e.getGame(year);
					
					if (g == null) {
						// current event, I guess... create a Game, don't duplicate
						g = current.create(e);
						g.setEid(eid);
						g.setStartDate(DateUtil.parse(startDate));
						g.setEndDate(DateUtil.fixEndDate(DateUtil.parse(endDate)));

						g.setResultsURL(Sources.getResultsURL(id, year));
						g.setStandingsURL(Sources.getStandingsURL(id, year));
						g.setAwardsURL(Sources.getAwardsURL(id, year));

						// get + store the new entries in the db
						for (TeamEntry te : getTeams(g, eid)) {
							db.store(te);
						}
						
						System.out.println("[Info ] Created new " 
								+ current.getName() + " game for event " 
								+ e.getName() + ". " + g.getTeams().size()
								+ " teams attending.");
					} else {
						// update teamlist / eid / etc
						// TODO: The EID should be used for comparison
						g.setEid(eid);
						
						// reset the start and end dates
						g.setStartDate(DateUtil.parse(startDate));
						
						g.setEndDate(DateUtil.fixEndDate(DateUtil.parse(endDate)));
						
						// remove the old entries
						db.purgeTeamEntries(g);
						
						// get + store the new entries to the database
						for (TeamEntry te : getTeams(g, eid)) {
							db.store(te);
						}
						
						g.setResultsURL(Sources.getResultsURL(g));
						g.setStandingsURL(Sources.getStandingsURL(g));
						g.setAwardsURL(Sources.getAwardsURL(g));
						
						System.out.println("[Info ] Updated teamlist for "
								+ e.getShortName() + " " + g.getGameYear() 
								+ ". " + g.getTeams().size() + " teams "
								+ "attending.");
					}
					
					db.store(g);
				}

				// store the updated event
				db.store(e);
			} catch (Exception ex) {
				logger.error("Could not process event from: " + input);
			}
		}
	}
	
	private static Event findEvent(Database db, String name, String id) {
		Event e = db.getEventByIdentifier(id);
		if (e == null) {
			System.out.println("[Info ] Searching for event: " + name);
			e = db.getNearestEvent(name);
			
			boolean correct = getBoolean("Closest match: " + e.getName()
					+ ", is this correct?");
			if (correct) {
				return e;
			} else {
				// no match found
				return null;
			}
		} else {
			return e;
		}
	}
	
	private static String getInput(String prompt) {
		System.out.print("[Input] " + prompt + " ");
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		try {
			return br.readLine();
		} catch (Exception ex) {
			return null;
		}
	}
	
	private static boolean getBoolean(String prompt) {
		while (true) {
			String input = getInput(prompt + " (yes/no)");
			if (input != null) {
				if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
					return true;
				} else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
					return false;
				}
			}
		}
	}
	
	private Team getTeam(int number) {
		for (Team t : teams) {
			if (t.getNumber() == number) {
				return t;
			}
		}
		
		return null;
	}
	
	private List<TeamEntry> getTeams(Game game, int eid) throws IOException {
		Document doc;
		try {
			doc = Jsoup.parse(new URL(BASE_TEAMLIST_URL + eid), 5000);
		} catch (Exception ex) {
			logger.error("Failed to download team list for event " + eid, ex);
			return null;
		}

		List<Integer> tnums = new ArrayList<Integer>();
		
		String text = doc.select("pre").text();
		
		for (String s : ListUtil.safeSplit(text, "\n")) {
			s = s.trim();
			
			if (s.isEmpty() || s.startsWith("<") || s.startsWith("ID_team")) {
				continue;
			}

			StringTokenizer st = new StringTokenizer(s);
			st.nextToken();
			tnums.add(Integer.parseInt(st.nextToken()));
		}
		
		if (tnums.isEmpty()) {
			logger.warn("Invalid team list, aborting update");
		}

		List<TeamEntry> ret = new ArrayList<TeamEntry>();

		for (int i : tnums) {
			Team t = Database.getInstance().getTeam(i);
			if (t == null) {
				logger.warn("Event links to nonexistent team: " + i);
				continue;
			}
			
			TeamEntry e = game.createEntry(t);
			ret.add(e);
		}
		
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		new EventMiner();
	}
	
}
