package net.frcdb.stats.calc;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.QueryKeys;
import java.util.Collection;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repairs missing references in game lists 
 * @author tim
 */
public class ReferenceRepair implements EventStatistic {

	private Logger logger = LoggerFactory.getLogger(ReferenceRepair.class);
	
	@Override
	public String[] getNames() {
		return new String[] { "referencerepair", "repairreferences" };
	}
	
	@Override
	public void calculate(Event event) {
		// get all child games of the given event (ancestor query)
		QueryKeys<Game> keys = Database.ofy().load()
				.type(Game.class)
				.ancestor(event)
				.keys();
		
		boolean changed = false;
		
		for (Key<Game> key : keys) {
			if (!hasReference(event, key)) {
				event.getGameReferences().add(Ref.create(key));
				changed = true;
				
				logger.info("Repaired reference to " + event.getShortName() +
						" year " + key.getId());
			}
		}
		
		if (changed) {
			// don't waste writes
			Database.save().entity(event);
		}
	}
	
	private boolean hasReference(Event e, Key<Game> key) {
		for (Ref<Game> r : e.getGameReferences()) {
			if (r.equivalent(key)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getBackendName() {
		return null;
	}
	
}
