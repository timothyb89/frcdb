package net.frcdb.stats.mining;

import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import net.frcdb.util.Sources;

/**
 * Fixes incorrect data URLs for games.
 * @author tim
 */
public class FixGameURLs {

	public static void main(String[] args) {
		Database db = new Database();
		
		for (Game g : db.getGames()) {
			g.setAwardsURL(Sources.getAwardsURL(g));
			g.setResultsURL(Sources.getResultsURL(g));			
			g.setStandingsURL(Sources.getStandingsURL(g));
			
			System.out.println("Fixed: " + g);
			db.store(g);
		}
	}
	
}
