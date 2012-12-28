package net.frcdb.content;

import java.util.Date;
import net.frcdb.content.moderation.Moderatable;
import net.frcdb.content.moderation.ModerationStatus;
import net.frcdb.content.rating.Ratable;

/**
 * A base implementation of content that is both moderated and rated.
 * @author tim
 */
public abstract class MRContent implements Moderatable, Ratable {

	private ModerationStatus moderationStatus;
	
	private String author;
	private Date date;
	private String description;
	
	private int upvotes;
	private int downvotes;

	public MRContent() {
		date = new Date();
	}
	
	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public ModerationStatus getModerationStatus() {
		return moderationStatus;
	}

	@Override
	public void setModerationStatus(ModerationStatus moderationStatus) {
		this.moderationStatus = moderationStatus;
	}
	
	@Override
	public int getUpvotes() {
		return upvotes;
	}

	@Override
	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}

	@Override
	public void upvote() {
		upvotes++;
	}
	
	@Override
	public int getDownvotes() {
		return downvotes;
	}

	@Override
	public void setDownvotes(int downvotes) {
		this.downvotes = downvotes;
	}

	@Override
	public void downvote() {
		downvotes++;
	}
	
	@Override
	public double getScore() {
		return upvotes - downvotes;
	}

	@Override
	public void resetRating() {
		upvotes = 0;
		downvotes = 0;
	}

	@Override
	public int compareTo(Ratable o) {
		double thisScore = getScore();
		double otherScore = o.getScore();
		
		if (thisScore > otherScore) {
			return 1;
		} else if (thisScore < otherScore) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
