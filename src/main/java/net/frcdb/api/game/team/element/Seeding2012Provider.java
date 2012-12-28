package net.frcdb.api.game.team.element;

/**
 *
 * @author tim
 */
public interface Seeding2012Provider {
	
	/**
	 * Gets the qualification score for a team. "QS" on the standings page.
	 * @return the qualification score for a team
	 */
	public float getQualificationScore();
	public void setQualificationScore(float qualificationScore);
	
	/**
	 * Gets the total points scored by a team during the hybrid period. "HP"
	 * on the standings page.
	 * @return the number of hybrid points scored by a team
	 */
	public float getHybridPoints();
	public void setHybridPoints(float hybridPoints);
	
	/**
	 * Gets the total bridge points scored by a team during a particular game.
	 * "BP" on the standings page.
	 * @return the bridge point total
	 */
	public float getBridgePoints();
	public void setBridgePoints(float bridgePoints);
	
	/**
	 * TP
	 * @return 
	 */
	public float getTeleopPoints();
	public void setTeleopPoints(float teleopPoints);
	
	/**
	 * CP
	 * @return 
	 */
	public int getCoopertitionPoints();
	public void setCoopertitionPoints(int cp);

	/**
	 * DQ
	 * @return 
	 */
	public int getDisqualifications();
	public void setDisqualifications(int disqualifications);
	
	public int getWins();
	public void setWins(int wins);
	
	public int getLosses();
	public void setLosses(int losses);
	
	public int getTies();
	public void setTies(int ties);
	
}
