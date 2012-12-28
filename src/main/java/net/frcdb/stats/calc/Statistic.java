package net.frcdb.stats.calc;

import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public interface Statistic {

	/**
	 * Gets the possible names for this statistic. The formal name for this
	 * statistic should be at index zero.
	 * @return an arrays of strings containing possible names for this statistic
	 */
	public String[] getNames();

	/**
	 * Runs this statistic
	 * @param game The game to apply the statistic to
	 * @param teams The cached list of teams to operate on (global list, get
	 *			per-game teams through <code>Game.getTeams()</code>)
	 * @param db The database connection
	 */
	public void calculate(Game game, List<Team> teams, Database db);

}
