package net.frcdb.servlet.bean;

import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;

/**
 *
 * @author tim
 */
public class TeamData {
	
	private Team team;
	private List<YearData> years;

	public TeamData() {
		years = new ArrayList<YearData>();
	}
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	public TeamData team(Team team) {
		setTeam(team);
		return this;
	}

	public List<YearData> getYears() {
		return years;
	}

	public void setYears(List<YearData> years) {
		this.years = years;
	}
	
	public void addYear(YearData h) {
		years.add(h);
	}
	
	public YearData year() {
		YearData data = new YearData();
		years.add(data);
		return data;
	}
	
	public class YearData {
		
		private int year;
		private String name;
		private List<GameData> games;

		public YearData() {
			games = new ArrayList<GameData>();
		}

		public List<GameData> getGames() {
			return games;
		}

		public void setGames(List<GameData> games) {
			this.games = games;
		}
		
		public YearData game(GameData data) {
			games.add(data);
			return this;
		}

		public GameData game() {
			GameData ret = new GameData();
			games.add(ret);
			return ret;
		}
		
		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}
		
		public YearData year(int year) {
			setYear(year);
			return this;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public YearData name(String name) {
			setName(name);
			return this;
		}
		
	}
	
	public class GameData {
		
		private String eventName;
		private Game game;
		private TeamEntry entry;

		public TeamEntry getEntry() {
			return entry;
		}

		public void setEntry(TeamEntry entry) {
			this.entry = entry;
		}
		
		public GameData entry(TeamEntry e) {
			setEntry(e);
			return this;
		}

		public Game getGame() {
			return game;
		}

		public void setGame(Game game) {
			this.game = game;
		}

		public GameData game(Game game) {
			this.game = game;
			return this;
		}
		
		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public GameData eventName(String eventName) {
			setEventName(eventName);
			return this;
		}
		
	}
	
}
