package net.frcdb.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.db.Database;

/**
 * Because of some structural changes we need to reset all of the team entries.
 * This should theoretically wipe out all standings and team entry data, and
 * reinitialize the TeamEntries from the match results if available.
 * 
 * 2010/past: Reinit from match results automatically (needed anyway)
 * 2011/current: Reinit separately with EventMiner
 * @author tim
 */
public class ResetTeamEntries {
	
	public static void main(String[] args) throws IOException {
		Database db = new Database();
		
		// delete all team entries! D:
		List<TeamEntry> copy = new ArrayList<TeamEntry>();
		
		
		// copy.addAll(db.getDB().query(TeamEntry.class));
		// TODO: Fixme
		for (TeamEntry e : copy) {
			db.delete(e);
		}
		
		for (Game g : db.getGames()) {
			g.getTeams().clear();
			
			if (g.getGameYear() != Event.CURRENT_YEAR) {
				// reinit from match results
				System.out.println("[Info ] Reinitializing teamlist for " + g);
				g.updateTeamsFromMatches(db);
			} else {
				System.out.println("[Info ] Skipping reinit for " + g);
			}
			
			db.store(g);
		}
	}
	
}
