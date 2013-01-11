package net.frcdb.stats.calc;

import net.frcdb.api.event.Event;

/**
 *
 * @author tim
 */
public interface EventStatistic extends Statistic {
	
	public void calculate(Event event);
	
}
