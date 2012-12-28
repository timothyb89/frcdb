package net.frcdb.util;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 *
 * @author tim
 */
public class GAEUserUtil {
	
	public static void main(String[] args) {
		UserService s = UserServiceFactory.getUserService();
		
	}
	
}
