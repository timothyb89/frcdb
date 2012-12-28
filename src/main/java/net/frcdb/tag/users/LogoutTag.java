package net.frcdb.tag.users;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author tim
 */
public class LogoutTag extends SimpleTagSupport {
	
	private String url;
	private String text;

	/**
	 * Called by the container to invoke this tag. The implementation of this
	 * method is provided by the tag library developer, and handles all tag
	 * processing, body iteration, etc.
	 */
	@Override
	public void doTag() throws JspException {
		JspWriter out = getJspContext().getOut();
		
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		
		String redirect = url;
		if (redirect == null) {
			//redirect = request.getRequestURI();
			
			// use "hidden" uri to account for jsp forwards
			redirect = (String) request.getAttribute(
					"javax.servlet.forward.request_uri");
		}
		
		String label = text;
		if (label == null) {
			label = "Logout";
		}
		
		UserService service = UserServiceFactory.getUserService();
		String dest = service.createLogoutURL(redirect);
		
		try {
			out.print("<a href=\"");
			out.print(dest);
			out.print("\">");
			out.print(label);
			out.print("</a>");
		} catch (java.io.IOException ex) {
			throw new JspException("Error in LoginTag tag", ex);
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
