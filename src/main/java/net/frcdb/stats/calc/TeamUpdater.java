package net.frcdb.stats.calc;

import java.io.IOException;
import net.frcdb.stats.mining.TeamMiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class TeamUpdater implements GlobalStatistic {

	private Logger logger = LoggerFactory.getLogger(TeamUpdater.class);
	
	@Override
	public String[] getNames() {
		return new String[] { "teamupdater", "update-teams", "teamupdate" };
	}
	
	@Override
	public void calculate() {
		try {
			new TeamMiner();
		} catch (IOException ex) {
			logger.error("Team update failed", ex);
		}
	}

	@Override
	public String getBackendName() {
		return "stat-compute";
	}
	
}
