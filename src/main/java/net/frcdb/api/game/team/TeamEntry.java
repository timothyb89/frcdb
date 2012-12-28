package net.frcdb.api.game.team;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.MatchType;
import net.frcdb.api.team.Team;

/**
 *
 * @author tim
 */
@Entity
public class TeamEntry {
	
	@Id
	private Long id;
	
	@Parent
	private Ref<Game> game;
	
	@Index
	private int gameYear;
	
	@Load
	private Ref<Team> team;
	
	@Index
	private int rank;
	private int matchesPlayed;
	
	@Index private int wins;
	@Index private int losses;
	@Index private int ties;
	
	@Index
	private MatchType finalMatchLevel;

	public TeamEntry() {
	}

	public TeamEntry(Game game, Team team) {
		this.game = Ref.create(game);
		this.team = Ref.create(team);
		
		gameYear = game.getGameYear();
	}

	public Ref<Game> getGame() {
		return game;
	}

	public int getGameYear() {
		return gameYear;
	}
	
	public Team getTeam() {
		return team.get();
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getTies() {
		return ties;
	}

	public void setTies(int ties) {
		this.ties = ties;
	}

	public MatchType getFinalMatchLevel() {
		return finalMatchLevel;
	}

	public void setFinalMatchLevel(MatchType finalMatchLevel) {
		this.finalMatchLevel = finalMatchLevel;
	}

}
