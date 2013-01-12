package net.frcdb.db;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Saver;
import java.util.*;
import net.frcdb.api.award.Award;
import net.frcdb.api.event.Event;
import net.frcdb.api.event.EventRoot;
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
import net.frcdb.api.team.TeamRoot;
import net.frcdb.api.team.TeamStatistics;
import net.frcdb.config.ConfigurationProperty;
import net.frcdb.content.Content;
import net.frcdb.content.ContentProvider;
import net.frcdb.content.moderation.ModerationStatus;
import net.frcdb.robot.Robot;
import net.frcdb.robot.RobotComponent;
import net.frcdb.robot.RobotProperty;
import net.frcdb.stats.StatisticsRoot;
import net.frcdb.util.ListUtil;
import net.frcdb.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Pretty much everything here needs to use keys instead of list() queries
 * whenever possible to hit the cache.
 * @author tim
 */
public class Database {
	
	static {
		// Entity registration
		factory().register(ConfigurationProperty.class);
		
		factory().register(EventRoot.class);
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
		
		factory().register(TeamRoot.class);
		factory().register(Team.class);
		factory().register(TeamStatistics.class);
		
		factory().register(StatisticsRoot.class);
		
		// robots
		
		//content
	
	}
	
	private Logger logger = LoggerFactory.getLogger(Database.class);
	
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
	
	public Collection<Team> getTeams() {
		//return ofy().load().type(Team.class).list();
		// only get-by-key will use the cache!
		return ofy().load().keys(
				ofy().load().type(Team.class).keys()).values();
	}
	
	public int countTeams() {
		return ofy().load().type(Team.class).count();
	}

	/**
	 * Gets a team with the given number. As this is a direct key load, it
	 * doesn't make any datastore queries and should return quickly and will use
	 * the cache if possible.
	 * @param number the number of the team to fetch
	 * @return the team with the given number, or null if not found
	 */
	public Team getTeam(final int number) {
		Key<Team> key = Key.create(TeamRoot.key(), Team.class, number);
		return ofy().load().key(key).get();
	}

	public Collection<Team> getTopTeams(int count) {
		return ofy().load().keys(ofy().load().type(Team.class)
				.filter("hits >", 0)
				.order("-hits")
				.limit(count)
				.keys()).values();
	}
	
	public Collection<Event> getEvents() {
		return ofy().load().keys(
				ofy().load().type(Event.class).keys()).values();
	}
	
	public int countEvents() {
		return ofy().load().type(Event.class).count();
	}

	/**
	 * Attempts to find the event with the closest matching name, based on the
	 * Levenshtein distance between the events' names and the query. This may
	 * return unexpected results, so be sure to ask the user for confirmation
	 * before accepting any returned values.
	 * Holy crap this is expensive
	 * @param name the event name to search for
	 * @return the event with the shortest Levenshtein distance to the query.
	 */
	public Event getNearestEvent(final String name) {
		// slow, due to custom iteration
		Collection<Event> events = getEvents();
		
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

	/**
	 * Gets an event via its short name. As this is a direct key query, it
	 * is the cheapest way to fetch an event, and will completely avoid the
	 * datastore if the cache is warm.
	 * @param shortName the exact short name of the event to fetch
	 * @return the event with the given short name, or null if not found
	 */
	public Event getEventByShortName(String shortName) {
		if (shortName == null || shortName.isEmpty()) {
			return null;
		}
		
		Key<Event> key = Key.create(EventRoot.key(), Event.class, shortName);
		return ofy().load().key(key).get();
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
		
		Event e = getEventByShortName(shortName);
		
		if (e == null) {
			return null;
		} else {
			return e.getName();
		}
	}
	
	public Collection<Event> getTopEvents(int count) {
		return ofy().load().keys(ofy().load().type(Event.class)
				.filter("hits >", 0)
				.limit(count)
				.order("-hits")
				.keys()).values();
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
	public Collection<Game> getLatestGames(int count) {
		return ofy().load().keys(ofy().load().type(Game.class)
				.filter("endDate <", new Date())
				.limit(count)
				.order("-endDate")
				.keys()).values();
	}
	
	/**
	 * Gets <code>count</number> number of games that have not yet occurred.
	 * @param count the number of events to get
	 * @return a list of future games
	 */
	public Collection<Game> getUpcomingGames(int count) {
		return ofy().load().keys(ofy().load().type(Game.class)
				.filter("startDate >", new Date())
				.limit(count)
				.order("startDate")
				.keys()).values();
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

	public Collection<Game> getGames() {
		return ofy().load().keys(ofy().load().type(Game.class).keys()).values();
	}
	
	public Collection<Game> getGames(int year) {
		return ofy().load().keys(ofy().load()
				.type(Game.class)
				.filter("gameYearIndex =", year)
				.keys()).values();
	}
	
	public int countGames() {
		// TODO: costly! D:
		return ofy().load().type(Game.class).count();
	}
	
	public Collection<Game> getGames(Event event) {
		return ofy().load().keys(ofy().load().type(Game.class)
				.ancestor(event)
				.keys()).values();
	}
	
	public Game getGame(Event event, int year) {
		Key<Game> key = Key.create(Key.create(event), Game.class, year);
		return ofy().load().key(key).get();
	}
	
	public Game getLatestGame(Event event) {
		return ofy().load().key(ofy().load().type(Game.class)
				.ancestor(event)
				.order("-__key__")
				.limit(1)
				.first().key()).get();
	}
	
	/**
	 * Gets the games (and events) the given team has participated in
	 * @param team The team to search for
	 * @return a list of games the given team has entries for
	 */
	public Collection<Game> getGames(Team team) {
		Collection<TeamEntry> entries = getEntries(team);
		
		return ofy().load().keys(ofy().load().type(Game.class)
				.filter("teams", entries)
				.keys()).values(); // TODO: make sure this works
	}

	public Collection<TeamEntry> getEntries(Team team) {
		return ofy().load().keys(ofy().load().type(TeamEntry.class)
				.filter("team =", team)
				.keys()).values();
	}
	
	public Collection<TeamEntry> getEntries(Team team, int year) {
		return ofy().load().keys(ofy().load().type(TeamEntry.class)
				.filter("team", team)
				.filter("gameYear =", year)
				.keys()).values();
	}
	
	public TeamEntry getEntry(Game game, Team team) {
		// special case: TeamEntries have preset IDs with the team number
		return ofy().load().key(Key.create(
				Key.create(game),
				TeamEntry.class,
				team.getNumber())).get();
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
		
		Collection<TeamEntry> ents = getEntries(team);
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
				.filter("team =", team)
				.filter("gameYear =", Event.CURRENT_YEAR)
				.list();
	}
	
	public int countEntries(Team team, int year) {
		return ofy().load().type(TeamEntry.class)
				.filter("team =", team)
				.filter("gameYear =", year)
				.count();
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
		return ofy().load().key(ofy().load().type(Match.class)
				.ancestor(game)
				.filter("type =", type)
				.filter("number =", number)
				.limit(1)
				.first()
				.key()).get();
	}
	
	/**
	 * Delete all matches of a particular game (event-year).
	 * @param game the game to purge
	 */
	public void purgeMatches(Game game) {
		ofy().delete().keys(ListUtil.refsToKeys(game.getAllMatchReferences()));
		game.getQualificationMatchReferences().clear();
		game.getQuarterfinalsMatchReferences().clear();
		game.getSemifinalsMatchReferences().clear();
		game.getFinalsMatchReferences().clear();
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

	public static Saver save() {
		return ofy().save();
	}
	
    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
	
}
