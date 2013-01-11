package net.frcdb.stats.calc;

import java.util.Date;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import net.frcdb.util.DateUtil;

/**
 * Not really a 'calculation', but whatever.
 * This fixes the dates on events so we can sort then and whatnot.
 * @author tim
 */
public class DateCalc implements GlobalStatistic {

	@Override
	public String[] getNames() {
		return new String[] {"DateCalc", "date"};
	}

	@Override
	public void calculate() {
		for (Game g : Database.getInstance().getGames()) {
			Date[] dates = DateUtil.parseEventDateRange(g.getDate());

			g.setStartDate(dates[0]);
			g.setEndDate(dates[1]);
			Database.save().entity(g);
		}
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
