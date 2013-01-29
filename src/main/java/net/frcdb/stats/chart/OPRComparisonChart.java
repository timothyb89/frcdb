package net.frcdb.stats.chart;

import com.fasterxml.jackson.core.JsonGenerator;
import net.frcdb.stats.chart.api.YearChart;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Ignore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.event.element.GameOPRProvider;
import net.frcdb.api.game.event.element.OPRStatistics;
import net.frcdb.stats.chart.api.ColumnDefinition;
import net.frcdb.stats.chart.api.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scatterplot illustrating average opr vs total opr across all events.
 * @author tim
 */
@EntitySubclass
public class OPRComparisonChart extends YearChart {

	@Ignore
	private Logger logger = LoggerFactory.getLogger(OPRComparisonChart.class);
	
	public OPRComparisonChart() {
	}

	public OPRComparisonChart(int year) {
		super(year);
	}
	
	@Override
	public String getName() {
		return "opr-share-global";
	}

	@Override
	public String getDisplayName() {
		return "Overall Average OPR vs Total";
	}

	@Override
	public String getChartType() {
		return "ScatterChart";
	}

	@Override
	public ColumnDefinition[] getColumns() {
		return new ColumnDefinition[] {
			column().label("Total OPR").type("number").get(),
			column().label("Average OPR").type("number").get(),
			column().label("Event Name").type("string").role("tooltip").get()
		};
	}

	@Override
	public List<Row> getRows() {
		List<Row> ret = new ArrayList<Row>();
		
		for (Game game : getGames()) {
			if (!(game instanceof GameOPRProvider)) {
				continue;
			}
			
			GameOPRProvider opr = (GameOPRProvider) game;
			OPRStatistics stats = opr.getOPRStatistics();
			if (stats == null) {
				logger.warn("Skipping event without OPR data: " + game);
				continue;
			}
			
			// normalize for team count
			double total = stats.getSum();
			double mean = stats.getMean();
			
			ret.add(row(total, mean, game.getEvent().getName()));
		}
		
		return ret;
	}

	@Override
	public void writeOptions(JsonGenerator g) throws IOException {
		g.writeStringField("title", "Average OPR vs Total");
		g.writeStringField("theme", "maximized");
		
		g.writeObjectFieldStart("hAxis");
		g.writeStringField("title", "Total OPR");
		g.writeEndObject();
		
		g.writeObjectFieldStart("vAxis");
		g.writeStringField("title", "Average OPR");
		g.writeEndObject();
		
		g.writeStringField("legend", "none");
	}
	
}
