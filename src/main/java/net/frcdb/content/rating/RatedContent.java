package net.frcdb.content.rating;

/**
 * A base implementation for rated content. TODO: try to use a Bayesian average
 * instead?
 * @author tim
 */
public abstract class RatedContent implements Ratable {

	private int upvotes;
	private int downvotes;
	
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
