package net.frcdb.api.game.event;

import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Id;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.element.GameOPRProvider;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.game.team.LunacyTeamEntry;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.MatchPointsProvider;
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
public class Lunacy2009 extends Game {

	public static final Class[] GAME_PROPERTIES = {
		GameOPRProvider.class
	};
	
	public static final Class[] TEAM_PROPERTIES = {
		OPRProvider.class,
		Seeding2007Provider.class,
	};
	
	public static final Class[] STANDING_PROPERTIES = {
		Seeding2007Provider.class,
		MatchPointsProvider.class
	};

	public static final Statistic[] STATISTICS = {
		new OPRDPRCalc(), // TODO: More statistics here!
	};

	public Lunacy2009() {
	}
	
	public Lunacy2009(Event event) {
		super(event);
	}
	
	@Override
	public int getGameYear() {
		return 2009;
	}

	@Override
	public String getGameName() {
		return "Lunacy";
	}

	@Override
	public EventMining getMiner() {
		return null;
	}

	@Override
	public TeamEntry createEntry(Team team) {
		return new LunacyTeamEntry(this, team);
	}

	@Override
	public Standing createStanding() {
		// TODO: Unimplemented and will cause errors!
		return null;
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

}
