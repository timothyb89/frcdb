package net.frcdb.api.game.team.element;

/**
 *
 * @author tim
 */
public interface Seeding2007Provider {

	public int getWins();
	public void setWins(int wins);
	
	public int getLosses();
	public void setLosses(int losses);
	
	public int getTies();
	public void setTies(int ties);
	
	public float getQualificationScore();
	public void setQualificationScore(float rankingScore);

	public float getRankingScore();
	public void setRankingScore(float qualificationScore);

}
