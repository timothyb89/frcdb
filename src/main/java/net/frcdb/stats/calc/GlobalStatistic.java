package net.frcdb.stats.calc;

/**
 * A statistic that should apply to all games for a given year
 * @author tim
 */
public interface GlobalStatistic extends Statistic {

	/**
	 * Applies this statistic 
	 */
	public void calculate();

}
