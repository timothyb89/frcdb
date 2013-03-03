package net.frcdb.servlet.bean;

import java.util.Collection;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.stats.chart.EventTimeline;

/**
 *
 * @author tim
 */
public class IndexData {
	
	private int teamCount;
	private int eventCount;
	private int gameCount;
	
	private Collection<Event> topEvents;
	private Collection<Team> topTeams;
	
	private Collection<Game> latestGames;
	private Collection<Game> upcomingGames;
	private Collection<Game> currentGames;
	
	private EventTimeline timeline;

	public IndexData() {
	}

	public int getTeamCount() {
		return teamCount;
	}

	public void setTeamCount(int teamCount) {
		this.teamCount = teamCount;
	}

	public int getEventCount() {
		return eventCount;
	}

	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	public Collection<Event> getTopEvents() {
		return topEvents;
	}

	public void setTopEvents(Collection<Event> topEvents) {
		this.topEvents = topEvents;
	}

	public Collection<Team> getTopTeams() {
		return topTeams;
	}

	public void setTopTeams(Collection<Team> topTeams) {
		this.topTeams = topTeams;
	}

	public Collection<Game> getLatestGames() {
		return latestGames;
	}

	public void setLatestGames(Collection<Game> latestGames) {
		this.latestGames = latestGames;
	}

	public Collection<Game> getUpcomingGames() {
		return upcomingGames;
	}

	public void setUpcomingGames(Collection<Game> upcomingGames) {
		this.upcomingGames = upcomingGames;
	}

	public Collection<Game> getCurrentGames() {
		return currentGames;
	}

	public void setCurrentGames(Collection<Game> currentGames) {
		this.currentGames = currentGames;
	}

	public EventTimeline getTimeline() {
		return timeline;
	}

	public void setTimeline(EventTimeline timeline) {
		this.timeline = timeline;
	}
	
}
