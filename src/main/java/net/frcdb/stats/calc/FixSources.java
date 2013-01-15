package net.frcdb.stats.calc;

import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import net.frcdb.util.Sources;

/**
 *
 * @author tim
 */
public class FixSources implements GameStatistic {

	@Override
	public String[] getNames() {
		return new String[] { "fixsources", "sources" };
	}
	
	@Override
	public void calculate(Game game) {
		game.setResultsURL(Sources.getResultsURL(game));
		game.setStandingsURL(Sources.getStandingsURL(game));
		game.setAwardsURL(Sources.getAwardsURL(game));
		
		Database.save().entity(game);
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
