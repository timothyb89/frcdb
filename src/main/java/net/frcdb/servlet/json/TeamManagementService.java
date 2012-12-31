package net.frcdb.servlet.json;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
	@Path("/import")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public JsonResponse importTeams(
			FormDataMultiPart form) 
			throws IOException {
		if (!UserUtil.isUserAdmin()) {
			return JsonResponse.error("You are not allowed to import teams.");
		}
		com.sun.jersey.core.impl.provider.entity.ByteArrayProvider provider;
		FormDataBodyPart field = form.getField("file");
		
		BodyPartEntity entity = (BodyPartEntity) field.getEntity();
		
		try {
			logger.info("Attempting to import teams");
			TeamsImport imp = new TeamsImport(entity.getInputStream());
			return JsonResponse.success("Teams imported.");
		} catch (Exception ex) {
			logger.error("Error importing teams", ex);
			return JsonResponse.error("Import failed: " + ex.getMessage());
		}
	}
	
}
