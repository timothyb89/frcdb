package net.frcdb.stats.calc;

import net.frcdb.api.event.EventRoot;
import net.frcdb.api.game.event.GameType;
import net.frcdb.db.Database;
import net.frcdb.stats.chart.EventTimeline;

/**
 * Initializes event timelines for all known years. This only creates timelines
 * for years that currently have no timelines; however, all timelines will be
 * regenerated.
 * @author tim
 */
public class EventTimelines implements GlobalStatistic {

	@Override
	public String[] getNames() {
		return new String[] {
			"eventtimelines",
			"event-timelines"
		};
	}

	@Override
	public void calculate() {
		EventRoot r = EventRoot.get();
		
		for (GameType t : GameType.values()) {
			int year = t.getYear();
			
			EventTimeline timeline = r.getTimeline(year);
			if (timeline == null) {
				timeline = new EventTimeline(year);
				r.putTimeline(year, timeline);
			}
			
			timeline.generate();
			
			Database.save().entity(timeline).now();
		}
		
		Database.save().entity(r);
	}
	
	@Override
	public String getBackendName() {
		return null;
	}
	
}
