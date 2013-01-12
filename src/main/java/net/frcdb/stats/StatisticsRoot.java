package net.frcdb.stats;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.frcdb.db.Database;

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

	public StatisticsRoot() {
	}

	public String getSitemapKey() {
		return sitemapKey;
	}

	public void setSitemapKey(String sitemapKey) {
		this.sitemapKey = sitemapKey;
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
