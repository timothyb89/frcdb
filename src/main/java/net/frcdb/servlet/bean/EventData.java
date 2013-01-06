package net.frcdb.servlet.bean;

import java.util.Collection;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.team.TeamEntry;

/**
 *
 * @author tim
 */
public class EventData {
	
	private Event event;
	private Collection<Game> games;
	
	private Game game;
	private Collection<TeamEntry> teams;
	private Collection<Match> qualificationMatches;
	private Collection<Match> quarterfinalMatches;
	private Collection<Match> semifinalMatches;
	private Collection<Match> finalMatches;
	
	private String oprShareData;

	public EventData() {
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Collection<Game> getGames() {
		return games;
	}

	public void setGames(Collection<Game> games) {
		this.games = games;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Collection<TeamEntry> getTeams() {
		return teams;
	}

	public void setTeams(Collection<TeamEntry> teams) {
		this.teams = teams;
	}

	public Collection<Match> getQualificationMatches() {
		return qualificationMatches;
	}

	public void setQualificationMatches(Collection<Match> qualificationMatches) {
		this.qualificationMatches = qualificationMatches;
	}

	public Collection<Match> getQuarterfinalMatches() {
		return quarterfinalMatches;
	}

	public void setQuarterfinalMatches(Collection<Match> quarterfinalMatches) {
		this.quarterfinalMatches = quarterfinalMatches;
	}

	public Collection<Match> getSemifinalMatches() {
		return semifinalMatches;
	}

	public void setSemifinalMatches(Collection<Match> semifinalMatches) {
		this.semifinalMatches = semifinalMatches;
	}

	public Collection<Match> getFinalMatches() {
		return finalMatches;
	}

	public void setFinalMatches(Collection<Match> finalMatches) {
		this.finalMatches = finalMatches;
	}

	public String getOprShareData() {
		return oprShareData;
	}

	public void setOprShareData(String oprShareData) {
		this.oprShareData = oprShareData;
	}
	
}
