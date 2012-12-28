package net.frcdb.tag.js;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author tim
 */
public class InputTagHandler extends SimpleTagSupport {

	private String type = "text";
	private String name;
	private String size = "20";
	private String value;
	private String autocomplete;
	private String clazz;

	/**
	 * Called by the container to invoke this tag.
	 * The implementation of this method is provided by the tag library developer,
	 * and handles all tag processing, body iteration, etc.
	 *
	 * @throws JspException On error
	 */
	@Override
	public void doTag() throws JspException {
		JspWriter out = getJspContext().getOut();

		try {
			String params = "";
			if (type != null) {
				params += " type=\"" + type + "\"";
			} else {
				params += " type=\"text\"";
			}

			if (value != null) {
				params += " value=\"" + value + "\"";
				params += " onclick=\"textField_clear(this)\" onblur=\"textField_clear(this)\"";
			} else {
				params += " value=\"\"";
			}

			if (size != null) {
				params += " size=\"" + size + "\"";
			} else {
				params += " size=\"20\"";
			}

			if (clazz != null) {
				params += " class=\"" + clazz + "\"";
			}

			if (autocomplete != null) {
				params += " autocomplete=\"off\"";
			}

			out.println("<input name=\"" + name + "\"" + params  + ">");

			if (autocomplete != null) {
				out.println(""
						+ "<script type=\"text/javascript\">\n"
						+ "    $(\"input[name='" + name + "']\").autocomplete(\n"
						+ "        \"" + autocomplete + "\", {\n"
						+ "            matchContains: true,\n"
						+ "            delay: 200\n"
						+ "    });\n"
						+ "</script>");
			}
		} catch (IOException ex) {
			throw new JspException("Error in InputTagHandler tag", ex);
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}



}
