package net.frcdb.api.game.team;

import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.element.HangingProvider;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.game.team.element.Seeding2010Provider;
import net.frcdb.api.team.Team;

/**
 *
 * @author tim
 */
@EntitySubclass
public class BreakawayTeamEntry extends TeamEntry 
		implements OPRProvider, HangingProvider, Seeding2010Provider {
	
	private float seedingScore;
	private float coopertitionBonus;
	private float hangingPoints;
	
	private double opr;
	private double oprZ;
	private double dpr;

	public BreakawayTeamEntry() {
		
	}

	public BreakawayTeamEntry(Game game, Team team) {
		super(game, team);
	}

	@Override
	public float getSeedingScore() {
		return seedingScore;
	}

	@Override
	public void setSeedingScore(float seedingScore) {
		this.seedingScore = seedingScore;
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
	public double getDPR() {
		return dpr;
	}

	@Override
	public void setDPR(double dpr) {
		this.dpr = dpr;
	}

	@Override
	public double getOPR() {
		return opr;
	}

	@Override
	public void setOPR(double opr) {
		this.opr = opr;
	}
	
	@Override
	public double getOPRZ() {
		return oprZ;
	}

	@Override
	public void setOPRZ(double oprZ) {
		this.oprZ = oprZ;
	}
	
}
