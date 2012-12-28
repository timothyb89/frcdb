package net.frcdb.util;

import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;

/**
 * Utilities for quick access to the latest FIRST URLs.
 * @author tim
 */
public class Sources {

	public static final String BASE_URL = 
			"http://www2.usfirst.org/$YEARcomp/events/";
	
	public static final String STANDINGS_FILE = "/rankings.html";
	public static final String RESULTS_FILE = "/matchresults.html";
	public static final String AWARDS_FILE = "/awards.html";
	
	public static String getStandingsURL(Game game) {
		// probably bad
		Event e = game.getEvent();
		String base = BASE_URL.replace(
				"$YEAR", 
				String.valueOf(game.getGameYear()));
		
		return base + e.getIdentifier() + STANDINGS_FILE;
	}
	
	public static String getStandingsURL(String eventId, int year) {
		String base = BASE_URL.replace(
				"$YEAR", 
				String.valueOf(year));
		
		return base + eventId + STANDINGS_FILE;
	}
	
	public static String getResultsURL(Game game) {
		Event e = game.getEvent();
		String base = BASE_URL.replace(
				"$YEAR", 
				String.valueOf(game.getGameYear()));
		
		return base + e.getIdentifier() + RESULTS_FILE;
	}
	
	public static String getResultsURL(String eventId, int year) {
		String base = BASE_URL.replace(
				"$YEAR", 
				String.valueOf(year));
		
		return base + eventId + RESULTS_FILE;
	}
	
	public static String getAwardsURL(Game game) {
		Event e = game.getEvent();
		String base = BASE_URL.replace(
				"$YEAR", 
				String.valueOf(game.getGameYear()));
		
		return base + e.getIdentifier() + AWARDS_FILE;
	}
	
	public static String getAwardsURL(String eventId, int year) {
		String base = BASE_URL.replace(
				"$YEAR", 
				String.valueOf(year));
		
		return base + eventId + AWARDS_FILE;
	}
	
}
