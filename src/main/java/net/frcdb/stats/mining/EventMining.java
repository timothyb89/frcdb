package net.frcdb.stats.mining;

import java.io.IOException;
import java.util.List;
import net.frcdb.api.award.Award;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public interface EventMining<T extends Game> {

	public List<Award> getAwards(T game, Database db) throws IOException;
	public List<Standing> getStandings(T game, Database db) throws IOException;
	public List<Match> getMatches(T game, Database db) throws IOException;

}
