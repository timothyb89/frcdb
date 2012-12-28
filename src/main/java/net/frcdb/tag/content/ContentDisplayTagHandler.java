package net.frcdb.tag.content;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import net.frcdb.content.CImage;
import net.frcdb.content.Content;
import net.frcdb.content.video.YouTubeVideo;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class ContentDisplayTagHandler extends SimpleTagSupport {
	
	private String cid;
	private String size;
	private Content content;

	/**
	 * Called by the container to invoke this tag. The implementation of this
	 * method is provided by the tag library developer, and handles all tag
	 * processing, body iteration, etc.
	 */
	@Override
	public void doTag() throws JspException {
		JspWriter out = getJspContext().getOut();
		
		try {
			if (content != null) {
				display(content, out);
			} else if (cid != null) {
				PageContext pc = (PageContext) getJspContext();
				HttpServletRequest request = (HttpServletRequest) pc.getRequest();
				Database db = (Database) request.getAttribute("database");

				display(db.getContent(cid), out);
			} else {
				out.println("No cid or content param provided.");
			}
		} catch (IOException ex) {
			throw new JspException("Error in ContentDisplayTagHandler tag", ex);
		}
	}
	
	private void display(Content c, JspWriter out) throws IOException {
		System.out.println("Displaying: " + c.getClass().getName());
		if (c == null) {
			out.println("Content not found: " + cid + "<br>");
		} else {
			if (c instanceof CImage) {
				displayImage(out, (CImage) c);
			} else if (c instanceof YouTubeVideo) {
				displayVideo(out, (YouTubeVideo) c);
			}
		}
	}
	
	private void displayImage(JspWriter out, CImage image) throws IOException {
		String extra = size == null ? "" : "/" + size;
		
		out.println(String.format("<a class=\"fbthumbnail %s\" href=\"%s\">"
				+ "<img src=\"%s\" alt=\"%s\">"
				+ "</a>",
				"fancybox.image",
				"/content/" + image.getId() + "/large",
				"/content/" + image.getId() + extra,
				image.getDescription()));
	}
	
	private void displayVideo(JspWriter out, YouTubeVideo video)
			throws IOException {
		
		out.println(String.format("<a class=\"fbthumbnail %s\" href=\"%s\">"
				+ "<img src=\"%s\" alt=\"%s\">"
				+ "</a>",
				"fancybox.iframe",
				"http://www.youtube.com/embed/" + video.getVideoId(),
				"/content/" + video.getId() + "/thumbnail",
				video.getTitle()));
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public void setContent(Content content) {
		this.content = content;
	}
	
}
