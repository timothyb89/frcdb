package net.frcdb.api.game.standing;

import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.Seeding2012Provider;

/**
 *
 * @author tim
 */
@EntitySubclass
public class ReboundStanding extends Standing implements Seeding2012Provider {
	
	private int wins;
	private int losses;
	private int ties;
	
	private float qualificationScore;
	private float hybridPoints;
	private float bridgePoints;
	private float teleopPoints;
	private int coopertitionPoints;
	
	private int disqualifications;

	public ReboundStanding() {
	}

	public ReboundStanding(Game game) {
		super(game);
	}

	public ReboundStanding(
			Game game, int rank, TeamEntry team, int matchesPlayed) {
		super(game, rank, team, matchesPlayed);
	}
	
	@Override
	public int getWins() {
		return wins;
	}

	@Override
	public void setWins(int wins) {
		this.wins = wins;
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
	public int getTies() {
		return ties;
	}

	@Override
	public void setTies(int ties) {
		this.ties = ties;
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
	public float getHybridPoints() {
		return hybridPoints;
	}

	@Override
	public void setHybridPoints(float hybridPoints) {
		this.hybridPoints = hybridPoints;
	}

	@Override
	public float getBridgePoints() {
		return bridgePoints;
	}

	@Override
	public void setBridgePoints(float bridgePoints) {
		this.bridgePoints = bridgePoints;
	}

	@Override
	public float getTeleopPoints() {
		return teleopPoints;
	}

	@Override
	public void setTeleopPoints(float teleopPoints) {
		this.teleopPoints = teleopPoints;
	}

	@Override
	public int getCoopertitionPoints() {
		return coopertitionPoints;
	}

	@Override
	public void setCoopertitionPoints(int coopertitionPoints) {
		this.coopertitionPoints = coopertitionPoints;
	}

	@Override
	public int getDisqualifications() {
		return disqualifications;
	}

	@Override
	public void setDisqualifications(int disqualifications) {
		this.disqualifications = disqualifications;
	}
	
}
