package net.frcdb.stats.mining.y2013;

import java.io.IOException;
import java.util.List;
import net.frcdb.api.award.Award;
import net.frcdb.api.game.event.Ascent2013;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.stats.mining.EventMining;

/**
 *
 * @author tim
 */
public class AscentMiner implements EventMining<Ascent2013> {

	@Override
	public List<Award> getAwards(Ascent2013 game) throws IOException {
		return null;
	}

	@Override
	public List<Standing> getStandings(Ascent2013 game) throws IOException {
		return new AscentStandings(game).parse();
	}

	@Override
	public List<Match> getMatches(Ascent2013 game) throws IOException {
		return new AscentMatchResults(game).parse();
	}

	@Override
	public List<TeamEntry> getTeams(Ascent2013 game) throws IOException {
		return new AscentTeams(game).parse();
	}
	
}
