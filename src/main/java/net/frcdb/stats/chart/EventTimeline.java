package net.frcdb.stats.chart;

import com.fasterxml.jackson.core.JsonGenerator;
import com.googlecode.objectify.annotation.EntitySubclass;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.stats.chart.api.ColumnDefinition;
import net.frcdb.stats.chart.api.Row;
import net.frcdb.stats.chart.api.YearChart;

/**
 *
 * @author tim
 */
@EntitySubclass
public class EventTimeline extends YearChart {

	public EventTimeline() {
	}

	public EventTimeline(int year) {
		super(year);
	}
	
	@Override
	public String getName() {
		return "event-timeline";
	}

	@Override
	public String getDisplayName() {
		return "Event Timeline";
	}

	@Override
	public String getChartType() {
		return "Timeline";
	}

	@Override
	public ColumnDefinition[] getColumns() {
		return new ColumnDefinition[] {
			column().label("Start").type("date").get(),
			column().label("End").type("date").get(),
			column().label("Content").type("string").get()
		};
	}

	@Override
	public List<Row> getRows() {
		List<Row> ret = new ArrayList<Row>();
		
		for (Game game : getGames()) {
			ret.add(row(
					game.getStartDate().getTime(),
					game.getEndDate().getTime(),
					game.getEvent().getName()));
		}
		
		return ret;
	}

	@Override
	public void writeOptions(JsonGenerator g) throws IOException {
		g.writeBooleanField("showNavigation", true);
		g.writeStringField("height", "300px");
		g.writeNumberField("eventMargin", 5);
	}
	
}
