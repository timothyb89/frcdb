package net.frcdb.api.game.event;

import net.frcdb.api.event.Event;

/**
 *
 * @author tim
 */
public enum GameType {

	ASCENT(2013, "Ultimate Ascent") {

		@Override
		public Game create(Event event) {
			return new Ascent2013(event);
		}

		@Override
		public Class getGameClass() {
			return Ascent2013.class;
		}
		
	},
	
	REBOUND(2012, "Rebound Rumble") {

		@Override
		public Game create(Event event) {
			return new Rebound2012(event);
		}

		@Override
		public Class getGameClass() {
			return Rebound2012.class;
		}
		
	},
	
	LOGOMOTION(2011, "Logomotion") {
		
		@Override
		public Game create(Event event) {
			return new Logomotion2011(event);
		}

		@Override
		public Class getGameClass() {
			return Logomotion2011.class;
		}
		
	},
	
	BREAKAWAY(2010, "Breakaway") {

		@Override
		public Game create(Event event) {
			return new Breakaway2010(event);
		}

		@Override
		public Class getGameClass() {
			return Breakaway2010.class;
		}

	},

	LUNACY(2009, "Lunacy") {

		@Override
		public Game create(Event event) {
			return new Lunacy2009(event);
		}

		@Override
		public Class getGameClass() {
			return Lunacy2009.class;
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

	public Class<? extends Game> getGameClass() {
		return null;
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
