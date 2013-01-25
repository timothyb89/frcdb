package net.frcdb.stats.calc;

import net.frcdb.api.game.event.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class StandingsUpdater implements GameStatistic {

	private Logger logger = LoggerFactory.getLogger(StandingsUpdater.class);
	
	@Override
	public String[] getNames() {
		return new String[] {
			"standingsupdater",
			"standingsupdate",
			"update-standings"
		};
	}
	
	@Override
	public void calculate(Game game) {
		try {
			game.updateStandings();
		} catch (Exception ex) {
			logger.error("Standings update failed", ex);
		}
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
