package net.frcdb.stats.chart.api;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
@EntitySubclass
public abstract class GameChart extends Chart {

	/**
	 * A key to the game this chart sources data from. This should only be
	 * loaded during generate() to make sure fetches via json are fast.
	 */
	private Key<Game> game;

	public GameChart(Game game) {
		this.game = Key.create(game);
	}

	public GameChart() {
	}
	
	public Key<Game> getGameKey() {
		return game;
	}
	
	public Game getGame() {
		return Database.ofy().load().key(game).get();
	}
	
	public void setGame(Game game) {
		this.game = Key.create(game);
	}

}
