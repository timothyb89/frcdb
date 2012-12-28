package net.frcdb.stats.calc;

import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class TeamActivity implements GlobalStatistic {

	public String[] getNames() {
		return new String[] {"teamactivity", "activity"};
	}

	public void calculate(List<Game> games, List<Team> teams, Database db) {
		for (Team t : teams) {
			
		}
	}

}
