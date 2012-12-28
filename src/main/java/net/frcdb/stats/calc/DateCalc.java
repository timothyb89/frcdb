package net.frcdb.stats.calc;

import java.util.Date;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.util.DateUtil;

/**
 * Not really a 'calculation', but whatever.
 * This fixes the dates on events so we can sort then and whatnot.
 * @author tim
 */
public class DateCalc implements GlobalStatistic {

	public String[] getNames() {
		return new String[] {"DateCalc", "date"};
	}

	public void calculate(List<Game> games, List<Team> teams, Database db) {
		for (Game g : games) {
			Date[] dates = DateUtil.parseEventDateRange(g.getDate());

			g.setStartDate(dates[0]);
			g.setEndDate(dates[1]);
			db.store(g);
		}
	}

}
