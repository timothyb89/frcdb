package net.frcdb.stats;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.frcdb.db.Database;
import net.frcdb.stats.chart.api.Chart;

/**
 *
 * @author tim
 */
@Entity
public class StatisticsRoot {

	public static final long ID = 3;
	
	@Id
	private Long id = ID;
	
	private String sitemapKey;

	private String teamsKey;
	private String eventsKey;
	
	/**
	 * A list of general charts. These are displayed at /stats
	 */
	private List<Key<Chart>> charts;
	
	public StatisticsRoot() {
		charts = new ArrayList<Key<Chart>>();
	}

	public String getSitemapKey() {
		return sitemapKey;
	}

	public void setSitemapKey(String sitemapKey) {
		this.sitemapKey = sitemapKey;
	}

	public String getTeamsKey() {
		return teamsKey;
	}

	public void setTeamsKey(String teamsKey) {
		this.teamsKey = teamsKey;
	}

	public String getEventsKey() {
		return eventsKey;
	}

	public void setEventsKey(String eventsKey) {
		this.eventsKey = eventsKey;
	}
	
	public Collection<Chart> getCharts() {
		return Database.ofy().load().keys(charts).values();
	}
	
	public List<Key<Chart>> getChartKeys() {
		return charts;
	}
	
	public void addChart(Chart c) {
		charts.add(Key.create(c));
	}
	
	/**
	 * Creates a key referencing this StatisticsRoot object.
	 * @return a key to the global StatisticsRoot object
	 */
	public static Key<StatisticsRoot> key() {
		return Key.create(null, StatisticsRoot.class, ID);
	}
	
	/**
	 * Gets the global StatisticsRoot object. If none currently exists in the
	 * database, a new one will be created and stored automatically. If
	 * possible, the value in memcache will be used automatically.
	 * @return a StatisticsRoot object
	 */
	public static StatisticsRoot get() {
		Ref<StatisticsRoot> ref = Database.ofy().load().key(key());
		StatisticsRoot root = ref.get();
		
		if (root == null) {
			root = new StatisticsRoot();
			Database.save().entity(root).now();
		}
		
		return root;
	}
	
}
