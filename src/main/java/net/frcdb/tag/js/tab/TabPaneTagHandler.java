package net.frcdb.tag.js.tab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *
 * @author tim
 */
public class TabPaneTagHandler extends BodyTagSupport {

	private String id;
	private String style;
	private List<Tab> tabs;

	/** 
	 * Creates new instance of tag handler 
	 */
	public TabPaneTagHandler() {
		super();
	}

	////////////////////////////////////////////////////////////////
	///                                                          ///
	///   User methods.                                          ///
	///                                                          ///
	///   Modify these methods to customize your tag handler.    ///
	///                                                          ///
	////////////////////////////////////////////////////////////////
	/**
	 * Method called from doStartTag().
	 * Fill in this method to perform other operations from doStartTag().
	 */
	private void otherDoStartTagOperations() {
		tabs = new ArrayList<Tab>();
	}

	/**
	 * Method called from doEndTag()
	 * Fill in this method to perform other operations from doEndTag().
	 */
	private void otherDoEndTagOperations() {
		
	}

	/**
	 * Fill in this method to process the body content of the tag.
	 * You only need to do this if the tag's BodyContent property
	 * is set to "JSP" or "tagdependent."
	 * If the tag's bodyContent is set to "empty," then this method
	 * will not be called.
	 */
	private void writeTagBodyContent(JspWriter out, BodyContent bodyContent) throws IOException {
		int selected = 0;
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).isSelected()) {
				selected = i;
			}
		}
		
		String sty = style == null ? "" : " style=\"" + style + "\"";
		
		out.println("<div id=\"" + id + "\"" + sty + ">");
		out.println("\t<ul>");
		for (Tab tab : tabs) {
			out.println("\t\t<li><a href=\"#" + tab.getId() + "\">" 
					+ tab.getTitle() + "</a></li>");
			
		}
		out.println("\t</ul>");
		
		for (Tab tab : tabs) {
			out.println("\t<div id=\"" + tab.getId() + "\">");
			out.println(tab.getContent());
			out.println("\t</div>");
		}
		out.println("</div>");
		
		out.println("<script type=\"text/javascript\">");
		out.println("$(function() {");
		out.println("    $(\"#" + id + "\").tabs({selected: " + selected + "});");
		out.println("});");
		out.println("</script>");
		
		// clear the body content for the next time through.
		bodyContent.clearBody();
	}

	////////////////////////////////////////////////////////////////
	///                                                          ///
	///   Tag Handler interface methods.                         ///
	///                                                          ///
	///   Do not modify these methods; instead, modify the       ///
	///   methods that they call.                                ///
	///                                                          ///
	////////////////////////////////////////////////////////////////
	/**
	 * This method is called when the JSP engine encounters the start tag,
	 * after the attributes are processed.
	 * Scripting variables (if any) have their values set here.
	 * @return EVAL_BODY_BUFFERED if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
	 * This method is automatically generated. Do not modify this method.
	 * Instead, modify the methods that this method calls.
	 */
	@Override
	public int doStartTag() throws JspException {
		otherDoStartTagOperations();

		if (theBodyShouldBeEvaluated()) {
			return EVAL_BODY_BUFFERED;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * This method is called after the JSP engine finished processing the tag.
	 * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP 
	 * page, otherwise return SKIP_PAGE.
	 * This method is automatically generated. Do not modify this method.
	 * Instead, modify the methods that this method calls.
	 */
	@Override
	public int doEndTag() throws JspException {
		otherDoEndTagOperations();

		if (shouldEvaluateRestOfPageAfterEndTag()) {
			return EVAL_PAGE;
		} else {
			return SKIP_PAGE;
		}
	}

	/**
	 * This method is called after the JSP engine processes the body content 
	 * of the tag.
	 * @return EVAL_BODY_AGAIN if the JSP engine should evaluate the tag body 
	 *     again, otherwise return SKIP_BODY.
	 * This method is automatically generated. Do not modify this method.
	 * Instead, modify the methods that this method calls.
	 */
	@Override
	public int doAfterBody() throws JspException {
		try {
			// This code is generated for tags whose bodyContent is "JSP"
			BodyContent bodyCont = getBodyContent();
			JspWriter out = bodyCont.getEnclosingWriter();

			writeTagBodyContent(out, bodyCont);
		} catch (Exception ex) {
			handleBodyContentException(ex);
		}

		if (theBodyShouldBeEvaluatedAgain()) {
			return EVAL_BODY_AGAIN;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * Handles exception from processing the body content.
	 */
	private void handleBodyContentException(Exception ex) throws JspException {
		// Since the doAfterBody method is guarded, place exception handing code here.
		throw new JspException("Error in TabPaneTagHandler tag", ex);
	}

	/**
	 * Fill in this method to determine if the rest of the JSP page
	 * should be generated after this tag is finished.
	 * Called from doEndTag().
	 */
	private boolean shouldEvaluateRestOfPageAfterEndTag() {
		// TODO: code that determines whether the rest of the page
		//       should be evaluated after the tag is processed
		//       should be placed here.
		//       Called from the doEndTag() method.
		//
		return true;
	}

	/**
	 * Fill in this method to determine if the tag body should be evaluated
	 * again after evaluating the body.
	 * Use this method to create an iterating tag.
	 * Called from doAfterBody().
	 */
	private boolean theBodyShouldBeEvaluatedAgain() {
		// TODO: code that determines whether the tag body should be
		//       evaluated again after processing the tag
		//       should be placed here.
		//       You can use this method to create iterating tags.
		//       Called from the doAfterBody() method.
		//
		return false;
	}

	private boolean theBodyShouldBeEvaluated() {
		// TODO: code that determines whether the body should be
		//       evaluated should be placed here.
		//       Called from the doStartTag() method.
		return true;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public List<Tab> getTabs() {
		return tabs;
	}
	
	public void addTab(Tab tab) {
		tabs.add(tab);
	}
	
	public void removeTab(Tab tab) {
		tabs.remove(tab);
	}
	
}
