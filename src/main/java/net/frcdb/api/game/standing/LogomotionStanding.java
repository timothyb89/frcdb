package net.frcdb.api.game.standing;

import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.Seeding2007Provider;

/**
 *
 * @author tim
 */
@EntitySubclass
public class LogomotionStanding extends Standing implements Seeding2007Provider {
	
	private int wins;
	private int losses;
	private int ties;
	
	private float qualificationScore;
	private float rankingScore;

	public LogomotionStanding() {
	}

	public LogomotionStanding(Game game) {
		super(game);
	}

	public LogomotionStanding(
			Game game, int rank, TeamEntry team, int matchesPlayed) {
		super(game, rank, team, matchesPlayed);
	}

	@Override
	public int getLosses() {
		return losses;
	}

	@Override
	public void setLosses(int losses) {
		this.losses = losses;
	}

	@Override
	public float getQualificationScore() {
		return qualificationScore;
	}

	@Override
	public void setQualificationScore(float qualificationScore) {
		this.qualificationScore = qualificationScore;
	}

	@Override
	public float getRankingScore() {
		return rankingScore;
	}

	@Override
	public void setRankingScore(float rankingScore) {
		this.rankingScore = rankingScore;
	}

	@Override
	public int getTies() {
		return ties;
	}

	@Override
	public void setTies(int ties) {
		this.ties = ties;
	}

	@Override
	public int getWins() {
		return wins;
	}

	@Override
	public void setWins(int wins) {
		this.wins = wins;
	}
	
}
