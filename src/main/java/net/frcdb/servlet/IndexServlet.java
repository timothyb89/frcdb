package net.frcdb.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.frcdb.api.event.Event;
import net.frcdb.api.event.EventRoot;
import net.frcdb.api.team.TeamRoot;
import net.frcdb.db.Database;
import net.frcdb.servlet.bean.IndexData;

/**
 *
 * @author tim
 */
public class IndexServlet extends HttpServlet {

	public static final String PREFIX = "/WEB-INF/srv";
	
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
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Database db = Database.getInstance();
		
		TeamRoot teamRoot = TeamRoot.get();
		EventRoot eventRoot = EventRoot.get();
		
		IndexData data = new IndexData();
		data.setTeamCount(teamRoot.getCount());
		data.setEventCount(eventRoot.getEventCount());
		data.setGameCount(eventRoot.getGameCount());
		
		data.setTopEvents(db.getTopEvents(5));
		data.setTopTeams(db.getTopTeams(5));
		
		data.setLatestGames(db.getLatestGames(5));
		data.setUpcomingGames(db.getUpcomingGames(5));
		data.setCurrentGames(db.getOngoingGames());
		
		data.setTimeline(eventRoot.getTimeline(Event.CURRENT_YEAR));
		
		request.setAttribute("data", data);
		
		forward("/index.jsp", request, response);
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
