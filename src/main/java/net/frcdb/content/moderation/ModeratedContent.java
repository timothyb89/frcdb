package net.frcdb.content.moderation;


/**
 * A base implementation for moderated content
 * @author tim
 */
public abstract class ModeratedContent implements Moderatable {

	private ModerationStatus status;

	@Override
	public ModerationStatus getModerationStatus() {
		return status;
	}

	@Override
	public void setModerationStatus(ModerationStatus status) {
		this.status = status;
	}
	
}
