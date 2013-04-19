package net.frcdb.servlet.json;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import net.frcdb.select.Selector;
import net.frcdb.select.SelectorFactory;
import net.frcdb.stats.calc.*;
import net.frcdb.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines handling for statistics
 * @author tim
 */
@Path("/admin/stats")
public class StatsManagementService {
	
	private static final List<Statistic> stats = new ArrayList<Statistic>();
	
	private Logger logger = LoggerFactory.getLogger(StatsManagementService.class);
	
	public static void register(Statistic s) {
		stats.add(s);
	}
	
	static {
		register(new OPRDPRCalc());
		register(new TeamStatisticsCalc());
		register(new GameYearIndexes());
		register(new FixSources());
		register(new TeamEntryUpdater());
		register(new MatchResultsUpdater());
		register(new StandingsUpdater());
		register(new FinalMatchLevel());
		
		register(new ReferenceRepair());
		
		register(new Counts());
		register(new SitemapGenerator());
		register(new TeamUpdater());
		register(new TeamCacheGenerator());
		register(new EventCacheGenerator());
		register(new ChartsInit());
		register(new EventTimelines());
	}
	
	public static Statistic getStatistic(String name) {
		for (Statistic s : stats) {
			for (String n : s.getNames()) {
				if (n.equalsIgnoreCase(name)) {
					return s;
				}
			}
		}
		
		return null;
	}
	
	@POST
	@Path("/execute")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse execute(
			@FormParam("name") String name,
			@FormParam("event") @DefaultValue("") String event,
			@FormParam("year") @DefaultValue("0") int year) {
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to execute stats.");
		}
		
		// do some basic parameter checking
		
		Statistic s = getStatistic(name);
		if (s == null) {
			return JsonResponse.error("Statistic not found: " + name);
		}
		
		// TODO: check event and year?
		
		Queue queue = QueueFactory.getQueue("statistics");
		
		// execute on a backend if needed
		if (s.getBackendName() != null) {
			String host = BackendServiceFactory.getBackendService()
					.getBackendAddress(s.getBackendName());
			queue.add(withUrl("/json/admin/stats/execute-task")
					.method(TaskOptions.Method.POST)
					.param("name", name)
					.param("event", event)
					.param("year", String.valueOf(year))
					.header("Host", host));
		} else {
			// queue the task normally
			queue.add(withUrl("/json/admin/stats/execute-task")
					.method(TaskOptions.Method.POST)
					.param("name", name)
					.param("event", event)
					.param("year", String.valueOf(year)));
		}
		
