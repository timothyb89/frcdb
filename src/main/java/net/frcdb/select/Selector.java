package net.frcdb.select;

import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author tim
 */
public class Selector {
	
	private Context context;
	private Scriptable scope;
	private Scriptable thisObj;
	
	private String type;
	private Map<String, Function> filters;

	public Selector(Context context, Scriptable scope, Scriptable thisObj, String type) {
		this.context = context;
		this.scope = scope;
		this.thisObj = thisObj;
		this.type = type;
		
		filters = new HashMap<String, Function>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Function> getFilters() {
		return filters;
	}
	
	public void setFilter(String property, Function f) {
		filters.put(property, f);
	}
	
	/**
	 * Checks if the given object satisfies all of the filters in this
	 * {@code Selector}.
	 * @param obj the object to check
	 */
	public boolean passes(Object obj) {
		// check each filter
		for (String s : filters.keySet()) {
			// TODO: compare to the field `s` in obj
			
			Function f = filters.get(s);
			
			// execute the function
			Object v = f.call(context, scope, thisObj, new Object[] {obj});
			
			// convert the return value to a boolean
			Boolean pass = (Boolean) Context.jsToJava(v, Boolean.class);
			
			if (!pass) {
				return false;
			}
		}
		
		return true;
	}
	
}
