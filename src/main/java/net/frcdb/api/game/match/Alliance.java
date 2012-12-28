package net.frcdb.api.game.match;

/**
 *
 * @author tim
 */
public enum Alliance {

	RED,
	BLUE,
	TIE;
	
	public static Alliance getAlliance(String name) {
		for (Alliance a : values()) {
			if (a.toString().equalsIgnoreCase(name)) {
				return a;
			}
		}
		
		return null;
	}

}
