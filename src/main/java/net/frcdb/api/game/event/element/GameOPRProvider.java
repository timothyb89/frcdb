package net.frcdb.api.game.event.element;

import net.frcdb.api.game.team.TeamEntry;

/**
 *
 * @author tim
 */
public interface GameOPRProvider {

	public double getAverageOPR();
	public void setAverageOPR(double averageOPR);
	
	public double getAverageDPR();
	public void setAverageDPR(double averageDPR);
	
	public double getTotalOPR();
	public void setTotalOPR(double totalOPR);
	
	public double getTotalDPR();
	public void setTotalDPR(double totalDPR);
	
	public double getHighestOPR();
	public void setHighestOPR(double highestOPR);
	public TeamEntry getHighestOPRTeam();
	public void setHighestOPRTeam(TeamEntry entry);
	
	public double getLowestOPR();
	public void setLowestOPR(double lowestOPR);
	public TeamEntry getLowestOPRTeam();
	public void setLowestOPRTeam(TeamEntry entry);
	
	public OPRStatistics getOPRStatistics();
	public void setOPRStatistics(OPRStatistics oprStatistics);
	
}
