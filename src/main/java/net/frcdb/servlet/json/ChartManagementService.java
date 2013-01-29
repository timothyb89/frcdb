package net.frcdb.servlet.json;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.frcdb.db.Database;
import net.frcdb.stats.chart.api.Chart;
import net.frcdb.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
@Path("/admin/chart")
public class ChartManagementService {
	
	private Logger logger = LoggerFactory.getLogger(ChartManagementService.class);
	
	@POST
	@Path("/generate")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse generateChart(@FormParam("id") String id) {
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to manage charts.");
		}
		
		Chart chart = Database.getInstance().getChart(id);
		if (chart == null) {
			logger.warn("Chart not found: " + id);
			return JsonResponse.error("Chart not found: " + id);
		}
		
		// queue
		Queue queue = QueueFactory.getQueue("statistics");
		queue.add(withUrl("/json/admin/chart/generate-task")
				.method(TaskOptions.Method.POST)
				.param("id", id));
		
		return JsonResponse.success("Chart has been queued for generation.");
	}
	
	@POST
	@Path("/generate-task")
	public Response executeTask(@FormParam("id") String id) {
		try {
			Chart chart = Database.getInstance().getChart(id);
			if (chart == null) {
				logger.warn("Chart not found: " + id);
				return Response.serverError()
						.entity("Chart not found: " + id)
						.build();
			}

			chart.generate();
			Database.save().entity(chart).now();
			logger.info("Generated chart " + id);
			return Response.ok("Processing finished without errors.").build();
		} catch (Exception ex) {
			logger.error("Error encountered while processing chart.", ex);
			return Response.serverError()
					.entity("Error: " + ex.getMessage())
					.build();
		}
	}
	
}
