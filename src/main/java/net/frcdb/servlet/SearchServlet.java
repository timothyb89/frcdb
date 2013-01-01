package net.frcdb.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.util.JSONUtil;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

/**
 * TODO: full results are extremely expensive, may need to disable them.
 * @author tim
 */
public class SearchServlet extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		request.getSession().setAttribute("original_url", request.getPathInfo());

		String q = request.getParameter("q");
		String type = request.getParameter("type");
		
		String path = request.getPathInfo();
		if (path != null) {
			Database db = Database.getInstance();
			if (path.equalsIgnoreCase("/json")) {
				if (type.equals("teams")) {
					Collection<Team> results = teamSearchJson(db, q);
					exportTeamResults(results, response);
				}
			} else if (path.equalsIgnoreCase("/nearest")) {
				if (type.equals("event")) {
					try {
						exportNearestEvent(request, response);
					} catch (IllegalArgumentException ex) {
						jsonError(ex.getMessage(), response);
					}
				}
			}
		} else {
			
			if (q != null) {
				if (type == null) {
					type = "teams";
				}

				String r = allocateSearch(q, type, request);
				if (r != null) {
					//response.sendRedirect("http://frcdb.net" + r);
					response.sendRedirect(r);
					return;
				}
			}

			RequestDispatcher rd = 
					request.getRequestDispatcher("/WEB-INF/srv/search/query.jsp");
			rd.forward(request, response);
		}
	}

	private String allocateSearch(String q, String type, HttpServletRequest request) {
		Database db = Database.getInstance();

		if (type.equalsIgnoreCase("teams")) {
			return teamSearch(db, q, request);
		} else if (type.equalsIgnoreCase("events")) {
			return eventSearch(db, q, request);
		} else {
			request.setAttribute("error", "Invalid search type");
			return null;
		}
	}

	private String teamSearch(Database db, String q, HttpServletRequest request) {
		q = q.toLowerCase();
		
		// try to find quickly
		try {
			int tnum = Integer.parseInt(q);
			Team t = db.getTeam(tnum);
			if (t != null) {
				return "/team/" + tnum;
			}
		} catch (NumberFormatException ex) {
			// continue
		}
		
		Collection<Team> res;
		Collection<Team> all = db.getTeams();
		
		if (q.isEmpty()) {
			res = all;
		} else {
			// horribly inefficient, but seems to be the only way...
			res = new ArrayList<Team>();
			
			for (Team t : all) {
				String number = String.valueOf(t.getNumber());
				String location = (t.getCity() + " " + t.getState() + " " + 
						t.getCountry()).toLowerCase();
				String name = t.getName().toLowerCase();
				String nickname = t.getNickname().toLowerCase();
				
				if (number.contains(q) || location.contains(q)
						|| name.contains(q) || nickname.contains(q)) {
					res.add(t);
				}
			}
		}
		
		if (res.size() == 1) {
			// TODO: we already have the team loaded, just forward there
			return "/team/" + res.iterator().next().getNumber();
		} else {
			request.setAttribute("teamSearchResults", res);
			return null;
		}
	}
	
	private Collection<Team> teamSearchJson(Database db, String q) {
		q = q.toLowerCase();
		
		Collection<Team> res;
		Collection<Team> all = db.getTeams();
		
		if (q.isEmpty()) {
			res = all;
		} else {
			// horribly inefficient, but seems to be the only way...
			res = new ArrayList<Team>();
			
			for (Team t : all) {
				String number = String.valueOf(t.getNumber());
				String location = (t.getCity() + " " + t.getState() + " " + 
						t.getCountry()).toLowerCase();
				String name = t.getName().toLowerCase();
				String nickname = t.getNickname().toLowerCase();
				
				if (number.contains(q) || location.contains(q)
						|| name.contains(q) || nickname.contains(q)) {
					res.add(t);
				}
				
				// limit the query
				if (res.size() > 30) {
					break;
				}
			}
		}
		
		return res;
	}
	
	private void exportTeamResults(Collection<Team> teams,
			HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		
		JSONUtil.exportTeamResults(response.getOutputStream(), teams);
		response.getOutputStream().close();
	}

	private String eventSearch(Database db, String q, HttpServletRequest request) {
		q = q.toLowerCase();

		// attempt to get by short name
		Event event = db.getEventByShortName(q);
		if (event != null) {
			return "/event/" + event.getShortName();
		}
		
		Collection<Event> res;
		Collection<Event> all = db.getEvents();
		if (q.isEmpty()) {
			res = all;
		} else {
			res = new ArrayList<Event>();
			
			try {
				// year search if q is numerical
				int year = Integer.parseInt(q);
				
				for (Event e : all) {
					Game g = e.getGame(year);
					if (g != null) {
						res.add(e);
					}
				}
			} catch (NumberFormatException ex) {
				// search through fields normally
				for (Event e : all) {
					String name = e.getName().toLowerCase();
					String shortName = e.getShortName().toLowerCase();
					String id = e.getIdentifier().toLowerCase();
					String venue = e.getVenue().toLowerCase();
					String city = e.getCity().toLowerCase();
					String state = e.getState().toLowerCase();
					String country = e.getCountry().toLowerCase();
					
					// TODO: NPE waiting to happen
					
					if (name.contains(q)
							|| shortName.contains(q)
							|| (id != null && id.contains(q))
							|| (venue != null && venue.contains(q))
							|| (city != null && city.contains(q))
							|| (state != null && state.contains(q))
							|| (country != null && country.contains(q))) {
						res.add(e);
					}
				}
			}
		}
		
		if (res.size() == 1) {
			return "/event/" + res.iterator().next().getShortName();
		} else {
			request.setAttribute("eventSearchResults", res);
			return null;
		}
	}
	
	private void exportNearestEvent(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO: Fix this
		String latString = request.getParameter("latitude");
		double latitude = validateDouble(latString);
		
		String longString = request.getParameter("longitude");
		double longitude = validateDouble(longString);
		
		//ODocument event = Database.getInstance().getNearestEvent(latitude, longitude);
		//String name = event.field("name");
		//String shortName = event.field("shortName");
		//double distance = (Double) event.field("distance");
		
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = f.createJsonGenerator(out, JsonEncoding.UTF8);
		g.setPrettyPrinter(new DefaultPrettyPrinter());
		
		g.writeStartObject();
		
		//g.writeStringField("name", name);
		//g.writeStringField("shortName", shortName);
		//.writeNumberField("distance", distance);
		g.writeEndObject();
		
		g.close();
	}
	
	private double validateDouble(String dStr) {
		if (dStr == null) {
			throw new IllegalArgumentException("No parameter provided");
		}
		
		try {
			return Double.parseDouble(dStr);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid number: " + dStr);
		}
	}

	private void jsonError(String message, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.getOutputStream().println("{\"error\": \"" + message + "\"}");
		response.getOutputStream().close();
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
