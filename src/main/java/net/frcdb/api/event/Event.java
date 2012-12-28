package net.frcdb.api.event;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
@Entity
public class Event {

	/**
	 * The year to show default data from.
	 */
	public static final int CURRENT_YEAR = 2012;
	
	@Id
	private Long id;

	/**
	 * The full event name, including sponsors, etc
	 */
	private String name;

	/**
	 * A short, identifiable, and plain-English event name.
	 */
	private String shortName;

	/**
	 * The event ID, usually the state abbreviation or similar.
	 */
	private String identifier;

	private String venue;
	private String city;
	private String state;
	private String country;
	
	private double latitude;
	private double longitude;
	
	/**
	 * A list of other possible names for this event
	 */
	private List<String> aliases;
	
	private int hits;
	
	private List<Ref<Game>> games;

	public Event() {
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public List<Game> getGames() {
		return Database.getInstance().getGames(this);
	}

	public List<Game> getGamesSorted() {
		return Database.getInstance().getGamesSorted(this);
	}

	public Game getGame(int year) {
		return Database.getInstance().getGame(this, year);
	}

	/**
	 * Gets the latest game for this event.
	 * @return the current game
	 */
	public Game getGame() {
		return Database.getInstance().getLatestGame(this);
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	public void addAlias(String alias) {
		if (aliases == null) {
			aliases = new ArrayList<String>();
		}
		
		aliases.add(alias);
	}
	
	public void removeAlias(String alias) {
		if (aliases == null) {
			aliases = new ArrayList<String>();
			return;
		}
		
		aliases.remove(alias);
	}
	
	public boolean isChampionship() {
		return name.equals("FIRST Championship");
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
}
