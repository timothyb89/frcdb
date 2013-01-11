package net.frcdb.api.team;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a team and a lot of info about it- everything available from FIRST's
 * database and whatever other info can be found.
 * Some values here may be null if the information wasn't available from our
 * sources (mainly FIRST's site).
 * @author tim
 */
@Cache
@Entity
public class Team {
	
	@Parent
	private Key<TeamRoot> parent;
	
	/**
	 * The team name (generally containing lots of sponsors, etc)
	 */
	private String name;

	/**
	 * The team nickname
	 */
	@Index
	private String nickname;

	/**
	 * The team number
	 */
	@Index
	@Id
	private long number;

	/**
	 * The team's ID in the FIRST database
	 */
	private int tpid;
	
	/**
	 * The team location. Old.
	 */
	private String location;
	
	private String country;
	
	/**
	 * The team's state or province
	 */
	private String state;
	
	private String city;

	/**
	 * The year the team started
	 */
	private int rookieSeason;

	/**
	 * The team's motto
	 */
	private String motto;

	/**
	 * The team website
	 */
	private String website;
	
	@Index
	private int hits;
	
	@Embed
	private List<TeamStatistics> statistics;

	public Team() {
		parent = Key.create(TeamRoot.get());
		statistics = new ArrayList<TeamStatistics>();
	}

	public Team(String name, int number) {
		this.name = name;
		this.number = number;
		
		statistics = new ArrayList<TeamStatistics>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getNumber() {
		return (int) number; // derp
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Integer getTPID() {
		return tpid;
	}

	public void setTPID(int tpid) {
		this.tpid = tpid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getRookieSeason() {
		return rookieSeason;
	}

	public void setRookieSeason(int rookieSeason) {
		this.rookieSeason = rookieSeason;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public List<TeamStatistics> getStatistics() {
		return statistics;
	}
	
	public TeamStatistics getStatistics(int year) {
		for (TeamStatistics s : statistics) {
			if (s.getYear() == year) {
				return s;
			}
		}
		
		return null;
	}
	
	public void addStatistics(TeamStatistics stats) {
		if (getStatistics(stats.getYear()) != null) {
			throw new IllegalArgumentException("Statistics for team " + this
					+ " already exist for year " + stats.getYear());
		}
		
		statistics.add(stats);
	}
	
	@Override
	public String toString() {
		return "Team[" +
					"number=" + number + ", " +
					"nickname=\"" + nickname + "\"" +
				"]";
	}

}
