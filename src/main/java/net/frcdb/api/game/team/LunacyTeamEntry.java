package net.frcdb.api.game.team;

import com.googlecode.objectify.annotation.EntitySubclass;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.element.MatchPointsProvider;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.game.team.element.Seeding2007Provider;
import net.frcdb.api.team.Team;

/**
 * TODO: actually get the data...
 * @author tim
 */
@EntitySubclass
public class LunacyTeamEntry extends TeamEntry implements
		OPRProvider, Seeding2007Provider, MatchPointsProvider {
	
	private double opr;
	private double oprZ;
	private double dpr;
	
	private float qualificationScore;
	private float rankingScore;
	private int matchPoints;

	public LunacyTeamEntry() {
	}
	
	public LunacyTeamEntry(Game game, Team team) {
		super(game, team);
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
	public double getDPR() {
		return dpr;
	}

	@Override
	public void setDPR(double dpr) {
		this.dpr = dpr;
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
	public int getMatchPoints() {
		return matchPoints;
	}

	@Override
	public void setMatchPoints(int matchPoints) {
		this.matchPoints = matchPoints;
	}

}
