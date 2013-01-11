package net.frcdb.stats.calc;

import java.io.IOException;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class TeamsFromMatches implements Statistic {

	public String[] getNames() {
		return new String[] {"teams"};
	}

	public void calculate(Game game, List<Team> teams, Database db) {
		try {
			game.updateTeamsFromMatches(db);
		} catch (IOException ex) {
			System.err.println("[Error] Failed to update teams list: "
					+ ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public String getBackendName() {
		return null; // probably want something here
	}

}
