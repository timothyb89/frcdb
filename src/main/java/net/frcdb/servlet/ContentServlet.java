package net.frcdb.servlet;

import com.google.appengine.api.users.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.frcdb.content.Content;
import net.frcdb.content.ContentProvider;
import net.frcdb.content.moderation.ModerationStatus;
import net.frcdb.content.video.YouTubeClient;
import net.frcdb.content.video.YouTubeVideo;
import net.frcdb.db.Database;
import net.frcdb.util.ImageUtil;
import net.frcdb.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: implement 'add' servlet
 * @author tim
 */
public class ContentServlet extends HttpServlet {

	public static final int IMAGE_MAX_SIZE = 5 * 1024 * 1024; // 5MB
	
	public static final String[] ALLOWED_IMAGE_TYPES = {
		"image/png",
		"image/x-png",
		"image/jpeg",
		"image/jpg",
		"image/gif"
	};
	
	private Logger logger = LoggerFactory.getLogger(ContentServlet.class);
	
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
		String path = request.getPathInfo();
		if (path == null || path.length() <= 1) {
			error("Invalid content URL, no id provided.", request, response);
			return;
		}
		
		path = path.substring(1); // remove leading /
		if (path.endsWith("/")) { // remove trailing / (if any)
			path = path.substring(0, path.length() - 1);
		}
		
		Database db = Database.getInstance();
		
		if (path.equalsIgnoreCase("aupload/image")) {
			// uploads are / should always be handled by the ajax uploader,
			// response must be json
			response.setContentType("application/json");
			try {
				handleAjaxImageUpload(request, response);
			} catch (Exception ex) {
				jsonError("Upload error: " + ex.getMessage(), response);
			}
		} else if (path.equalsIgnoreCase("aupload/video")) {
			response.setContentType("application/json");
			try {
				handleAjaxVideoAdd(request, response);
			} catch (Exception ex) {
				jsonError("Add error: " + ex.getMessage(), response);
			}
		} else if (path.equalsIgnoreCase("add")) {
			response.setContentType("application/json");
			try {
				handleContentAdd(request, response);
			} catch (Exception ex) {
				jsonError("Add error: " + ex.getMessage(), response);
			}
		} else {
			String extra = null;
			if (path.contains("/")) {
				// another / for extra parameter
				extra = path.substring(path.indexOf("/") + 1, path.length());
				path = path.substring(0, path.indexOf("/"));
			}
			
			Content content = db.getContent(path);
			if (content == null) {
				error("No content found with id: " + path, request, response);
				return;
			}
			
			response.setContentType(content.getContentType(extra));

			try {
				content.serve(response.getOutputStream(), extra);
				// content is responsible for closing the output stream when
				// finished
			} catch (IllegalArgumentException ex) {
				error("Content serving failed: " + ex.getMessage(),
						request, response);
			}
		}
	}
	
	private void handleAjaxImageUpload(
			HttpServletRequest request,	HttpServletResponse response)
			throws IOException {
		User user = UserUtil.getUser(request);
		if (user == null) {
			throw new IllegalArgumentException("Please login to upload images.");
		}
		
		List<String> cids = new ArrayList<String>();
		
		try {
			Content c = ImageUtil.handleUpload(request.getInputStream());
			c.setAuthor(user.getUserId());
			
			Database db = (Database) request.getAttribute("database");
			
			// store without the user first so we can shallow store later
			
			db.store(c);
			
			cids.add(c.getId());
			
			jsonSuccess(cids, response);
		} catch (IllegalArgumentException ex)  {
			jsonError("Upload error: " + ex.getMessage(), response);
		} catch (Exception ex) {
			jsonError("Error: " + ex.getMessage(), response);
			logger.error("Error in handleAjaxImageUpload", ex);
		}
	}
	
	private void handleAjaxVideoAdd(
			HttpServletRequest request,	HttpServletResponse response)
			throws IOException {
		
		if (!UserUtil.isUserLoggedIn()) {
			jsonError("Please login to upload videos.", response);
			return;
		}
		
		String vid = request.getParameter("vid");
		logger.info("Attempting to add video: " + vid);
		
		List<String> cids = new ArrayList<String>();
		
		try {
			String videoId = YouTubeClient.getVideoId(vid);
			if (videoId == null) {
				throw new IllegalArgumentException("Not a valid YouTube URL: " 
						+ vid);
			}
			
			YouTubeVideo video = new YouTubeClient().getVideo(videoId);
			video.setAuthor(UserUtil.getUser().getUserId());
			video.setModerationStatus(ModerationStatus.PENDING);
			
			Database db = (Database) request.getAttribute("database");
			db.store(video);
			
			cids.add(video.getId());
			
			jsonSuccess(cids, response);
		} catch (IllegalArgumentException ex)  {
			jsonError("Upload error: " + ex.getMessage(), response);
		} catch (Exception ex) {
			jsonError("Error: " + ex.getMessage(), response);
			logger.error("Error in handleAjaxVideoAdd", ex);
		}
	}
	
	private void handleContentAdd(
			HttpServletRequest request,	HttpServletResponse response)
			throws IOException {
		
		if (!UserUtil.isUserLoggedIn()) {
			jsonError("Please login to add content.", response);
			return;
		}
		
		String cpid = request.getParameter("cpid");
		String type = request.getParameter("type");
		
		Database db = (Database) request.getAttribute("database");
		
		logger.info("Adding to content provider of type " + type + ", " + cpid);
		
		// get the content provider (checks for invalid cpid/type)
		ContentProvider provider = db.getContentProvider(cpid, type);
		if (provider == null) {
			jsonError("No content provider found", response);
			logger.info("No content provider found");
			return;
		}
		
		logger.info("Found content provider: " + provider);
		
		List<String> cids = new ArrayList<String>();
		for (Iterator it = request.getParameterMap().keySet().iterator(); it.hasNext();) {
			String s = it.next().toString();
			if (s.startsWith("cid-")) {
				cids.add(request.getParameter(s));
			}
		}
		
		for (String cid : cids) {
			Content c = db.getContent(cid);
			
			if (c == null) {
				continue; // ignore...
			}
			
			provider.addContent(c);
			db.store(provider);
		}
		
		jsonSuccess(response);
	}
	
	private boolean isImageTypeValid(String type) {
		for (String s : ALLOWED_IMAGE_TYPES) {
			if (s.equals(type)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void error(String param, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		request.getSession().setAttribute("error", param);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/srv/error.jsp");
		rd.forward(request, response);
	}
	
	private void jsonSuccess(HttpServletResponse response)
			throws IOException {
		ServletOutputStream out = response.getOutputStream();
		out.println("{\"success\": true}");
		
		out.close();
	}
	
	private void jsonSuccess(List<String> cids, HttpServletResponse response)
			throws IOException {
		ServletOutputStream out = response.getOutputStream();
		out.println("{\"success\": true, ");
		out.println("\"cids\": [");
		for (int i = 0; i < cids.size(); i++) {
			out.print("\t\"");
			out.print(cids.get(i));
			out.print("\"");
			
			if (i < cids.size() - 1) {
				out.println(",");
			}
		}
		out.println("]}");
		
		out.close();
	}
	
	private void jsonError(String message, HttpServletResponse response)
			throws IOException {
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
