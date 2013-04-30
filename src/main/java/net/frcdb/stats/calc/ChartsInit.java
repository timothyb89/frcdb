package net.frcdb.stats.calc;

import java.util.Collection;
import net.frcdb.db.Database;
import net.frcdb.stats.StatisticsRoot;
import net.frcdb.stats.chart.EventsPerTeamChart;
import net.frcdb.stats.chart.api.Chart;
import net.frcdb.stats.chart.OPRComparisonChart;
import net.frcdb.stats.chart.OPROverTimeChart;
import net.frcdb.stats.chart.TeamAgeVsOPRChart;
import net.frcdb.stats.chart.TravelChart;

/**
 * Initializes the global charts at /stats. They can be updated later with
 * the ChartManagementService.
 * @author tim
 */
public class ChartsInit implements GlobalStatistic {

	@Override
	public String[] getNames() {
		return new String[] {
			"chartsinit",
			"charts-init",
			"init-charts"
		};
	}
	
	@Override
	public void calculate() {
		StatisticsRoot s = StatisticsRoot.get();
		
		// clear old
		for (Chart c : s.getCharts()) {
			c.delete();
		}
		Database.ofy().delete().entities(s.getChartKeys()).now();
		s.getChartKeys().clear();
		
		// this is really hacky at the moment, and desperately needs to be
		// improved.
		
		//Chart c = new OPRComparisonChart(2012);
		//Database.save().entity(c).now();
		//s.addChart(c);
		
		Chart c = new TravelChart(2013);
		Database.save().entities(c).now();
		s.addChart(c);
		
		c = new OPROverTimeChart(2013);
		Database.save().entity(c).now();
		s.addChart(c);
		
		c = new EventsPerTeamChart(2012);
		Database.save().entity(c).now();
		s.addChart(c);
		
		c = new TeamAgeVsOPRChart(2012);
		Database.save().entity(c).now();
		s.addChart(c);
		
		Database.save().entity(s).now();
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
