package net.frcdb.stats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.stats.calc.*;
import net.frcdb.util.ListUtil;

/**
 *
 * @author tim
 */
public class StatisticManager {

	private static StatisticManager instance;

	private List<GlobalStatistic> global;
	private List<Statistic> normal;

	private StatisticManager() {
		global = new ArrayList<GlobalStatistic>();
		normal = new ArrayList<Statistic>();

		initStats();
	}
	
	public static StatisticManager getInstance() {
		if (instance == null) {
			instance = new StatisticManager();
		}

		return instance;
	}

	private void initStats() {
		global.add(new DateCalc());
		global.add(new ExtraTeamInfo());

		normal.add(new OPRDPRCalc());
		normal.add(new TeamsFromMatches());
		normal.add(new FinalMatchLevel());
		normal.add(new MatchesPlayed());
	}

	public static Statistic getNormal(String name) {
		for (Statistic s : getInstance().normal) {
			for (String n : s.getNames()) {
				if (n.equalsIgnoreCase(name)) {
					return s;
				}
			}
		}

		return null;
	}

	public static GlobalStatistic getGlobal(String name) {
		for (GlobalStatistic s : getInstance().global) {
			for (String n : s.getNames()) {
				if (n.equalsIgnoreCase(name)) {
					return s;
				}
			}
		}

		return null;
	}
	
	private static List<Event> getEvents(String filter, List<Event> events) {
		List<Event> ret = new ArrayList<Event>();
		
		if (filter.equalsIgnoreCase("[all]")) {
			ret.addAll(events);
		} else {
			for (String s : ListUtil.safeSplit(filter, ",")) {
				s = s.trim();
				
				for (Event e : events) {
					if (e.getShortName().equalsIgnoreCase(s)) {
						ret.add(e);
					}
				}
			}
		}
		
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		Database db = new Database();
		
		System.out.print("[Info ] Initializing team cache... ");
		List<Team> dbTeams = db.getTeams();
		
		List<Team> teams = new ArrayList<Team>();
		teams.addAll(db.getTeams());
		System.out.println("done.");
		
		System.out.print("[Info ] Initializing event cache... ");
		List<Event> dbEvents = db.getEvents();
		
		List<Event> events = new ArrayList<Event>();
		events.addAll(db.getEvents());
		System.out.println("done.");
		
		List<Game> games = new ArrayList<Game>();
		for (Event e : events) {
			games.addAll(e.getGames());
		}
		
		while (true) {
			String input = readLine("Statistic");
			
			if (input.equalsIgnoreCase("quit")
					|| input.equalsIgnoreCase("exit")) {
				return;
			}
			
			GlobalStatistic gstat = getGlobal(input);
			if (gstat != null) {
				localExecuteGlobal(gstat, games, teams, db);
				System.out.println();
				continue;
			}
			
			Statistic stat = getNormal(input);
			if (stat != null) {
				localExecuteNormal(stat, events, teams, db);
				System.out.println();
				continue;
			}
			
			System.err.println("[Error] Statistic not found: " + input);
		}
	}
	
	private static void localExecuteGlobal(GlobalStatistic stat,
			List<Game> games, List<Team> teams, Database db) {
		stat.calculate(games, teams, db);
	}
	
	private static void localExecuteNormal(Statistic stat, List<Event> events, 
			List<Team> teams, Database db) throws IOException {
		List<Event> eventsFiltered = null;
		List<Game> games = null;
		
		List<String> regex = null;
		String input;
		
		while (eventsFiltered == null || eventsFiltered.isEmpty()) {
			input = readLine("Event name");
			
			regex = ListUtil.extract("(\\S+)", input);
			if (regex != null) {
				eventsFiltered = getEvents(regex.get(1), events);
				
				if (eventsFiltered == null || eventsFiltered.isEmpty()) {
					System.err.println("[Error] Event not found.");
				}
				continue;
			}
			
			regex = ListUtil.extract("(\\S+) (\\d+)", input);
			if (regex != null) {
				events = getEvents(regex.get(1), events);
				
				if (eventsFiltered == null || eventsFiltered.isEmpty()) {
					System.err.println("[Error] Event not found");
					continue;
				}
				
				int year = Integer.parseInt(regex.get(2));
				games = new ArrayList<Game>();
				
				for (Event e : eventsFiltered) {
					games.add(e.getGame(year));
				}
				continue;
			}
			
			System.err.println("[Error] Invalid syntax; use "
					+ "'<event short name>' or '<short name> <year>'");
		}
		
		while (games == null) {
			input = readLine("Game year");
			
			try {
				int year = Integer.parseInt(input);
				
				games = new ArrayList<Game>();
				for (Event e : eventsFiltered) {
					for (Game g : e.getGames()) {
						if (g.getGameYear() == year) {
							games.add(g);
						}
					}
				}
			} catch (NumberFormatException ex) {
				System.err.println("[Error] Invalid year ");
			}
		}
		
		System.out.println("[Info ] Running stat " 
				+ stat.getClass().getSimpleName() + " on " + games.size() 
				+ " game(s)");
		for (Game g : games) {
			stat.calculate(g, teams, db);
		}
	}
	
	private static String readLine(String text) throws IOException {
		System.out.print("[Input] " + text + ": ");
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		return br.readLine();
	}

}
