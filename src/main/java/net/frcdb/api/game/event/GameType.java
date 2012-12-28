package net.frcdb.api.game.event;

import net.frcdb.api.event.Event;

/**
 *
 * @author tim
 */
public enum GameType {

	REBOUND(2012, "Rebound Rumble") {

		@Override
		public Game create(Event event) {
			return new Rebound2012(event);
		}
		
	},
	
	LOGOMOTION(2011, "Logomotion") {
		
		@Override
		public Game create(Event event) {
			return new Logomotion2011(event);
		}
		
	},
	
	BREAKAWAY(2010, "Breakaway") {

		@Override
		public Game create(Event event) {
			return new Breakaway2010(event);
		}

	},

	LUNACY(2009, "Lunacy") {

		@Override
		public Game create(Event event) {
			return new Lunacy2009(event);
		}

	};

	private int year;
	private String name;

	private GameType(int year, String name) {
		this.year = year;
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public String getName() {
		return name;
	}

	public abstract Game create(Event event);

	public static GameType getGame(int year) {
		for (GameType l : values()) {
			if (l.getYear() == year) {
				return l;
			}
		}

		return null;
	}

	public static GameType getCurrent() {
		return getGame(Event.CURRENT_YEAR);
	}
	
	public static boolean isCurrent(Game game) {
		return getCurrent().getYear() == game.getGameYear();
	}

}
