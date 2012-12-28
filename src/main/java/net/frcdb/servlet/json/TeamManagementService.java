package net.frcdb.servlet.json;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
@Path("/admin/team")
public class TeamManagementService {
	
	@GET
	@Path("/{number : \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Team getTeam(@PathParam("number") int number) {
		Team t = Database.getInstance().getTeam(number);
		return t;
	}
	
}
