package net.frcdb.stats.chart;

import com.fasterxml.jackson.core.JsonGenerator;
import com.googlecode.objectify.annotation.EntitySubclass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.team.TeamEntry;
import net.frcdb.api.team.Team;
import net.frcdb.stats.chart.api.Chart;
import net.frcdb.stats.chart.api.ColumnDefinition;
import net.frcdb.stats.chart.api.Row;
import net.frcdb.stats.chart.api.YearChart;

/**
 * A chart to display the most common sources of teams that travel out of
 * state.
 * @author tim
 */
@EntitySubclass
public class TravelSourceChart extends YearChart {

	public TravelSourceChart() {
	}

	public TravelSourceChart(int year) {
		super(year);
	}
	
	@Override
	public String getName() {
		return "team-travel-source-chart";
	}

	@Override
	public String getDisplayName() {
		return "Team Travel Sources";
	}

	@Override
	public String getChartType() {
		return Chart.TYPE_GEO;
	}

	@Override
	public ColumnDefinition[] getColumns() {
		return new ColumnDefinition[] {
			column().label("State").type("string").get(),
			column().label("Size").type("number").get(),
			column().label("Description").type("string").role("tooltip").get()
		};
	}

	@Override
	public List<Row> getRows() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		
		// iterate over all team entries
		for (Game g : getGames()) {
			Event e = g.getEvent();
			
			for (TeamEntry entry : g.getTeams()) {
				String s = entry.getTeam().getState();
				// count if the event is out of state
				if (!s.equals(e.getState())) {
					if (counts.containsKey(s)) {
						counts.put(s, counts.get(s) + 1);
					} else {
						counts.put(s, 1);
					}
				}
			}
		}
		
		List<Row> ret = new ArrayList<Row>();
		
		for (Entry<String, Integer> entry : counts.entrySet()) {
			String s = entry.getKey();
			int c = entry.getValue();
			
			ret.add(row(
					s,
					c,
					s + ": " + c + " team" + (c != 1 ? "s" : "")));
		}
		
		return ret;
	}

	@Override
	public void writeOptions(JsonGenerator g) throws IOException {
		g.writeStringField("title", "Team Travel Sources");
		g.writeStringField("displayMode", "markers");
		g.writeStringField("region", "US");
		
		g.writeObjectFieldStart("colorAxis");
		g.writeArrayFieldStart("colors");
		g.writeString("green");
		g.writeString("blue");
		g.writeEndArray();
		g.writeEndObject();
	}
	
}
