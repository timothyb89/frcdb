package net.frcdb.content.moderation;

import net.frcdb.content.Content;

/**
 * Defines the functionality for moderatable content. Denied content should be
 * occasionally pruned. This can be to the different tables via SQL as such:
 * <code>
 * delete from [Cluster] where moderationStatus = DENIED
 * </code>
 * @author tim
 */
public interface Moderatable extends Content {
	
	/**
	 * Gets the status of this moderation.
	 * @return the current moderation status
	 */
	public ModerationStatus getModerationStatus();
	
	/**
	 * Set the moderation status of this content.
	 * @param status the status to set
	 */
	public void setModerationStatus(ModerationStatus status);
	
}
