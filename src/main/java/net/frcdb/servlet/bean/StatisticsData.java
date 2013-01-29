package net.frcdb.servlet.bean;

import java.util.Collection;
import net.frcdb.stats.chart.api.Chart;

/**
 *
 * @author tim
 */
public class StatisticsData {
	
	private Collection<Chart> charts;

	public StatisticsData() {
	}

	public StatisticsData(Collection<Chart> charts) {
		this.charts = charts;
	}

	public Collection<Chart> getCharts() {
		return charts;
	}

	public void setCharts(Collection<Chart> charts) {
		this.charts = charts;
	}
	
}
