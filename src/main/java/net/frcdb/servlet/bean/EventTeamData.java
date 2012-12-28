/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.frcdb.servlet.bean;

import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.team.TeamEntry;

/**
 *
 * @author Gavin
 */
public class EventTeamData {
    
    private TeamEntry team;
    private Game game;
    private Event event;
    private List<TeamEntry> playedWith;
    private List<TeamEntry> playedAgainst;
    private List<Match> matches;
    
    public EventTeamData() {
        playedWith = new ArrayList<TeamEntry>();
        playedAgainst = new ArrayList<TeamEntry>();
        matches = new ArrayList<Match>();
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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<TeamEntry> getPlayedAgainst() {
        return playedAgainst;
    }

    public void setPlayedAgainst(List<TeamEntry> playedAgainst) {
        this.playedAgainst = playedAgainst;
    }

    public List<TeamEntry> getPlayedWith() {
        return playedWith;
    }

    public void setPlayedWith(List<TeamEntry> playedWith) {
        this.playedWith = playedWith;
    }

    public TeamEntry getTeam() {
        return team;
    }

    public void setTeam(TeamEntry team) {
        this.team = team;
    }
    
    
    
}
