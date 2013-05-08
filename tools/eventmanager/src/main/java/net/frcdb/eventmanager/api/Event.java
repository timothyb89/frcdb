package net.frcdb.eventmanager.api;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author timothyb89
 */
@Data
@ToString(of = {"name", "shortName", "identifier", "game"})
public class Event {
	
	private String name;
	private String shortName;
	private String identifier;
	
	private String venue;
	private String city;
	private String state;
	private String country;
	
	private Game game; // for FIRST data
	private List<Integer> years;

	public Event() {
		years = new ArrayList<Integer>();
	}
	
	public Event(JsonNode node) {
		name       = value("name", node);
		shortName  = value("shortName", node);
		identifier = value("identifier", node);
		
		venue   = value("venue", node);
		city    = value("city", node);
		state   = value("state", node);
		country = value("country", node);
		
		years = new ArrayList<Integer>();
		if (node.has("years")) {
			for (JsonNode yearNode : node.get("years")) {
				years.add(yearNode.asInt());
			}
		}
	}
	
	private String value(String name, JsonNode n) {
		if (n.has(name)) {
			return n.get(name).asText();
		} else {
			return null;
		}
	}
	
	public void copyFrom(Event other) {
		name = other.name;
		shortName = other.shortName;
		identifier = other.identifier;
		
		venue = other.venue;
		city = other.city;
		state = other.state;
		country = other.country;
		
		game = other.game;
		years = other.years;
	}
	
	public void softCopyFrom(Event other) {
		if (other.name       != null)       name = other.name;
		if (other.shortName  != null)  shortName = other.shortName;
		if (other.identifier != null) identifier = other.identifier;
		
		if (other.venue   != null)   venue = other.venue;
		if (other.city    != null)    city = other.city;
		if (other.state   != null)   state = other.state;
		if (other.country != null) country = other.country;
		
		if (other.game  != null)  game = other.game;
		if (other.years != null) years = other.years;
	}
	
	/**
	 * Checks if this event is objectively different than another event. In this
	 * case, "objectively" means that all fields shared between FRC-DB and FIRST
	 * events must be equal - mainly excluding {@code shortName} and
	 * {@code years}.
	 * @param other the event to check against
	 * @return true if the events are different, false otherwise
	 */
	public boolean isDifferent(Event other) {
		if (other == null) {
			return true;
		}
		
		if (!name.equals(other.name)
				|| !identifier.equals(other.identifier)
				|| !venue.equals(other.venue)
				|| !city.equals(other.city)
				|| !state.equals(other.state)
				|| !country.equals(other.country)) {
			return true;
		}
		
		return false;
	}
	
	public void addYear(Integer year) {
		years.add(year);
	}
	
	public void removeYear(Integer year) {
		years.remove(year);
	}
	
}
