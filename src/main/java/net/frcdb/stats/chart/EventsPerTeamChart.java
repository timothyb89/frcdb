package net.frcdb.stats.chart;

import com.fasterxml.jackson.core.JsonGenerator;
import com.googlecode.objectify.annotation.EntitySubclass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.frcdb.api.team.Team;
import net.frcdb.db.Database;
import net.frcdb.stats.chart.api.Chart;
import net.frcdb.stats.chart.api.ColumnDefinition;
import net.frcdb.stats.chart.api.Row;
import net.frcdb.stats.chart.api.YearChart;

/**
 *
 * @author tim
 */
@EntitySubclass
public class EventsPerTeamChart extends YearChart {

	public EventsPerTeamChart() {
	}

	public EventsPerTeamChart(int year) {
		super(year);
	}
	
	@Override
	public String getName() {
		return "events-per-team";
	}

	@Override
	public String getDisplayName() {
		return "Events Per Team";
	}

	@Override
	public String getChartType() {
		return Chart.TYPE_COLUMN;
	}

	@Override
	public ColumnDefinition[] getColumns() {
		return new ColumnDefinition[] {
			column().label("Event Count").type("number").get(),
			column().label("Frequency").type("number").get()
		};
	}

	@Override
	public List<Row> getRows() {
		List<Row> ret = new ArrayList<Row>();
		
		Map<Integer, Integer> freqs = new HashMap<Integer, Integer>();
		for (Team t : Database.getInstance().getTeams()) {
			int count = Database.getInstance().countEntries(t, getYear());
			
			int oldFreq = freqs.containsKey(count) ? freqs.get(count) : 0;
			freqs.put(count, oldFreq + 1);
		}
		
		for (int count : freqs.keySet()) {
			ret.add(row(count, freqs.get(count)));
		}
		
		return ret;
	}
	
	@Override
	public void writeOptions(JsonGenerator g) throws IOException {
		g.writeStringField("title", "Average OPR vs Total");
		
		g.writeObjectFieldStart("hAxis");
		g.writeStringField("title", "# Events Attended");
		g.writeEndObject();
		
		g.writeObjectFieldStart("vAxis");
		g.writeStringField("title", "Frequency");
		g.writeEndObject();
		
		g.writeStringField("legend", "none");
	}
	
}
