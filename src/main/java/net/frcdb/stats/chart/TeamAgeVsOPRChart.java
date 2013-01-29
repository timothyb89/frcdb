package net.frcdb.stats.chart;

import com.fasterxml.jackson.core.JsonGenerator;
import com.googlecode.objectify.annotation.EntitySubclass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frcdb.api.team.Team;
import net.frcdb.api.team.TeamStatistics;
import net.frcdb.db.Database;
import net.frcdb.stats.chart.api.ColumnDefinition;
import net.frcdb.stats.chart.api.Row;
import net.frcdb.stats.chart.api.YearChart;

/**
 *
 * @author tim
 */
@EntitySubclass
public class TeamAgeVsOPRChart extends YearChart {

	public TeamAgeVsOPRChart() {
	}

	public TeamAgeVsOPRChart(int year) {
		super(year);
	}
	
	@Override
	public String getName() {
		return "team-number-vs-opr";
	}

	@Override
	public String getDisplayName() {
		return "Team Age vs OPR";
	}

	@Override
	public String getChartType() {
		return "ScatterChart";
	}

	@Override
	public ColumnDefinition[] getColumns() {
		return new ColumnDefinition[] {
			column().label("Team Age").type("number").get(),
			column().label("Team OPR").type("number").get(),
			column().label("Team").type("string").role("tooltip").get()
		};
	}

	@Override
	public List<Row> getRows() {
		List<Row> ret = new ArrayList<Row>();
		
		for (Team t : Database.getInstance().getTeams()) {
			TeamStatistics stats = t.getStatistics(getYear());
			if (stats == null) {
				continue;
			}
			
			int age = getYear() - t.getRookieSeason();
			double meanOpr = stats.getOprMean();
			if (meanOpr == Double.NaN || String.valueOf(meanOpr).equals("NaN")) {
				continue;
			}
			
			String team = String.format("#%d (%.3f)", t.getNumber(), meanOpr);
			
			ret.add(row(age, meanOpr, team));
		}
		
		return ret;
	}
	
	@Override
	public void writeOptions(JsonGenerator g) throws IOException {
		g.writeStringField("title", "Team Age vs OPR");
		g.writeStringField("theme", "maximized");
		
		g.writeObjectFieldStart("hAxis");
		g.writeStringField("title", "# Events Attended");
		g.writeEndObject();
		
		g.writeObjectFieldStart("vAxis");
		g.writeStringField("title", "Frequency");
		g.writeEndObject();
		
		g.writeStringField("legend", "none");
	}
	
}
