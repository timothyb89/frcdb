package net.frcdb.stats.mining.y2012;

import java.io.IOException;
import java.util.List;
import net.frcdb.api.award.Award;
import net.frcdb.api.game.event.Rebound2012;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.stats.mining.EventMining;

/**
 *
 * @author tim
 */
public class ReboundMiner implements EventMining<Rebound2012> {

	@Override
	public List<Award> getAwards(Rebound2012 game) throws IOException {
		// todo
		return null;
	}

	@Override
	public List<Standing> getStandings(Rebound2012 game) throws IOException {
		return new ReboundStandings(game).parse();
	}

	@Override
	public List<Match> getMatches(Rebound2012 game) throws IOException {
		return new ReboundMatchResults(game).parse();
	}

	@Override
	public List<TeamEntry> getTeams(Rebound2012 game) throws IOException {
		return new ReboundTeams(game).parse();
	}	
	
}
