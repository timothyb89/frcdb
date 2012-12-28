package net.frcdb.content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * TODO: This won't work with objectify. Content needs to be an @Entity for
 * inheritance purposes, so RatedContent and ModeratedContent should probably
 * just be merged into a single Content class.
 * Defines the requirements for content that may be displayed (via HTML)
 * @author tim
 */
public interface Content {
	
	/**
	 * Returns the content ID. This must be a unique string that can identify
	 * this specific content.
	 * @return a string that uniquely identifies this content
	 */
	public String getId();
	
	/**
	 * Gets the author for this content
	 * @return the name of the author of this content
	 */
	public String getAuthor();
	
	public void setAuthor(String user);
	
	/**
	 * Gets the creation or upload date for this content.
	 */
	public Date getDate();
	
	public void setDate(Date date);
	
	/**
	 * Gets the (possibly user-generated) description of this content.
	 * @return a description for this content
	 */
	public String getDescription();
	public void setDescription(String description);
	
	/**
	 * Gets the mimetype (e.g., text/html, image/jpeg, etc) for this type
	 * of content.
	 * @param pathExtra extra path information, if any
	 * @return the mime type
	 */
	public String getContentType(String pathExtra);
	
	/**
	 * Outputs this content. This should the target datatype directly if
	 * needed, such as the actual image data, plain text, etc.
	 * @param out the OutputStream to write to
	 * @param pathExtra any extra path info, excluding leading or trailing "/"
	 *     marks
	 */
	public void serve(OutputStream out, String pathExtra) throws IOException;
	
}
