package net.frcdb.stats.chart;

import net.frcdb.stats.chart.api.YearChart;
import net.frcdb.stats.chart.api.Chart;
import com.fasterxml.jackson.core.JsonGenerator;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Ignore;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.game.event.element.GameOPRProvider;
import net.frcdb.api.game.event.element.OPRStatistics;
import net.frcdb.stats.chart.api.ColumnDefinition;
import net.frcdb.stats.chart.api.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
@EntitySubclass
public class OPROverTimeChart extends YearChart {

	@Ignore
	private Logger logger = LoggerFactory.getLogger(OPROverTimeChart.class);
	
	public OPROverTimeChart() {
	}

	public OPROverTimeChart(int year) {
		super(year);
	}
	
	@Override
	public String getName() {
		return "oprovertime";
	}

	@Override
	public String getDisplayName() {
		return "OPR Over Time";
	}

	@Override
	public String getChartType() {
		return Chart.TYPE_SCATTER;
	}

	@Override
	public ColumnDefinition[] getColumns() {
		return new ColumnDefinition[] {
			column().label("Event Date").type("date").get(),
			column().label("Mean OPR").type("number").get(),
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
			
			Date date = game.getStartDate();
			double mean = stats.getMean();
			String name = game.getEvent().getName()
					+ String.format(" (%.3f)", mean);
			
			ret.add(row(date.getTime(), mean, name));
		}
		
		return ret;
	}
	
	@Override
	public void writeOptions(JsonGenerator g) throws IOException {
		g.writeStringField("title", "OPR Over Time");
		g.writeStringField("theme", "maximized");
		
		g.writeObjectFieldStart("hAxis");
		g.writeStringField("title", "Date");
		g.writeEndObject();
		
		g.writeObjectFieldStart("vAxis");
		g.writeStringField("title", "Average Event OPR");
		g.writeEndObject();
		
		g.writeStringField("legend", "none");
	}
	
}
