package net.frcdb.servlet.json;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.export.TeamsImport;
import net.frcdb.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
@Path("/admin/team")
public class TeamManagementService {
	
	private Logger logger = LoggerFactory.getLogger(TeamManagementService.class);
	
	@GET
	@Path("/{number : \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse<Team> getTeam(@PathParam("number") int number) {
		Team team = Database.getInstance().getTeam(number);
		
		if (team == null) {
			return JsonResponse.error(team, "Team not found: " + number);
		} else {
			return JsonResponse.success(team);
		}
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse<Team> createTeam(
			@FormParam("name") String name,
			@FormParam("nickname") String nickname,
			@FormParam("number") int number,
			@FormParam("country") String country,
			@FormParam("state") String state,
			@FormParam("city") String city,
			@FormParam("rookieSeason") int rookieSeason,
			@FormParam("motto") String motto,
			@FormParam("website") String website) {
		
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to create teams.");
		}
		
		Team team = new Team(name, number);
		team.setNickname(nickname);
		team.setCountry(country);
		team.setState(state);
		team.setCity(city);
		team.setRookieSeason(rookieSeason);
		team.setMotto(motto);
		team.setWebsite(website);
		
		Database.getInstance().store(team);
		
		return JsonResponse.success(team, "Team created successfully.");
	}
	
	@POST
	@Path("/modify")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse modifyTeam(
			@FormParam("number") int number,
			@FormParam("name") String name,
			@FormParam("nickname") String nickname,
			@FormParam("country") String country,
			@FormParam("state") String state,
			@FormParam("city") String city,
			@FormParam("rookieSeason") String rookieSeason,
			@FormParam("motto") String motto,
			@FormParam("website") String website) {
		
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to import teams.");
		}
		
		Team t = Database.getInstance().getTeam(number);
		if (t == null) {
			return JsonResponse.error("Team " + number + " does not exist");
		}
		
		if (crappyValidate(name)) {
			t.setName(name);
		}
		
		if (crappyValidate(nickname)) {
			t.setNickname(nickname);
		}
		
		if (crappyValidate(country)) {
			t.setCountry(country);
		}
		
		if (crappyValidate(state)) {
			t.setState(state);
		}
		
		if (crappyValidate(city)) {
			t.setCity(city);
		}
		
		if (crappyValidate(rookieSeason)) {
			try {
				t.setRookieSeason(Integer.parseInt(rookieSeason));
			} catch (NumberFormatException e) {}
		}
		
		if (crappyValidate(website)) {
			t.setWebsite(website);
		}
		
		Database.save().entity(t);
		
		return JsonResponse.success("Team updated.");
	}
	
	private boolean crappyValidate(String s) {
		return s != null && !s.isEmpty();
	}
	
	@POST
	@Path("/import")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public JsonResponse importTeams(FormDataMultiPart form) throws IOException {
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to import teams.");
		}
		
		FormDataBodyPart field = form.getField("file");
		BodyPartEntity entity = (BodyPartEntity) field.getEntity();
		
		// save the json file
		FileService service = FileServiceFactory.getFileService();
		AppEngineFile file = 
				service.createNewBlobFile("application/json", "teams.json");
		
		InputStream stream = entity.getInputStream();
		
		FileWriteChannel channel = service.openWriteChannel(file, true);
		byte[] buffer = new byte[BlobstoreService.MAX_BLOB_FETCH_SIZE];
		
		int len;
		while ((len = stream.read(buffer)) >= 0) {
			ByteBuffer bb = ByteBuffer.wrap(buffer, 0, len);
			channel.write(bb);
		}

		channel.closeFinally();
		
		logger.info("Saved team json to the blobstore");
		
		String key = service.getBlobKey(file).getKeyString();
		
		Queue queue = QueueFactory.getQueue("import");
		queue.add(withUrl("/json/admin/team/import-task")
				.method(TaskOptions.Method.POST)
				.countdownMillis(1000)
				.param("key", key));
		
		return JsonResponse.success("Queued teams for import.");
	}
	
	@POST
	@Path("/import-task")
	public Response importTask(@FormParam("key") String key) {
		BlobstoreService s = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey bk = new BlobKey(key);
		
		try {
			BlobstoreInputStream stream = new BlobstoreInputStream(bk);
			
			// import
			logger.info("Attempting to import teams");
			TeamsImport imp = new TeamsImport(stream);

			// clean up
			stream.close();
			
			FileService service = FileServiceFactory.getFileService();
			AppEngineFile file = service.getBlobFile(bk);
			service.delete(file);
			
			return Response.ok("Imported teams").build();
		} catch (Exception ex) {
			logger.error("Failed to import teams.", ex);
			return Response
					.serverError()
					.entity("Import failed: " + ex.getMessage())
					.build();
		}
	}
	
}
