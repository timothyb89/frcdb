package net.frcdb.stats.calc;

import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class GameYearIndexes implements GameStatistic {

	@Override
	public String[] getNames() {
		return new String[] { "gameyearindexes" };
	}
	
	@Override
	public void calculate(Game game) {
		game.setGameYearIndex(game.getGameYear());
		Database.getInstance().store(game);
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
