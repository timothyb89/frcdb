package net.frcdb.db;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import java.util.*;
import net.frcdb.api.award.Award;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.*;
import net.frcdb.api.game.event.element.OPRStatistics;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.match.MatchType;
import net.frcdb.api.game.standing.BreakawayStanding;
import net.frcdb.api.game.standing.LogomotionStanding;
import net.frcdb.api.game.standing.ReboundStanding;
import net.frcdb.api.game.standing.Standing;
import net.frcdb.api.game.team.BreakawayTeamEntry;
import net.frcdb.api.game.team.LogomotionTeamEntry;
import net.frcdb.api.game.team.LunacyTeamEntry;
import net.frcdb.api.game.team.ReboundTeamEntry;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;
import net.frcdb.api.team.TeamStatistics;
import net.frcdb.config.ConfigurationProperty;
import net.frcdb.content.Content;
import net.frcdb.content.ContentProvider;
import net.frcdb.content.moderation.ModerationStatus;
import net.frcdb.robot.Robot;
import net.frcdb.robot.RobotComponent;
import net.frcdb.robot.RobotProperty;
import net.frcdb.util.ListUtil;
import net.frcdb.util.StringUtil;

/**
 *
 * @author tim
 */
public class Database {
	
	static {
		// Entity registration
		factory().register(ConfigurationProperty.class);
		
		factory().register(Event.class);
		
		// games
		factory().register(Game.class);
		factory().register(Lunacy2009.class);
		factory().register(Breakaway2010.class);
		factory().register(Logomotion2011.class);
		factory().register(Rebound2012.class);
		
		factory().register(OPRStatistics.class);
		
		factory().register(Match.class);
		factory().register(Award.class);
		
		// standings
		factory().register(Standing.class);
		//factory().register(LunacyStanding.class);
		factory().register(BreakawayStanding.class);
		factory().register(LogomotionStanding.class);
		factory().register(ReboundStanding.class);
		
		// teamentries
		factory().register(TeamEntry.class);
		factory().register(LunacyTeamEntry.class);
		factory().register(BreakawayTeamEntry.class);
		factory().register(LogomotionTeamEntry.class);
		factory().register(ReboundTeamEntry.class);
		
		factory().register(Team.class);
		factory().register(TeamStatistics.class);
		
		// robots
		
		//content
		
		
	}
	
	public Database() {
		
	}
	
	public List<ConfigurationProperty> getConfigurationProperties() {
		return ofy().load().type(ConfigurationProperty.class).list();
	}
	
	public ConfigurationProperty getConfigurationProperty(String key) {
		return ofy().load().type(ConfigurationProperty.class)
				.filter("key =", key)
				.limit(1)
				.first().get();
	}
	
	public List<Team> getTeams() {
		return ofy().load().type(Team.class).list();
	}

	public Team getTeam(final int number) {
		return ofy().load().type(Team.class)
				.filter("number =", number)
				.limit(1)
				.first().get();
	}

	public List<Team> getTopTeams(int count) {
		return ofy().load().type(Team.class)
				.filter("hits >", 0)
				.order("-hits")
				.limit(count)
				.list();
	}
	
	public List<Event> getEvents() {
		return ofy().load().type(Event.class).list();
	}

