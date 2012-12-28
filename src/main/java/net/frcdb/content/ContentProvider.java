package net.frcdb.content;

import java.util.List;

/**
 *
 * @author tjbuckley12
 */
public interface ContentProvider {
	
	/**
	 * The ORID of the class this class. Implementations should return the
	 * Object flagged with the @Id annotation.
	 * @return the ORID for this ContentProvider
	 */
	public Object getRecordId();
	
	/**
	 * Gets the content type for this provider. The content type should be the
	 * classname as used by the database for sql selects. It should contain
	 * a provider with the record id returned by getId()
	 * @return the content type
	 */
	public String getContentType();
	
	/**
	 * Gets all content associated with this ContentProvider
	 * @return a list of Content
	 */
	public List<Content> getContent();
	
	/**
	 * Adds the given content to this ContentProvider
	 * @param c the Content to add
	 */
	public void addContent(Content c);
	
	/**
	 * Removes the given content from this ContentProvider
	 * @param c the content to remove
	 */
	public void removeContent(Content c);
	
}
