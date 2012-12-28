package net.frcdb.api.game.team;

import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.game.team.element.Seeding2007Provider;
import net.frcdb.api.team.Team;

/**
 *
 * @author tim
 */
@EntitySubclass
public class LogomotionTeamEntry extends TeamEntry
		implements Seeding2007Provider, OPRProvider {
	
	private float qualificationScore;
	private float rankingScore;
	
	private double opr;
	private double oprZ;
	private double dpr;

	public LogomotionTeamEntry() {
	}

	public LogomotionTeamEntry(Game game, Team team) {
		super(game, team);
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