	/**
	 * Attempts to find the event with the closest matching name, based on the
	 * Levenshtein distance between the events' names and the query. This may
	 * return unexpected results, so be sure to ask the user for confirmation
	 * before accepting any returned values.
	 * @param name the event name to search for
	 * @return the event with the shortest Levenshtein distance to the query.
	 */
	public Event getNearestEvent(final String name) {
		// slow, due to custom iteration
		List<Event> events = getEvents();
		
		// orientdb lists aren't mutable, so make a copy
		
		List<Event> cache = new ArrayList<Event>();
		cache.addAll(events);
		
		Collections.sort(cache, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				// find the distances to each
				int distanceA = StringUtil.levenshteinDistance(
						name, o1.getName());
				int distanceB = StringUtil.levenshteinDistance(
						name, o2.getName());
				
				// compare the two
				return distanceA - distanceB;
			}
			
		});
		
		return cache.get(0);
	}

	public Event getEventByShortName(String shortName) {
		shortName = shortName.toLowerCase();
		
		return ofy().load().type(Event.class)
				.filter("shortName =", shortName)
				.limit(1)
				.first().get();
	}
	
	public Event getEventByEID(final int eid) {
		return ofy().load().type(Event.class)
				.filter("EID =", eid)
				.limit(1)
				.first().get();
	}
	
	/**
	 * Retrieves an event based on its identifier.
	 * @param id the event identifier
	 * @return the event with the given identifier
	 */
	public Event getEventByIdentifier(String id) {
		return ofy().load().type(Event.class)
				.filter("identifier =", id)
				.limit(1)
				.first().get();
	}
	
	public String getEventName(String shortName) {
		// normalize
		shortName = shortName.toLowerCase();
		
		Event e = ofy().load().type(Event.class)
				.filter("shortName =", shortName)
				.limit(1)
				.first().get();
		
		if (e == null) {
			return null;
		} else {
			return e.getName();
		}
	}
	
	public List<Event> getTopEvents(int count) {
		return ofy().load().type(Event.class)
				.filter("hits >", 0)
				.limit(count)
				.order("-hits")
				.list();
	}
	
	//public ODocument getNearestEvent(double latitude, double longitude) {
	//	// TODO
	//	return null;
	//}
	
	/**
	 * Get the last events to occur, excluding events that haven't yet occurred.
	 * @param count the number of games to get
	 * @return a list of the latest games in descending order
	 */
	public List<Game> getLatestGames(int count) {
		return ofy().load().type(Game.class)
				.filter("endDate <", new Date())
				.limit(count)
				.order("endDate")
				.list();
	}
	
	/**
	 * Gets <code>count</number> number of games that have not yet occurred.
	 * @param count the number of events to get
	 * @return a list of future games
	 */
	public List<Game> getUpcomingGames(int count) {
		return ofy().load().type(Game.class)
				.filter("startDate >", new Date())
				.limit(count)
				.order("startDate")
				.list();
	}
	
	/**
	 * Gets a list of games that are currently in progress.
	 * @return a list of all games currently in progress
	 */
	public List<Game> getOngoingGames() {
		Date now = new Date();
		
		// only one inequality filter supported
		List<Game> notEnded = ofy().load().type(Game.class)
				.filter("endDate >=", now)
				.list();
		
		List<Game> ret = new ArrayList<Game>();
		for (Game g : notEnded) {
			if (g.getStartDate().before(now)) {
				ret.add(g);
			}
		}
		
		return ret;
	}

	public List<Game> getGames() {
		return ofy().load().type(Game.class).list();
	}
	
	public List<Game> getGames(Event event) {
		return ofy().load().type(Game.class)
				.ancestor(event)
				.list();
	}
	
	public Game getGame(Event event, int year) {
		return ofy().load().type(Game.class)
				.ancestor(event)
				.filter("gameYear", year)
				.limit(1)
				.first().get();
	}
	
	public Game getLatestGame(Event event) {
		return ofy().load().type(Game.class)
				.ancestor(event)
				.order("-gameYear")
				.limit(1)
				.first().get();
	}
	
	public List<Game> getGamesSorted(String event) {
		return ofy().load().type(Game.class)
				.filter("eventName =", event)
				.order("gameYear")
				.list();
	}
	
	public List<Game> getGamesSorted(Event event) {
		return getGamesSorted(event.getShortName());
	}
	
	/**
	 * Gets the games (and events) the given team has participated in
	 * @param team The team to search for
	 * @return a list of games the given team has entries for
	 */
	public List<Game> getGames(Team team) {
		List<TeamEntry> entries = getEntries(team);
		
		return ofy().load().type(Game.class)
				.filter("teams", entries)
				.list(); // TODO: make sure this works
	}

	public List<TeamEntry> getEntries(Team team) {
		return ofy().load().type(TeamEntry.class)
				.filter("team =", team)
				.list();
	}
	
	public List<TeamEntry> getEntries(Team team, int year) {
		return ofy().load().type(TeamEntry.class)
				.filter("team", team)
				.filter("gameYear =", year)
				.list();
	}
	
	public TeamEntry getEntry(Game game, Team team) {
		return ofy().load().type(TeamEntry.class)
				.ancestor(game)
				.filter("team", team)
				.limit(1)
				.first().get();
	}
	
	/**
	 * Deletes all TeamEntries for a particular game
	 * @param game The game for which to delete team entries
	 */
	public void purgeTeamEntries(Game game) {
		ofy().delete().keys(ListUtil.refsToKeys(game.getTeamReferences()));
	}
	
	public Map<GameType, List<Game>> getParticipatedGames(Team team) {
		Map<GameType, List<Game>> r 
				= new EnumMap<GameType, List<Game>>(GameType.class);
		
		// TODO: Fix me
		
		List<TeamEntry> ents = getEntries(team);
		if (ents == null) {
			return r;
		}
		
		for (TeamEntry e : ents) {
			GameType type = GameType.getGame(e.getGameYear());
			Game g = e.getGame().get();
			
			if (r.containsKey(GameType.getGame(e.getGameYear()))) {
				// already added, append
				r.get(type).add(g);
			} else {
				// new year, create new list
				List<Game> entries = new ArrayList<Game>();
				entries.add(g);
				
				r.put(type, entries);
			}
		}
		
		return r;
	}
	
	/**
	 * Gets team entries and event data for games that took place in the current
	 * year.
	 * @param team The team for which to get entries
	 * @return a list of TeamEntries for the given team this year.
	 */
	public List<TeamEntry> getCurrentEntries(final Team team) {
		return ofy().load().type(TeamEntry.class)
				.filter("teamNumber =", team.getNumber())
				.filter("gameYear =", Event.CURRENT_YEAR)
				.list();
	}
	
	public int countEntries(int team, int year) {
		return ofy().load().type(TeamEntry.class)
				.filter("teamNumber =", team)
				.filter("gameYear =", year)
				.count();
	}
	
	public List<Match> getMatches() {
		return ofy().load().type(Match.class).list();
	}
	
	public List<Match> getMatches(Game game) {
		return ofy().load().type(Match.class)
				.ancestor(game)
				.list();
	}
	
	public List<Match> getMatches(Game game, MatchType type) {
		return ofy().load().type(Match.class)
				.ancestor(game)
				.filter("type =", type)
				.list();
	}
	
	public List<Match> getMatches(Game game, Team team) {
		List<Match> red = ofy().load().type(Match.class)
				.ancestor(game)
				.filter("redTeams =", team)
				.list();
		List<Match> blue = ofy().load().type(Match.class)
				.ancestor(game)
				.filter("blueTeams =", team)
				.list();
		
		List<Match> ret = new ArrayList<Match>(red.size() + blue.size());
		ret.addAll(red);
		ret.addAll(blue);
		return ret;
		
		// TODO: test this
	}
	
	public Match getMatch(Game game, MatchType type, int number) {
		return ofy().load().type(Match.class)
				.ancestor(Game.class)
				.filter("type =", type)
				.filter("number =", number)
				.limit(1)
				.first().get();
	}
	
	/**
	 * Delete all matches of a particular game (event-year).
	 * @param game the game to purge
	 */
	public void purgeMatches(Game game) {
		ofy().delete().keys(ListUtil.refsToKeys(game.getMatchReferences()));
		game.getMatchReferences().clear();
	}
	
	public List<Standing> getStandings(String event, int year) {
		return ofy().load().type(Standing.class)
				.filter("eventName =", event)
				.filter("gameYear =", year)
				.list();
	}
	
	public List<Standing> getStandings(Game game) {
		return ofy().load().type(Standing.class)
				.ancestor(game)
				.list();
	}
	
	public void purgeStandings(Game game) {
		ofy().delete().keys(ListUtil.refsToKeys(game.getStandingReferences()));
		game.getStandingReferences().clear();
	}
	
	public List<Content> getAllContent() {
		return ofy().load().type(Content.class).list();
	}
	
	/**
	 * Gets a list of content pending moderation. Content type must be
	 * "Moderatable" and the moderation status must be "pending".
	 * @return a list of pending moderatable content.
	 */
	public List<Content> getPendingContent() {
		return ofy().load().type(Content.class)
				.filter("moderationStatus =", ModerationStatus.PENDING)
				.list();
	}
	
	public Content getContent(String id) {
		return ofy().load().type(Content.class)
				.filter("id =", id)
				.limit(1)
				.first().get();
	}
	
	public ContentProvider getContentProvider(String id, String clazz) {
		// prevent some sql injection, only allow proper names
		/*if (!clazz.matches("\\w+") || !id.matches("#\\d+\\:\\d+")) {
			return null;
		}
		
		List<ContentProvider> ret = query(
				"select from " + clazz + " where @rid = " + id);
		if (ret.isEmpty()) {
			return null;
		} else {
			return ret.get(0);
		}*/
		
		// TODO: content pretty much needs a complete rewrite
		
		return null;
	}
	
	public Robot getRobot(int team, int year) {
		return ofy().load().type(Robot.class)
				.filter("team =", team)
				.filter("year =", year)
				.first().get();
	}
	
	public List<Robot> getRobots(Team team) {
		return ofy().load().type(Robot.class)
				.filter("team =", team)
				.list();
	}
	
	public List<RobotComponent> getComponents(String robotId) {
		return ofy().load().type(RobotComponent.class)
				.filter("robotId =", robotId)
				.list();
	}
	
	public RobotComponent getComponent(String id) {
		return ofy().load().type(RobotComponent.class)
				.filter("id =", id)
				.limit(1)
				.first().get();
	}
	
	public RobotComponent getComponent(String robotId, String name) {
		return ofy().load().type(RobotComponent.class)
				.filter("robotId =", robotId)
				.filter("name =", name)
				.limit(1)
				.first().get();
	}
	
	public List<RobotProperty> getRobotProperties(RobotComponent comp) {
		return ofy().load().type(RobotProperty.class)
				.filter("parentId =", comp.getId())
				.list();
	}
	
	public RobotProperty getRobotProperty(String parentId, String name) {
		return ofy().load().type(RobotProperty.class)
				.filter("parentId =", parentId)
				.filter("name =", name)
				.limit(1)
				.first().get();
	}
	
	public void store(Object object) {
		ofy().save().entity(object);
	}
	
	public void store(Object... objects) {
		ofy().save().entities(objects);
	}
	
	public void delete(Object object) {
		ofy().delete().entity(object);
	}
	
	public static Database getInstance() {
		return new Database();
	}
	
	public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
	
}
