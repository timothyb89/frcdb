package net.frcdb.servlet.json;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.frcdb.stats.calc.OPRDPRCalc;
import net.frcdb.stats.calc.Statistic;
import net.frcdb.stats.calc.TeamStatisticsCalc;
import net.frcdb.util.UserUtil;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
@Path("/admin/stats")
public class StatsManagementService {
	
	private static final List<Statistic> registered = new ArrayList<Statistic>();
	
	private Logger logger = LoggerFactory.getLogger(StatsManagementService.class);
	
	public static void register(Statistic s) {
		registered.add(s);
	}
	
	static {
		register(new OPRDPRCalc());
		register(new TeamStatisticsCalc());
	}
	
	public static Statistic getStatistic(String name) {
		for (Statistic s : registered) {
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
			@FormParam("event") String event,
			@FormParam("year") int year) {
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to execute stats.");
		}
		
		// do some basic parameter checking
		
		Statistic s = getStatistic(name);
		if (s == null) {
			return JsonResponse.error("Statistic not found: " + name);
		}
		
		// TODO: check event and year?
		
		// queue the task
		Queue queue = QueueFactory.getQueue("statistics");
		queue.add(withUrl("/json/admin/stats/execute-task")
				.method(TaskOptions.Method.POST)
				.param("name", name)
				.param("event", event)
				.param("year", String.valueOf(year)));
		
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
		
		// TODO: need to fix up the statistics API, this is derpy
		try {
			s.calculate(game, null, db);
			return Response.ok("Processing finished without errors.").build();
		} catch (Exception ex) {
			logger.error("Error encountered while processing statistic.", ex);
			return Response.serverError()
					.entity("Error: " + ex.getMessage())
					.build();
		}
	}
	
}