		return JsonResponse.success("Statistic has been queued for calculation.");
	}
	
	@POST
	@Path("/execute-selector")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse executeSelect(
			@FormParam("name") String name,
			@FormParam("selector") String selector) {
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to execute stats.");
		}
		
		// do some basic parameter checking
		
		Statistic s = getStatistic(name);
		if (s == null) {
			return JsonResponse.error("Statistic not found: " + name);
		}
		
		if (s instanceof GlobalStatistic) {
			return JsonResponse.error("Global statistics cannot be used with "
					+ "selectors - use the normal executor instead.");
		}
		
		// validate the selector by parsing it
		try {
			Selector sel = SelectorFactory.createSelector(selector);
		} catch (Exception ex) {
			logger.error("Selector error", ex);
			return JsonResponse.error("Selector error: " + ex.getMessage());
		}
		
		// TODO: check event and year?
		
		Queue queue = QueueFactory.getQueue("statistics");
		
		// execute on a backend if needed
		if (s.getBackendName() != null) {
			String host = BackendServiceFactory.getBackendService()
					.getBackendAddress(s.getBackendName());
			queue.add(withUrl("/json/admin/stats/execute-selector-task")
					.method(TaskOptions.Method.POST)
					.param("name", name)
					.param("selector", selector)
					.header("Host", host));
		} else {
			// queue the task normally
			queue.add(withUrl("/json/admin/stats/execute-selector-task")
					.method(TaskOptions.Method.POST)
					.param("name", name)
					.param("selector", selector));
		}
		
		return JsonResponse.success("Statistic has been queued for calculation.");
	}
	
	@POST
	@Path("/execute-task")
	public Response executeTask(
			@FormParam("name") String name,
			@FormParam("event") String eventName,
			@FormParam("year") int year) {
		Statistic s = getStatistic(name);
		if (s == null) {
			logger.warn("Statistic not found: " + name);
			return Response.serverError()
					.entity("Statistic not found: " + name)
					.build();
		}
		
		Database db = Database.getInstance();
		
		try {
			if (s instanceof GameStatistic) {
				GameStatistic gs = (GameStatistic) s;
				
				Collection<? extends Game> games;
				if (eventName.isEmpty() || eventName.equalsIgnoreCase("all")) {
					if (year == 0) {
						games = db.getGames();
					} else {
						games = db.getGames(year);
					}
				} else {
					Event event = db.getEventByShortName(eventName);
					if (event == null) {
						return Response.serverError()
								.entity("Unknown event: " + eventName)
								.build();
					}

					Game game = Database.getInstance().getGame(event, year);
					if (game == null) {
						return Response.serverError()
								.entity("Unknown game: " + eventName + " " + year)
								.build();
					}
					
					games = new ArrayList<Game>();
					// wtf generics? this is stupid
					((List) games).add(game);
				}
				
				for (Game g : games) {
					logger.info("Executing game statistic "
							+ gs.getClass().getName() + " on " + g);
					gs.calculate(g);
				}
			} else if (s instanceof EventStatistic) {
				EventStatistic es = (EventStatistic) s;
				Collection<Event> events;
				
				if (eventName.isEmpty() || eventName.equalsIgnoreCase("all")) {
					events = db.getEvents();
				} else {
					Event event = db.getEventByShortName(eventName);
					if (event == null) {
						return Response.serverError()
								.entity("Unknown event: " + eventName)
								.build();
					}
					
					events = new ArrayList<Event>();
					events.add(event);
				}
				
				for (Event e : events) {
					logger.info("Executing event statistic "
							+ s.getClass().getName() + " on event " + e);
					es.calculate(e);
				}
			} else if (s instanceof GlobalStatistic) {
				GlobalStatistic gs = (GlobalStatistic) s;
				gs.calculate();
				
				logger.info("Executing global statistic "
						+ gs.getClass().getName());
			}
			
			logger.info("Stats completed. stat=" + s.getClass().getName()
					+ ", event=" + eventName + ", year=" + year);
			return Response.ok("Processing finished without errors.").build();
		} catch (Exception ex) {
			logger.error("Error encountered while processing statistic.", ex);
			return Response.serverError()
					.entity("Error: " + ex.getMessage())
					.build();
		}
	}
	
	@POST
	@Path("/execute-selector-task")
	public Response executeSelectorTask(
			@FormParam("name") String name,
			@FormParam("selector") String selector) {
		Statistic s = getStatistic(name);
		if (s == null) {
			logger.warn("Statistic not found: " + name);
			return Response.serverError()
					.entity("Statistic not found: " + name)
					.build();
		}
		
		Selector sel = SelectorFactory.createSelector(selector);
		
		try {
			if (s instanceof GameStatistic) {
				GameStatistic gs = (GameStatistic) s;
				
				if (sel.getType() != Game.class) {
					throw new IllegalArgumentException("Invalid selector "
							+ "provided for statistic " + name);
				}
				
				for (Object o : sel.fetchMatching()) {
					Game g = (Game) o;
					logger.info("Executing game statistic "
							+ s.getClass().getName() + " on game " + g);
					gs.calculate(g);
				}
			} else if (s instanceof EventStatistic) {
				EventStatistic es = (EventStatistic) s;
				
				if (sel.getType() != Event.class) {
					throw new IllegalArgumentException("Invalid selector "
							+ "provided for statistic " + name);
					
				}
				
				for (Object o : sel.fetchMatching()) {
					Event e = (Event) o;
					logger.info("Executing event statistic "
							+ s.getClass().getName() + " on event " + e);
					es.calculate(e);
				}
			} else {
				logger.warn("Unknown statistic type: " + s.getClass().getName());
			}
			
			logger.info("Stats completed. stat=" + s.getClass().getName());
			return Response.ok("Processing finished without errors.").build();
		} catch (Exception ex) {
			logger.error("Error encountered while processing statistic.", ex);
			return Response.serverError()
					.entity("Error: " + ex.getMessage())
					.build();
		}
	}
	
}
