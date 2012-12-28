package net.frcdb.stats.calc;

import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;

/**
 * A statistic that should apply to all games for a given year
 * @author tim
 */
public interface GlobalStatistic {

	/**
	 * Gets the possible names for this statistic
	 * @return an arrays of strings containing possible names for this statistic
	 */
	public String[] getNames();

	/**
	 * Applies this statistic to the given data.
	 * @param games The cached list of games
	 * @param teams The cached list of teams
	 * @param db The database to store to
	 */
	public void calculate(List<Game> games, List<Team> teams, Database db);

}
