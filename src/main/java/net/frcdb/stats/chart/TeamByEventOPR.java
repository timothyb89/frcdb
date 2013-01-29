package net.frcdb.stats.chart;

import java.util.List;
import net.frcdb.stats.chart.api.ColumnDefinition;
import net.frcdb.stats.chart.api.Row;
import net.frcdb.stats.chart.api.YearChart;

/**
 *
 * @author tim
 */
public class TeamByEventOPR extends YearChart {

	@Override
	public String getName() {
		return "team-vs-event-opr";
	}

	@Override
	public String getDisplayName() {
		return "Team vs Event OPR";
	}

	@Override
	public String getChartType() {
		return "ScatterChart";
	}

	@Override
	public ColumnDefinition[] getColumns() {
		return new ColumnDefinition[] {
			// todo
		};
	}

	@Override
	public List<Row> getRows() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
