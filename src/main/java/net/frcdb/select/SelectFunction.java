package net.frcdb.select;

import java.util.HashMap;
import java.util.Map;
import net.frcdb.api.event.Event;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author tim
 */
public class SelectFunction extends BaseFunction {
	
	private static Map<String, Class> classMap = new HashMap<String, Class>() {{
		put("Event", Event.class);
		put("Game", Game.class);
		put("Team", Team.class);
	}};
	
	
	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Need exactly 2 arguments");
		}
		
		// get the selector type
		String type = (String) Context.jsToJava(args[0], String.class);
		Class typeClass = classMap.get(type);
		
		if (typeClass == null) {
			throw new IllegalArgumentException("Unknown type: " + type);
		}
		
		// TODO: map string type to a class
		
		NativeObject params = (NativeObject) args[1];
		
		Selector ret = new Selector(cx, scope, params, typeClass);
		
		// get the filter functions
		for (Map.Entry<Object, Object> e : params.entrySet()) {
			String prop = (String) e.getKey();
			Object f = e.getValue();
			
			ret.setFilter(prop, f);
		}
		
		return ret;
	}
	
}
