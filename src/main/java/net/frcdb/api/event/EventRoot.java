package net.frcdb.api.event;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.HashMap;
import java.util.Map;
import net.frcdb.db.Database;
import net.frcdb.stats.chart.EventTimeline;

/**
 * A meta-object for some basic overall stats (counts) and to give event IDs
 * a clean namespace
 * @author tim
 */
@Entity
public class EventRoot {
	
	public static final long ID = 2;
	
	@Id
	private Long id = ID;
	
	private int eventCount;
	private int gameCount;

	// objectify requires strings
	private Map<String, Key<EventTimeline>> timelines;
	
	public EventRoot() {
		timelines = new HashMap<String, Key<EventTimeline>>();
	}

	public EventRoot(int eventCount, int gameCount) {
		this.eventCount = eventCount;
		this.gameCount = gameCount;
		
		timelines = new HashMap<String, Key<EventTimeline>>();
	}

	public int getEventCount() {
		return eventCount;
	}

	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}
	
	public void putTimeline(int year, EventTimeline timeline) {
		timelines.put(String.valueOf(year), Key.create(timeline));
	}
	
	public Key<EventTimeline> getTimelineKey(int year) {
		return timelines.get(String.valueOf(year));
	}

	public EventTimeline getTimeline(int year) {
		Key<EventTimeline> key = getTimelineKey(year);
		if (key == null) {
			return null;
		}
		
		return Database.ofy().load().key(key).get();
	}
	
	public Map<String, Key<EventTimeline>> getTimelines() {
		return timelines;
	}
	
	/**
	 * Creates a key referencing this EventRoot object.
	 * @return a key to the global EventRoot object
	 */
	public static Key<EventRoot> key() {
		return Key.create(null, EventRoot.class, ID);
	}
	
	/**
	 * Gets the global EventRoot object. If none currently exists in the
	 * database, a new one will be created and stored automatically. If
	 * possible, the value in memcache will be used automatically.
	 * @return a EventRoot object
	 */
	public static EventRoot get() {
		Ref<EventRoot> ref = Database.ofy().load().key(key());
		EventRoot root = ref.get();
		
		if (root == null) {
			root = new EventRoot();
			Database.save().entity(root).now();
		}
		
		return root;
	}
	
}
