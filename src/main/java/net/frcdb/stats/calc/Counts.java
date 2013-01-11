package net.frcdb.stats.calc;

import net.frcdb.api.event.EventRoot;
import net.frcdb.api.team.TeamRoot;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class Counts implements GlobalStatistic {

	@Override
	public String[] getNames() {
		return new String[] { "counts" };
	}
	
	@Override
	public void calculate() {
		TeamRoot tr = TeamRoot.get();
		tr.setCount(Database.getInstance().countTeams());
		
		EventRoot er = EventRoot.get();
		er.setEventCount(Database.getInstance().countEvents());
		er.setGameCount(Database.getInstance().countGames());
		
		Database.save().entities(tr, er);
	}
	
}
