package net.frcdb.api.event;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.frcdb.api.team.TeamRoot;
import net.frcdb.db.Database;

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

	public EventRoot() {
	}

	public EventRoot(int eventCount, int gameCount) {
		this.eventCount = eventCount;
		this.gameCount = gameCount;
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
