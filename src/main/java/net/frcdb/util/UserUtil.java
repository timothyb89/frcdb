package net.frcdb.util;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles initialization and destruction of users. The user object is stored
 * along with the database in
 * @author tim
 */
public class UserUtil {
	
	/**
	 * Initializes the user if needed
	 * @param session the current session
	 */
	public static void init(HttpServletRequest request) {
		UserService service = UserServiceFactory.getUserService();
		User user = service.getCurrentUser();
		
		if (user != null) {
			request.setAttribute("user", user);
			
			if (service.isUserAdmin()) {
				request.setAttribute("admin", true);
			}
		} else {
			request.setAttribute("admin", false);
		}
	}
	
	/**
	 * Gets the current User instance, calling init() if needed. If there is
	 * not currently a logged-in user, an instance of AnonymousUser will be
	 * returned instead. Otherwise, the User object associated with the session
	 * user will be returned. This method should never return null.
	 * @param request the current request
	 * @return a User instance
	 */
	public static User getUser(HttpServletRequest request) {
		init(request);
		return (User) request.getAttribute("user");
	}
	
	public static User getUser() {
		return UserServiceFactory.getUserService().getCurrentUser();
	}
	
	public static boolean isUserLoggedIn() {
		return UserServiceFactory.getUserService().isUserLoggedIn();
	}
	
	public static boolean isUserAdmin() {
		UserService service = UserServiceFactory.getUserService();
		
		// why this doesn't just return false when no user is logged in is
		// beyond me...
		return service.isUserLoggedIn() && service.isUserAdmin();
	}
	
}
