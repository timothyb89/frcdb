package net.frcdb.api.game.standing;

import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.HangingProvider;
import net.frcdb.api.game.team.element.Seeding2010Provider;

/**
 *
 * @author tim
 */
@EntitySubclass
public class BreakawayStanding extends Standing implements Seeding2010Provider,
		HangingProvider {
	
	private float seedingScore;
	private float coopertitionBonus;
	private float hangingPoints;

	public BreakawayStanding() {
	}

	public BreakawayStanding(Game game) {
		super(game);
	}

	public BreakawayStanding(
			Game game, int rank, TeamEntry team, int matchesPlayed) {
		super(game, rank, team, matchesPlayed);
	}
	
	@Override
	public float getCoopertitionBonus() {
		return coopertitionBonus;
	}

	@Override
	public void setCoopertitionBonus(float coopertitionBonus) {
		this.coopertitionBonus = coopertitionBonus;
	}

	@Override
	public float getHangingPoints() {
		return hangingPoints;
	}

	@Override
	public void setHangingPoints(float hangingPoints) {
		this.hangingPoints = hangingPoints;
	}

	@Override
	public float getSeedingScore() {
		return seedingScore;
	}

	@Override
	public void setSeedingScore(float seedingScore) {
		this.seedingScore = seedingScore;
	}
	
}
