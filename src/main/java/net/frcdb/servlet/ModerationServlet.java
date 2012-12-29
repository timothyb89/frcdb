package net.frcdb.servlet;

import com.google.appengine.api.users.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.frcdb.content.Content;
import net.frcdb.content.moderation.Moderatable;
import net.frcdb.content.moderation.ModerationStatus;
import net.frcdb.db.Database;
import net.frcdb.util.ListUtil;
import net.frcdb.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: forgot to copy doGet from teamservlet
 * @author tim
 */
public class ModerationServlet extends HttpServlet {

	public static final String PREFIX = "/WEB-INF/srv/moderator";
	
	private Logger logger = LoggerFactory.getLogger(ModerationServlet.class);
	
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
		
		if (path == null || path.length() <= 1) {
			// show full moderation queue
			// TODO: pagination?
			allocateQueue(request);
			forward("/queue.jsp", request, response);
			return;
		} else {
			List<String> groups = ListUtil.extract("/([a-f\\d]+)/(\\w+)/?", path);
			if (groups != null) {
				String cid = groups.get(1);
				String action = groups.get(2);
				
				try {
					performModeration(cid, action, request);
					allocateQueue(request);
					forward("/queue.jsp", request, response);
				} catch (IllegalArgumentException ex) {
					request.setAttribute("error", ex.getMessage());
					absForward("/WEB-INF/srv/error.jsp", request, response);
				}
				return;
			}
			
			request.setAttribute("error", "Invalid URL provided!");
			absForward("/WEB-INF/srv/error.jsp", request, response);
		}
	}

	private void allocateQueue(HttpServletRequest request) {
		Database db = Database.getInstance();
		//List<Moderatable> queue = db.getPendingContent();
		// TODO: fixme
		//request.setAttribute("queue", queue);
	}
	
	private void performModeration(String cid, String action,
			HttpServletRequest request) {
		// check the action
		ModerationStatus newStatus = null;
		if (action.equalsIgnoreCase("approve")) {
			newStatus = ModerationStatus.APPROVED;
		} else if (action.equalsIgnoreCase("deny")) {
			newStatus = ModerationStatus.DENIED;
		} else {
			throw new IllegalArgumentException("Invalid moderation action: " 
					+ action);
		}
		
		User user = UserUtil.getUser(request);
		Database db = Database.getInstance();
		
		Content content = db.getContent(cid);
		if (content == null) {
			throw new IllegalArgumentException("No content found with id: "
					+ cid);
		}
		
		if (!(content instanceof Moderatable)) {
			throw new IllegalArgumentException("This content may not be"
					+ " moderated.");
		}
		
		Moderatable m = (Moderatable) content;
		
		if (!UserUtil.isUserAdmin()) {
			throw new IllegalArgumentException("You are not allowed to moderate"
					+ " this type of content.");
		}
		
		if (newStatus == ModerationStatus.APPROVED) {
			request.setAttribute("info", "You approved content: " + cid);
		} else if (newStatus == ModerationStatus.DENIED) {
			request.setAttribute("info", "You denied content: " + cid);
		}
		
		logger.info("Beginning db commit...");
		
		// perform the moderation
		m.setModerationStatus(newStatus);
		db.store(m);
		
		logger.info("Committed to database: " + content);
	}

	private void error(String param, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		request.getSession().setAttribute("error", param);
		
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
