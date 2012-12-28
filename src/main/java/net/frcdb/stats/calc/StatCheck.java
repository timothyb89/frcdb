package net.frcdb.stats.calc;

import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class StatCheck {

	public static void main(String[] args) {
		Database db = new Database();
		
		System.out.println("Games:");
		for (Game g : db.getGames()) {
			System.out.println("\t" + g);
		}
		
		System.out.println("Events:");
		for (Event e : db.getEvents()) {
			System.out.println("\t" + e.getName());
			
			for (Game g: e.getGames()) {
				System.out.println("\t\t" + g.getGameName() + " " + g.getGameYear());
			}
		}
	}
	
}
