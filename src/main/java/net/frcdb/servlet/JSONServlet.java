package net.frcdb.servlet;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.frcdb.api.event.Event;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.robot.Robot;
import net.frcdb.robot.RobotComponent;
import net.frcdb.robot.RobotProperty;
import net.frcdb.util.JSONUtil;
import net.frcdb.util.ListUtil;

/**
 *
 * @author tim
 */
public class JSONServlet extends HttpServlet {

	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");

		HttpSession session = request.getSession();
		String path = request.getPathInfo();
		
		request.getSession().setAttribute("original_url", path);
		
		if (path == null || path.length() <= 1) {
			jsonError("Not enough parameters", response);
			return;
		}
		
		Database db = Database.getInstance();
		
		// /teams/
		List<String> groups = ListUtil.extract("/teams/?", path);
		if (groups != null) {
			try {
				writeTeams(response.getOutputStream(), db.getTeams());
			} catch (IllegalArgumentException ex) {
				jsonError(ex.getMessage(), response);
			}

			return;
		}
		
		// /events/
		groups = ListUtil.extract("/events/?", path);
		if (groups != null) {
			try {
				writeEvents(response.getOutputStream(), db.getEvents());
			} catch (IllegalArgumentException ex) {
				jsonError(ex.getMessage(), response);
			}

			return;
		}
		
		// /stats/
		groups = ListUtil.extract("/stats/?", path);
		if (groups != null) {
			try {
				writeStats(response.getOutputStream(), db);
			} catch (IllegalArgumentException ex) {
				jsonError(ex.getMessage(), response);
			}

			return;
		}
	}
	
	private void writeTeams(OutputStream out, List<Team> teams) 
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		
		g.writeStartArray();
		for (Team team : teams) {
			JSONUtil.exportTeam(g, team);
		}
		g.writeEndArray();
		
		g.close();
	}
	
	private void writeEvents(OutputStream out, List<Event> events) 
			throws IOException {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		
		g.writeStartArray();
		for (Event event : events) {
			JSONUtil.exportEvent(g, event);
		}
		g.writeEndArray();
		
		g.close();
	}
	
	private void writeStats(OutputStream out, Database db) throws IOException {
		JSONUtil.exportStats(out, db);
	}
	
	private RobotComponent getComponent(Robot robot, String name) {
		// TODO: do this through the db
		// this actually works, its properties that are the issue
		for (RobotComponent component : robot.getComponents()) {
			if (component.getName().equalsIgnoreCase(name)) {
				return component;
			}
		}
		
		return null;
	}
	
	private RobotProperty getProperty(List<RobotProperty> properties, String name) {
		for (RobotProperty property : properties) {
			if (property.getName().equalsIgnoreCase(name)) {
				return property;
			}
		}
		
		return null;
	}

	private void jsonSuccess(HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream out = response.getOutputStream();
		out.println("{\"success\": true}");
		out.close();
	}
	
	private void jsonError(String message, HttpServletResponse response) 
			throws IOException {
		JSONUtil.jsonError(response.getOutputStream(), message);
	}
	
	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/** 
	 * Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
