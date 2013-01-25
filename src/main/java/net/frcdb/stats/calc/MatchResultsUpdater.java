package net.frcdb.stats.calc;

import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class MatchResultsUpdater implements GameStatistic {

	private Logger logger = LoggerFactory.getLogger(MatchResultsUpdater.class);
	
	@Override
	public String[] getNames() {
		return new String[] {
			"MatchResultsUpdater",
			"resultsupdate",
			"update-results"
		};
	}
	
	@Override
	public void calculate(Game game) {
		try {
			game.updateResults();
			
			Database.save().entity(game).now();
		} catch (Exception ex) {
			logger.error("Update failed", ex);
		}
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
