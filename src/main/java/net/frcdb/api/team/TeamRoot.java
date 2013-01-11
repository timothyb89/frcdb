package net.frcdb.api.team;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.frcdb.db.Database;

/**
 * A hacky meta object to give Teams a clean ID namespace and hold some global
 * stats.
 * @author tim
 */
@Cache
@Entity
public class TeamRoot {
	
	public static final long ID = 1;
	
	@Id
	private Long id = ID;
	
	private int count;

	public TeamRoot() {
	}

	public TeamRoot(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * Creates a key referencing this TeamRoot object.
	 * @return a key to the global TeamRoot object
	 */
	public static Key<TeamRoot> key() {
		return Key.create(null, TeamRoot.class, ID);
	}
	
	/**
	 * Gets the global TeamRoot object. If none currently exists in the
	 * database, a new one will be created and stored automatically. If
	 * possible, the value in memcache will be used automatically.
	 * @return a TeamRoot object
	 */
	public static TeamRoot get() {
		Ref<TeamRoot> ref = Database.ofy().load().key(key());
		TeamRoot root = ref.get();
		
		if (root == null) {
			root = new TeamRoot();
			Database.save().entity(root).now();
		}
		
		return root;
	}
	
}
