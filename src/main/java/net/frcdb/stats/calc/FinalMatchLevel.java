package net.frcdb.stats.calc;

import java.util.HashMap;
import java.util.Map;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.match.MatchType;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class FinalMatchLevel implements GameStatistic {

	private Logger logger = LoggerFactory.getLogger(FinalMatchLevel.class);
	
	@Override
	public String[] getNames() {
		return new String[] {"finalmatchlevel", "matchlevel"};
	}

	@Override
	public void calculate(Game game) {
		Map<Team, MatchType> levels = new HashMap<Team, MatchType>();
		
		for (Match m : game.getAllMatches()) {
			for (Team t : m.getTeams()) {
				MatchType currentHighest = levels.get(t);
				if (currentHighest == null) { // no current value
					currentHighest = MatchType.QUALIFICATION;
				}
				
				MatchType newHighest = currentHighest;
				
				switch (currentHighest) {
					case QUALIFICATION:
						switch (m.getType()) {
							case QUALIFICATION: break; // no change
							case QUARTERFINAL: // all greater
							case SEMIFINAL: // change
							case FINAL:
								newHighest = m.getType();
								break;
						}
					case QUARTERFINAL:
						switch (m.getType()) {
							case QUALIFICATION: break; // no change
							case QUARTERFINAL: break; // no change
							case SEMIFINAL: // greater
							case FINAL:
								newHighest = m.getType();
								break;
						}
					case SEMIFINAL:
						switch (m.getType()) {
							case QUALIFICATION: break; // no change
							case QUARTERFINAL: break; // no change
							case SEMIFINAL: break; // no change
							case FINAL: // greater
								newHighest = m.getType();
								break;
						}
					case FINAL: break; // no change
				}
				
				levels.put(t, newHighest);
			}
		}
		
		for (Team t : levels.keySet()) {
			TeamEntry entry = game.getEntry(t);
			if (entry == null) {
				logger.warn("No entry exists for team " + t + " at " + game);
				continue;
			}
			
			entry.setFinalMatchLevel(MatchType.clone(levels.get(t)));
			Database.save().entity(entry);
		}
	}

	@Override
	public String getBackendName() {
		return null;
	}

}
