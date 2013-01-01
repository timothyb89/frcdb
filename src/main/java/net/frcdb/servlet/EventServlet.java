package net.frcdb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.match.Alliance;
import net.frcdb.api.game.match.Match;
import net.frcdb.api.game.match.MatchType;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.game.team.element.OPRProvider;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.servlet.bean.EventData;
import net.frcdb.servlet.bean.EventMatchData;
import net.frcdb.servlet.bean.EventTeamData;
import net.frcdb.util.JSONUtil;
import net.frcdb.util.ListUtil;

/**
 * TODO: standardize the EventData / EventMatchData / etc formatting.
 * @author tim
 */
public class EventServlet extends HttpServlet {

	public static final String PREFIX = "/WEB-INF/srv/event";

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		String path = request.getPathInfo();

		if (path == null || path.length() <= 1) {
			request.setAttribute("error", "No event provided!");
			absForward("/WEB-INF/srv/error.jsp", request, response);
			return;
		}

		Database db = initDatabase(request);

		// /eventname/ : Defaults to current year
		List<String> groups = ListUtil.extract("/([\\S&&[^/]]+)/?", path);
		if (groups != null) {
			try {
				String event = groups.get(1);
				int year = Event.CURRENT_YEAR;
				request.setAttribute("data", createEventData(event, year));
				
				forward("/event.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}

			return;
		}

		// /eventname/json/ : Defaults to current year
		groups = ListUtil.extract("/([\\S&&[^/]]+)/json/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(request);
				exportGame(request, response);
			} catch (IllegalArgumentException ex) {
				jsonError(ex.getMessage(), response);
			}

			return;
		}

		// /eventname/year/
		groups = ListUtil.extract("/([\\S&&[^/]]+)/(\\d+)/?", path);
		if (groups != null) {
			try {
				String event = groups.get(1);
				int year = Integer.parseInt(groups.get(2));
				request.setAttribute("data", createEventData(event, year));
				
				forward("/event.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}

			return;
		}
		
		// /eventname/year/matches/json/
		groups = ListUtil.extract("/([\\S&&[^/]]+)/(\\d+)/matches/json/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(groups.get(2), db, request);
				exportMatches(request, response);
				forward("/match.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		// /eventname/matches/json/ Defaults to current year
		groups = ListUtil.extract("/([\\S&&[^/]]+)/matches/json/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(request);
				exportMatches(request, response);
				forward("/match.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}

		// /eventname/year/json/
		groups = ListUtil.extract("/([\\S&&[^/]]+)/(\\d+)/json/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(groups.get(2), db, request);
				exportGame(request, response);
			} catch (IllegalArgumentException ex) {
				jsonError(ex.getMessage(), response);
			}

			return;
		}

		// /eventname/match/number/ : Defaults to current year
		groups = ListUtil.extract("/([\\S&&[^/]]+)/match/([qsf]?\\d+)/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(request);
				allocateEventMatchData(groups.get(2), request);
				forward("/match.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}

		// /eventname/year/match/number/
		groups = ListUtil.extract("/([\\S&&[^/]]+)/(\\d+)/match/([qsf]?\\d+)/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(groups.get(2), db, request);
				allocateEventMatchData(groups.get(3), request);
				forward("/match.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}

		// /eventname/team/number/ : Defaults to current year
		groups = ListUtil.extract("/([\\S&&[^/]]+)/team/(\\d+)/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(request);
				allocateEventTeamData(groups.get(2), request);
				forward("/team.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}

		// /eventname/year/team/number/
		groups = ListUtil.extract("/([\\S&&[^/]]+)/(\\d+)/team/(\\d+)/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(groups.get(2), db, request);
				allocateEventTeamData(groups.get(3), request);
				forward("/team.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}

		// /eventname/standings/ : Defaults to current year
		groups = ListUtil.extract("/([\\S&&[^/]]+)/standings/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(request);
				forward("/standings.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}

		// /eventname/year/standings/
		groups = ListUtil.extract("/([\\S&&[^/]]+)/(\\d+)/standings/?", path);
		if (groups != null) {
			try {
				allocateEvent(groups.get(1), db, request);
				allocateGame(groups.get(2), db, request);
				forward("/standings.jsp", request, response);
			} catch (IllegalArgumentException ex) {
				request.setAttribute("error", ex.getMessage());
				absForward("/WEB-INF/srv/error.jsp", request, response);
			}
			return;
		}
		
		request.setAttribute("error", "Invalid URL provided!");
		response.setContentType("text/html;charset=UTF-8");
		absForward("/WEB-INF/srv/error.jsp", request, response);
	}

	private Database initDatabase(HttpServletRequest request) {
		return Database.getInstance();
	}

	private void allocateEvent(String name, Database db,
			HttpServletRequest request) throws IllegalArgumentException {
		Event evt = db.getEventByShortName(name);

		if (evt == null) {
			throw new IllegalArgumentException("Event not found!");
		} else {
			request.setAttribute("event", evt);
			
			// register a hit
			// TODO: fix this later when the db supports it
			//db.store(new HitEntry("event", evt.getShortName()));
			
			// use some simple hit counting for now
			evt.setHits(evt.getHits() + 1);
			db.store(evt);
		}
	}

	private EventData createEventData(String eventName, int year) {
		Event event = Database.getInstance().getEventByShortName(eventName);
		if (event == null) {
			throw new IllegalArgumentException("Event not found: " + eventName);
		}
		
		EventData data = new EventData();
		data.setEvent(event);
		
		Game game = null;
		Collection<Game> games = event.getGamesSorted();
		data.setGames(games);
		
		for (Game g : games) {
			if (g.getGameYear() == year) {
				game = g;
			}
		}
		
		if (game == null) {
			throw new IllegalArgumentException("Event " + event.getShortName()
					+ " has no game for year " + year);
		}
		
		data.setGame(game);
		
		data.setTeams(game.getTeams());
		data.setQualificationMatches(game.getQualificationMatches());
		data.setQuarterfinalMatches(game.getQuarterfinalsMatches());
		data.setSemifinalMatches(game.getSemifinalsMatches());
		data.setFinalMatches(game.getFinalsMatches());
		
		return data;
	}
	
	private void allocateGame(String number, Database db,
			HttpServletRequest request) {
		Event evt = (Event) request.getAttribute("event");

		try {
			Game g = evt.getGame(Integer.parseInt(number));

			if (g == null) {
				throw new IllegalArgumentException("No game found for year "
						+ number);
			} else {
				request.setAttribute("eventGame", g);
				
				Date now = new Date();
				if (now.before(g.getStartDate())) {
					request.setAttribute("warning", "This event has not yet "
							+ "occurred, so only team lists are available.");
				} else if (!now.after(g.getEndDate())) {
					request.setAttribute("info", "This event is currently in "
							+ "progress, so certain information may be "
							+ "unavailable.");
				}
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid game year given!");
		}
	}

	private void allocateGame(HttpServletRequest request) {
		Event evt = (Event) request.getAttribute("event");

		Game g = evt.getGame();

		if (g == null) {
			throw new IllegalArgumentException("No game information exists!");
		} else {
			request.setAttribute("eventGame", g);

			if (new Date().before(g.getStartDate())) {
				request.setAttribute("warning", "This event has not yet "
						+ "occurred, so only team lists are available.");
			}
		}
	}

	private void exportGame(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Game game = (Game) request.getAttribute("eventGame");

		response.setContentType("application/json;charset=UTF-8");
		JSONUtil.exportGame(response.getOutputStream(), game);
		response.getOutputStream().close();
	}
	
	private void exportMatches(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Game game = (Game) request.getAttribute("eventGame");
		
		response.setContentType("application/json;charset=UTF-8");
		JSONUtil.exportMatches(response.getOutputStream(), game);
		response.getOutputStream().close();
	}

	private void allocateEventTeamData(String number, HttpServletRequest request)
			throws IllegalArgumentException {

		EventTeamData eventTeamData = new EventTeamData();

		Game game = (Game) request.getAttribute("eventGame");
		eventTeamData.setEvent((Event) request.getAttribute("event"));
		eventTeamData.setGame(game);

		try {
			int teamNumber = Integer.parseInt(number);
			Team team = Database.getInstance().getTeam(teamNumber);
			TeamEntry e = game.getEntry(team);
			eventTeamData.setTeam(e);


			if (e == null) {
				throw new IllegalArgumentException("Team #" + team + "not found!");
			}

			if (e.getMatchesPlayed() == 0) {
				request.setAttribute("warning", "Team <b>"
						+ team.getNumber() + "</b> was inactive at this"
						+ " event.");
			}

			// field: matches
			List<Match> matches = game.getMatches(team);
			eventTeamData.setMatches(matches);

			// field: playedWith
			List<TeamEntry> playedWith = new ArrayList<TeamEntry>();
			List<TeamEntry> playedAgainst = new ArrayList<TeamEntry>();
			List<Team> tmp;
			for (Match m : matches) {
				Alliance a = m.getAlliance(team);
				if (a == Alliance.BLUE) {
					tmp = m.getBlueTeams();
					for (Team t : tmp) {
						TeamEntry te = game.getEntry(t);
						if (!playedWith.contains(te)) {
							playedWith.add(te);
						}
					}
					tmp = m.getRedTeams();
					for (Team t : tmp) {
						TeamEntry te = game.getEntry(t);
						if (!playedAgainst.contains(te)) {
							playedAgainst.add(te);
						}
					}
				} else {
					tmp = m.getRedTeams();
					for (Team t : tmp) {
						TeamEntry te = game.getEntry(t);
						if (!playedWith.contains(te)) {
							playedWith.add(te);
						}
					}
					tmp = m.getBlueTeams();
					for (Team t : tmp) {
						TeamEntry te = game.getEntry(t);
						if (!playedAgainst.contains(te)) {
							playedAgainst.add(te);
						}
					}
				}
			}
			
			eventTeamData.setPlayedWith(playedWith);
			eventTeamData.setPlayedAgainst(playedAgainst);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid team number!");
		}

		request.setAttribute("data", eventTeamData);

	}

	private void allocateEventMatchData(String number, HttpServletRequest request) {
		Event event = (Event) request.getAttribute("event");
		Game game = (Game) request.getAttribute("eventGame");

		int mNum = 0;
		try {
			Match m;
			if (number.startsWith("q")) {
				mNum = Integer.parseInt(number.substring(1));
				m = game.getMatch(MatchType.QUARTERFINAL, mNum);
			} else if (number.startsWith("s")) {
				mNum = Integer.parseInt(number.substring(1));
				m = game.getMatch(MatchType.SEMIFINAL, mNum);
			} else if (number.startsWith("f")) {
				mNum = Integer.parseInt(number.substring(1));
				m = game.getMatch(MatchType.FINAL, mNum);
			} else {
				mNum = Integer.parseInt(number);
				m = game.getMatch(MatchType.QUALIFICATION, mNum);
			}

			if (m == null) {
				throw new IllegalArgumentException("Match not found: " + number);
			}
			
			EventMatchData data = new EventMatchData();
			data.setEvent(event);
			data.setGame(game);
			data.setMatch(m);

			for (Team team : m.getRedTeams()) {
				data.addRedTeam(game.getEntry(team));
			}

			for (Team team : m.getBlueTeams()) {
				data.addBlueTeam(game.getEntry(team));
			}

			data.setRedPrediction(sumOpr(data.getRedTeams()));
			data.setBluePrediction(sumOpr(data.getBlueTeams()));

			request.setAttribute("data", data);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid match number!");
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("Unknown match #" + mNum + "!");
		}
	}

	private double sumOpr(List<TeamEntry> entries) {
		double ret = 0;

		for (TeamEntry e : entries) {
			if (e instanceof OPRProvider) {
				OPRProvider opr = (OPRProvider) e;
				ret += opr.getOPR();
			}
		}

		return ret;
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

	private void jsonError(String message, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		JSONUtil.jsonError(response.getOutputStream(), message);
		response.getOutputStream().close();
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
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
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
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
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
