package net.frcdb.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;

/**
 * Fixes game years.
 * @author tim
 */
public class FixDates {
	
	public static void main(String[] args) {
		for (Game g : Database.getInstance().getGames()) {
			g.setStartDate(setYear(g.getStartDate(), g.getGameYear()));
			
			g.setEndDate(setYear(g.getEndDate(), g.getGameYear()));
			g.setEndDate(fixEndDate(g.getEndDate()));
			
			Database.getInstance().store(g);
			
			System.out.println("fixed: " + g);
		}
	}
	
	private static Date setYear(Date d, int year) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(d);
		
		c.set(Calendar.YEAR, year);
		
		return c.getTime();
	}
	
	/**
	 * End dates should be at the end of the day
	 * @param date 
	 */
	public static Date fixEndDate(Date date) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(date);
		
		c.set(Calendar.HOUR_OF_DAY, 23);
		
		return c.getTime();
	}
	
}
