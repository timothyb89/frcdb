package net.frcdb.tag.js;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author tim
 */
public class ChartTagHandler extends SimpleTagSupport {

	private String id;
	private String chartId;
	private String style;

	/**
	 * Called by the container to invoke this tag. The implementation of this
	 * method is provided by the tag library developer, and handles all tag
	 * processing, body iteration, etc.
	 */
	@Override
	public void doTag() throws JspException {
		JspWriter out = getJspContext().getOut();
		
		try {
			String containerId = id == null ? chartId : id;
			String i = "id=\"" + containerId + "\"";
			String s = style == null ? "" : " style=\"" + style + "\"";
			out.println("<div " + i + " class=\"chart\"" + s + "></div>");
			
			out.println("<script type=\"text/javascript\">");
			out.println("$(document).ready(function() {");
			out.println("    $.chart({");
			out.println("        chartId: \"" + chartId + "\",");
			out.println("        containerId: \"" + containerId + "\"");
			out.println("    });");
			out.println("});");
			out.println("</script>");
		} catch (IOException ex) {
			throw new JspException("Error in ChartTagHandler tag", ex);
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
}
