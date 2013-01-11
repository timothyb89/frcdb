package net.frcdb.servlet.json;

import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.frcdb.api.event.Event;
import net.frcdb.db.Database;
import net.frcdb.export.EventsImport;
import net.frcdb.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
@Path("/admin/event")
public class EventManagementServlet {
	
	private Logger logger = LoggerFactory.getLogger(EventManagementServlet.class);
	
	@GET
	@Path("/{shortName : \\w+}")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse<Event> getEvent(
			@PathParam("shortName") String shortName) {
		Event event = Database.getInstance().getEventByShortName(shortName);
		
		if (event == null) {
			return JsonResponse.error("Event not found: " + shortName);
		} else {
			return JsonResponse.success(event);
		}
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse<Event> createEvent(
			@FormParam("name") String name,
			@FormParam("shortName") String shortName,
			@FormParam("identifier") String identifier,
			@FormParam("venue") String venue,
			@FormParam("city") String city,
			@FormParam("state") String state,
			@FormParam("country") String country) {
		
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to create events.");
		}
		
		Event event = new Event(shortName);
		event.setName(name);
		event.setIdentifier(identifier);
		event.setVenue(venue);
		event.setCity(city);
		event.setState(state);
		event.setCountry(country);
		
		Database.getInstance().store(event);
		
		return JsonResponse.success(event, "Event created successfully.");
	}
	
	@POST
	@Path("/import")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public JsonResponse importEvents(FormDataMultiPart form) throws IOException {
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to import events.");
		}
		
		try {
			FormDataBodyPart field = form.getField("file");
			BodyPartEntity entity = (BodyPartEntity) field.getEntity();
		
			logger.info("Attempting to import events");
			EventsImport imp = new EventsImport(entity.getInputStream());
			return JsonResponse.success("Events imported.");
		} catch (Exception ex) {
			logger.error("Error importing teams", ex);
			return JsonResponse.error("Import failed: " + ex.getMessage());
		}
	}
	
}
