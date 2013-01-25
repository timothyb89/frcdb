package net.frcdb.stats.calc;

import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class TeamEntryUpdater implements GameStatistic {

	private Logger logger = LoggerFactory.getLogger(TeamEntryUpdater.class);
	
	@Override
	public String[] getNames() {
		return new String[] {
			"teamentryupdater",
			"teamentryupdate",
			"update-teamentries",
			"update-team-entries"
		};
	}
	
	@Override
	public void calculate(Game game) {
		try {
			game.updateTeams();
			
			Database.save().entity(game).now();
		} catch (Exception ex) {
			logger.error("Error fetching teams", ex);
		}
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
