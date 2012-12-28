package net.frcdb.api.game.standing;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;


/**
 *
 * @author tim
 */
@Entity
public class Standing implements Comparable<Standing> {

	@Id
	private Long id;
	
	@Parent
	private Ref<Game> game;
	
	@Index
	private int gameYear;
	
	@Index private int rank;
	@Load private Ref<TeamEntry> team;
	private int matchesPlayed;

	public Standing() {
	}

	public Standing(Game game) {
		this.game = Ref.create(game);
		
		gameYear = game.getGameYear();
	}
	
	public Standing(Game game, int rank, TeamEntry team, int matchesPlayed) {
		this.game = Ref.create(game);
		this.team = Ref.create(team);
		
		this.rank = rank;
		this.matchesPlayed = matchesPlayed;
		
		gameYear = game.getGameYear();
	}
	
	public Ref<Game> getGame() {
		return game;
	}

	public int getGameYear() {
		return gameYear;
	}
	
	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public TeamEntry getTeam() {
		return team.get();
	}
	
	public Ref<TeamEntry> getTeamReference() {
		return team;
	}
	
	public void setTeam(TeamEntry team) {
		this.team = Ref.create(team);
	}

	@Override
	public int compareTo(Standing o) {
		if (getRank() > o.getRank()) {
			return 1;
		} else if (getRank() < o.getRank()) {
			return -1;
		} else {
			return 0;
		}
	}

}
