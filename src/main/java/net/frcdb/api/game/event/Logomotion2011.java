package net.frcdb.api.game.event;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Load;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.element.GameOPRProvider;
import net.frcdb.api.game.event.element.OPRStatistics;
import net.frcdb.api.game.standing.LogomotionStanding;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.game.team.LogomotionTeamEntry;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.game.team.element.Seeding2007Provider;
import net.frcdb.api.team.Team;
import net.frcdb.stats.calc.OPRDPRCalc;
import net.frcdb.stats.calc.Statistic;
import net.frcdb.stats.mining.EventMining;

/**
 *
 * @author tim
 */
@EntitySubclass
public class Logomotion2011 extends Game implements GameOPRProvider {

	public static final Class[] GAME_PROPERTIES = {
		GameOPRProvider.class
	};
	
	public static final Class[] TEAM_PROPERTIES = {
		OPRProvider.class,
		Seeding2007Provider.class,
	};
	
	public static final Class[] STANDING_PROPERTIES = {
		Seeding2007Provider.class
	};

	public static final Statistic[] STATISTICS = {
		new OPRDPRCalc(), // TODO: More statistics here!
	};
	
	private double averageOPR;
	private double averageDPR;
	
	private double totalOPR;
	private double totalDPR;
	
	private double highestOPR;
	@Load private Ref<TeamEntry> highestOPRTeam;
	
	private double lowestOPR;
	@Load private Ref<TeamEntry> lowestOPRTeam;

	@Embed
	private OPRStatistics oprStatistics;
	
	public Logomotion2011() {
	}

	public Logomotion2011(Event event) {
		super(event);
	}
	
	@Override
	public int getGameYear() {
		return 2011;
	}

	@Override
	public String getGameName() {
		return "Logomotion";
	}

	@Override
	public EventMining getMiner() {
		return null;
	}

	@Override
	public TeamEntry createEntry(Team team) {
		return new LogomotionTeamEntry(this, team);
	}

	@Override
	public Standing createStanding() {
		return new LogomotionStanding(this);
	}

	@Override
	public Class[] getGameProperties() {
		return GAME_PROPERTIES;
	}

	@Override
	public Class[] getTeamProperties() {
		return TEAM_PROPERTIES;
	}

	@Override
	public Class[] getStandingProperties() {
		return STANDING_PROPERTIES;
	}
	
	@Override
	public Statistic[] getStatistics() {
		return STATISTICS;
	}

	@Override
	public double getAverageOPR() {
		return averageOPR;
	}

	@Override
	public void setAverageOPR(double averageOPR) {
		this.averageOPR = averageOPR;
	}

	@Override
	public double getAverageDPR() {
		return averageDPR;
	}

	@Override
	public void setAverageDPR(double averageDPR) {
		this.averageDPR = averageDPR;
	}

	@Override
	public double getTotalOPR() {
		return totalOPR;
	}

	@Override
	public void setTotalOPR(double totalOPR) {
		this.totalOPR = totalOPR;
	}

	@Override
	public double getTotalDPR() {
		return totalDPR;
	}

	@Override
	public void setTotalDPR(double totalDPR) {
		this.totalDPR = totalDPR;
	}
	
	@Override
	public double getHighestOPR() {
		return highestOPR;
	}

	@Override
	public void setHighestOPR(double highestOPR) {
		this.highestOPR = highestOPR;
	}

	@Override
	public TeamEntry getHighestOPRTeam() {
		if (highestOPRTeam == null) {
			return null;
		}
		
		return highestOPRTeam.get();
	}

	@Override
	public void setHighestOPRTeam(TeamEntry highestOPRTeam) {
		this.highestOPRTeam = Ref.create(highestOPRTeam);
	}

	@Override
	public double getLowestOPR() {
		return lowestOPR;
	}

	@Override
	public void setLowestOPR(double lowestOPR) {
		this.lowestOPR = lowestOPR;
	}

	@Override
	public TeamEntry getLowestOPRTeam() {
		if (lowestOPRTeam == null) {
			return null;
		}
		
		return lowestOPRTeam.get();
	}

	@Override
	public void setLowestOPRTeam(TeamEntry lowestOPRTeam) {
		this.lowestOPRTeam = Ref.create(lowestOPRTeam);
	}

	@Override
	public OPRStatistics getOPRStatistics() {
		return oprStatistics;
	}

	@Override
	public void setOPRStatistics(OPRStatistics oprStatistics) {
		this.oprStatistics = oprStatistics;
	}
	
	@Override
	public void standingParseExtra(TeamEntry entry, Standing standing) {
		// set some shared properties for TeamEntries/Standings
		LogomotionTeamEntry e = (LogomotionTeamEntry) entry;
		LogomotionStanding s = (LogomotionStanding) standing;
		
		e.setWins(s.getWins());
		e.setLosses(s.getLosses());
		e.setTies(s.getTies());
		e.setQualificationScore(s.getQualificationScore());
		e.setRankingScore(s.getRankingScore());
	}

}
