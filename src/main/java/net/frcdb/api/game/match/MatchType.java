package net.frcdb.api.game.match;

import java.util.Comparator;

/**
 * Represents a type of match. Currently troublesome because of old cached
 * versions hiding in the database.
 * @author tim
 */
public enum MatchType {

	QUALIFICATION("Qualifications", "", 0),
	QUARTERFINAL("Quarterfinals", "Q", 1),
	SEMIFINAL("Semifinals", "S", 2),
	FINAL("Finals", "F", 3),
	NULL("None", "D", 4);

	private String text;
	private String prefix;
	private Integer weight;

	private MatchType(String text, String prefix, Integer weight) {
		this.text = text;
		this.prefix = prefix;
		this.weight = weight;
	}

	public String getText() {
		if (this != NULL && !text.endsWith("s")) {
			text += "s"; // compensate for database
		}
		
		return text;
	}

	public String getPrefix() {
		return prefix;
	}

	public Integer getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return "MatchType["
				+ "text='" + text + "', "
				+ "prefix='" + prefix + "', "
				+ "weight='" + weight + "']";
	}
	
	public class MatchTypeComparer implements Comparable<MatchType> {

		@Override
		public int compareTo(MatchType o) {
			return weight.compareTo(o.getWeight());
		}
		
	}
	
	public MatchTypeComparer comparer() {
		return new MatchTypeComparer();
	}

	public static class MatchTypeComparator implements Comparator<MatchType> {

		@Override
		public int compare(MatchType o1, MatchType o2) {
			return o1.compareTo(o2);
		}
		
	}
	
	public static MatchTypeComparator comparator() {
		return new MatchTypeComparator();
	}
	
	public static MatchType getTypeFromPrefix(String prefix) {
		for (MatchType t : values()) {
			if (t.getPrefix().equals(prefix)) {
				return t;
			}
		}
		
		return null;
	}
	
	public static MatchType clone(MatchType matchType) {
		return getTypeFromPrefix(matchType.getPrefix());
	}
	
	public static MatchType getMatchType(String name) {
		for (MatchType t : values()) {
			if (t.getText().equalsIgnoreCase(name)) {
				return t;
			}
		}
		
		return null;
	}
	
}
