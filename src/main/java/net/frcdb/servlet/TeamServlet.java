package net.frcdb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.event.GameType;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.robot.Robot;
import net.frcdb.robot.year.DefaultRobotTemplate;
import net.frcdb.robot.year.YearTemplate;
import net.frcdb.servlet.bean.RobotData;
import net.frcdb.servlet.bean.TeamData;
import net.frcdb.util.JSONUtil;
import net.frcdb.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class TeamServlet extends HttpServlet {

	public static final String PREFIX = "/WEB-INF/srv/team";
	
	private Logger logger = LoggerFactory.getLogger(TeamServlet.class);
	
	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();
		request.getSession().setAttribute("original_url", path);

		if (path == null || path.length() <= 1) {
			error("No team provided!", request, response);
		}
		
		List<String> groups = ListUtil.extract("/([\\d&&[^/]]+)/?", path);
		if (groups != null) {
			try {
				allocateTeamData(groups.get(1), request);
				forward("/team.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		groups = ListUtil.extract("/([\\d&&[^/]]+)/json/?", path);
		if (groups != null) {
			try {
				allocateTeam(groups.get(1), request);
				exportTeam(request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		groups = ListUtil.extract("/([\\d&&[^/]]+)/robot/?", path);
		if (groups != null) {
			try {
				allocateTeam(groups.get(1), request);
				allocateRobot(String.valueOf(Event.CURRENT_YEAR), request);
				allocateRobotData(request);
				forward("/robot.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		groups = ListUtil.extract("/([\\d&&[^/]]+)/robot/(\\d+)/?", path);
		if (groups != null) {
			try {
				allocateTeam(groups.get(1), request);
				allocateRobot(groups.get(2), request);
				allocateRobotData(request);
				forward("/robot.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		groups = ListUtil.extract("/([\\d&&[^/]]+)/robot/edit/?", path);
		if (groups != null) {
			try {
				allocateTeam(groups.get(1), request);
				
				// TODO: reimplement
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		groups = ListUtil.extract("/([\\d&&[^/]]+)/robot/(\\d+)/edit/?", path);
		if (groups != null) {
			try {
				// TODO: implement again...
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		request.setAttribute("error", "Invalid URL provided!");
		absForward("/WEB-INF/srv/error.jsp", request, response);
	}

	private void allocateTeam(String param, HttpServletRequest request) {
		Database db = Database.getInstance();

		try {
			int number = Integer.parseInt(param);
			Team t = db.getTeam(number);

			if (t == null) {
				throw new IllegalArgumentException("Team not found: " + param);
			}

			request.setAttribute("team", t);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Not a number: " + param);
		} catch (Exception ex) {
			logger.error("Error getting team", ex);
			throw new IllegalArgumentException("Error getting team: " + param 
					+ ": " + ex.getMessage(), ex);
		}
	}
	
	private void allocateTeamData(String param, HttpServletRequest request) {
		Database db = Database.getInstance();
		
		try {
			TeamData data = new TeamData();
			
			int number = Integer.parseInt(param);
			Team t = db.getTeam(number);

			if (t == null) {
				throw new IllegalArgumentException("Team not found: " + param);
			}
			
			data.setTeam(t);
			
			Map<GameType, List<Game>> games = db.getParticipatedGames(t);
			for (GameType type : games.keySet()) {
				TeamData.YearData year = data.year()
						.year(type.getYear())
						.name(type.getName());
				
				for (Game g : games.get(type)) {
					TeamEntry te = g.getEntry(t);
					String eventName = g.getFullEventName();
					
					year.game()
							.entry(g.getEntry(t))
							.eventName(eventName);
				}
			}
			
			request.setAttribute("data", data);
			
			// register a hit
			//db.store(new HitEntry("team", String.valueOf(t.getNumber())));
			
			// use simple hit counting for now
			t.setHits(t.getHits() + 1);
			db.store(t);
			
			// warn on inactive team
			if (db.countEntries(t.getNumber(), Event.CURRENT_YEAR) == 0) {
				request.setAttribute("warning", "Team <b>#" + t.getNumber() 
						+ "</b> did not compete this year.");
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Not a number: " + param);
		} catch (Exception ex) {
			logger.error("Error getting team", ex);
			throw new IllegalArgumentException("Error getting team: " + param 
					+ ": " + ex.getMessage(), ex);
		}
	}
	
	private void allocateRobot(String yearStr, HttpServletRequest request) {
		Database db = Database.getInstance();
		
		try {
			int year = Integer.parseInt(yearStr);
			Team team = (Team) request.getAttribute("team");
			
			if (GameType.getGame(year) == null) {
				throw new IllegalArgumentException("Unknown year " + year);
			}
			
			Robot robot = db.getRobot(team.getNumber(), year);
			if (robot == null) {
				// no robot exists for this team-year, make a new one
				// TODO: also initialize a year template 
				
				robot = new Robot(team, year);
				
				// apply templates
				
				// general template
				new DefaultRobotTemplate(robot).apply();
				
				// yearly specifics
				YearTemplate template = YearTemplate.getTemplate(year, robot);
				if (template != null) {
					template.apply();
				}
				
				// TODO: make an "update templates" class that checks
				// old robot entries and adds new template properties
				
				// save
				db.store(robot);
			}
			
			request.setAttribute("robot", robot);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid year: " + yearStr);
		}
	}
	
	private void allocateRobotData(HttpServletRequest request) {
		RobotData data = new RobotData();
		
		Team team = (Team) request.getAttribute("team");
		data.setTeam(team);
		
		Robot robot = (Robot) request.getAttribute("robot");
		data.setRobot(robot);
		
		// just show all available years and create new robots on demand
		List<Integer> years = new ArrayList<Integer>();
		for (GameType t : GameType.values()) {
			years.add(t.getYear());
		}
		data.setYears(years);
		
		request.setAttribute("data", data);
	}
	
	private void exportTeam(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		
		Team team = (Team) request.getAttribute("team");
		
		JSONUtil.exportTeam(response.getOutputStream(), team);
		response.getOutputStream().close();
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
		response.setContentType("application/json;charset=UTF-8");
		response.getOutputStream().println("{\"error\": \"" + message + "\"}");
		response.getOutputStream().close();
	}
	
	private void error(String param, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		request.setAttribute("error", param);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/srv/error.jsp");
		rd.forward(request, response);
	}
	
	private void forward(String loc, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		RequestDispatcher rd = request.getRequestDispatcher(PREFIX + loc);
		rd.forward(request, response);
	}

	private void absForward(String loc, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		
		RequestDispatcher rd = request.getRequestDispatcher(loc);
		rd.forward(request, response);
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
