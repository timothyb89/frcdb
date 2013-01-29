package net.frcdb.stats.chart.api;

import com.googlecode.objectify.annotation.EntitySubclass;
import java.util.Collection;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;

/**
 * Defines a chart based on all games in a particular year.
 * @author tim
 */
@EntitySubclass
public abstract class YearChart extends Chart {

	private int year;
	
	public YearChart() {
		
	}

	public YearChart(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public Collection<Game> getGames() {
		return Database.getInstance().getGames(year);
	}
	
}
