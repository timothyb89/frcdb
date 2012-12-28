package net.frcdb.servlet.bean;

import java.util.List;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;

/**
 *
 * @author tim
 */
public class IndexData {
	
	private int teamCount;
	private int eventCount;
	private int gameCount;
	
	private List<Event> topEvents;
	private List<Team> topTeams;
	
	private List<Game> latestGames;
	private List<Game> upcomingGames;
	private List<Game> currentGames;

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

	public List<Event> getTopEvents() {
		return topEvents;
	}

	public void setTopEvents(List<Event> topEvents) {
		this.topEvents = topEvents;
	}

	public List<Team> getTopTeams() {
		return topTeams;
	}

	public void setTopTeams(List<Team> topTeams) {
		this.topTeams = topTeams;
	}

	public List<Game> getLatestGames() {
		return latestGames;
	}

	public void setLatestGames(List<Game> latestGames) {
		this.latestGames = latestGames;
	}

	public List<Game> getUpcomingGames() {
		return upcomingGames;
	}

	public void setUpcomingGames(List<Game> upcomingGames) {
		this.upcomingGames = upcomingGames;
	}

	public List<Game> getCurrentGames() {
		return currentGames;
	}

	public void setCurrentGames(List<Game> currentGames) {
		this.currentGames = currentGames;
	}
	
}
