package net.frcdb.api.game.team;

import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.game.team.element.Seeding2012Provider;
import net.frcdb.api.team.Team;

/**
 *
 * @author tim
 */
@EntitySubclass
public class AscentTeamEntry extends TeamEntry
		implements OPRProvider, Seeding2012Provider {
	
	private double opr;
	private double oprZ;
	private double dpr;
	
	private float qualificationScore;
	private float hybridPoints;
	private float bridgePoints;
	private float teleopPoints;
	private int coopertitionPoints;
	
	private int disqualifications;

	public AscentTeamEntry() {
	}

	public AscentTeamEntry(Game game, Team team) {
		super(game, team);
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
