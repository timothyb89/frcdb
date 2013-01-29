package net.frcdb.stats.chart;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.frcdb.api.event.EventRoot;
import net.frcdb.db.Database;

/**
 * A meta-object to provide a clean namespace for Chart objects.
 * @author tim
 */
@Entity
public class ChartRoot {
	
	public static final long ID = 4;
	
	@Id
	private Long id = ID;
	
	public ChartRoot() {
		
	}
	
	/**
	 * Creates a key referencing this ChartRoot object.
	 * @return a key to the global ChartRoot object
	 */
	public static Key<ChartRoot> key() {
		return Key.create(null, ChartRoot.class, ID);
	}
	
	/**
	 * Gets the global ChartRoot object. If none currently exists in the
	 * database, a new one will be created and stored automatically. If
	 * possible, the value in memcache will be used automatically.
	 * @return a ChartRoot object
	 */
	public static ChartRoot get() {
		Ref<ChartRoot> ref = Database.ofy().load().key(key());
		ChartRoot root = ref.get();
		
		if (root == null) {
			root = new ChartRoot();
			Database.save().entity(root).now();
		}
		
		return root;
	}
	
}
