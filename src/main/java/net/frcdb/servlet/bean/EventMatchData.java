package net.frcdb.servlet.bean;

import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.team.TeamEntry;

/**
 *
 * @author tjbuckley12
 */
public class EventMatchData {
	
	private Event event;
	private Game game;
	private Match match;
	private List<TeamEntry> redTeams;
	private List<TeamEntry> blueTeams;
	
	private double redPrediction;
	private double bluePrediction;

	public EventMatchData() {
		redTeams = new ArrayList<TeamEntry>();
		blueTeams = new ArrayList<TeamEntry>();
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<TeamEntry> getBlueTeams() {
		return blueTeams;
	}

	public void setBlueTeams(List<TeamEntry> blueTeams) {
		this.blueTeams = blueTeams;
	}
	
	public void addBlueTeam(TeamEntry team) {
		blueTeams.add(team);
	}

	public double getBluePrediction() {
		return bluePrediction;
	}

	public void setBluePrediction(double bluePrediction) {
		this.bluePrediction = bluePrediction;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public List<TeamEntry> getRedTeams() {
		return redTeams;
	}

	public void setRedTeams(List<TeamEntry> redTeams) {
		this.redTeams = redTeams;
	}

	public void addRedTeam(TeamEntry entry) {
		redTeams.add(entry);
	}
	
	public double getRedPrediction() {
		return redPrediction;
	}

	public void setRedPrediction(double redPrediction) {
		this.redPrediction = redPrediction;
	}
	
}
