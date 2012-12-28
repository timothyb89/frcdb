package net.frcdb.content.rating;

import net.frcdb.content.Content;

/**
 * Defines functionality for content that can be rated. Rated content can be
 * sorted by its rating, which is the result of 
 * @author tim
 */
public interface Ratable extends Content, Comparable<Ratable> {
	
	/**
	 * Returns the number of positive ratings on this content
	 * @return the positive rating count
	 */
	public int getUpvotes();
	public void setUpvotes(int upvotes);
	public void upvote();
	
	/**
	 * Returns the number of negative ratings on this content.
	 * @return the negative rating count
	 */
	public int getDownvotes();
	public void setDownvotes(int downvotes);
	public void downvote();
	
	/**
	 * Calculates the score for this ratable content.
	 * @return the calculated score for this content
	 */
	public double getScore();

	/**
	 * Resets the rating for this content.
	 */
	public void resetRating();
	
}
