package net.frcdb.content.moderation;

/**
 *
 * @author tim
 */
public enum ModerationStatus {
	
	APPROVED,
	DENIED,
	PENDING;
	
	public boolean isApproved() {
		return this == APPROVED;
	}
	
	public boolean isDenied() {
		return this == DENIED;
	}
	
	public boolean isPending() {
		return this == PENDING;
	}
	
}
