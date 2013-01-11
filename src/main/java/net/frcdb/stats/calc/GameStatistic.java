package net.frcdb.stats.calc;

import net.frcdb.api.game.event.Game;

/**
 *
 * @author tim
 */
public interface GameStatistic extends Statistic {
	
	public void calculate(Game game);
	
}
