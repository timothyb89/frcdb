package net.frcdb.stats.calc;

import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;

/**
 * Sets values for gameYear and teamNumber in TeamEntries (for performance 
 * reasons)
 * @author tim
 */
public class ExtraTeamInfo implements GlobalStatistic {

	public String[] getNames() {
		return new String[] {"extraInfo", "teamNumbers", "gameYears"};
	}

	public void calculate(List<Game> games, List<Team> teams, Database db) {
		//for (TeamEntry e : db.getDB().query(TeamEntry.class)) {
		//	e.setTeamNumber(e.getTeam().getNumber());
		//	e.setGameYear(e.getGame().getGameYear());
		//	db.store(e);
		//}
	}

}
