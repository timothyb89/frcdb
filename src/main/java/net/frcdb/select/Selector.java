package net.frcdb.select;

import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.frcdb.db.Database;
import net.frcdb.select.filter.DatabaseFilter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class Selector {
	
	private Logger logger = LoggerFactory.getLogger(Selector.class);
	
	private Context context;
	private Scriptable scope;
	private Scriptable thisObj;
	
	private Class<?> type;
	
	private List<DatabaseFilter> dbFilters;
	private Map<String, Function> filters;

	public Selector(Context context, Scriptable scope,
			Scriptable thisObj, Class<?> type) {
		this.context = context;
		this.scope = scope;
		this.thisObj = thisObj;
		this.type = type;
		
		dbFilters = new ArrayList<DatabaseFilter>();
		filters = new HashMap<String, Function>();
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Scriptable getScope() {
		return scope;
	}

	public void setScope(Scriptable scope) {
		this.scope = scope;
	}

	public Scriptable getThisObj() {
		return thisObj;
	}

	public void setThisObj(Scriptable thisObj) {
		this.thisObj = thisObj;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Map<String, Function> getFilters() {
		return filters;
	}
	
	public void setFilter(String property, Object f) {
		if (f instanceof DatabaseFilter) {
			DatabaseFilter dbf = (DatabaseFilter) f;
			
			// set the property name here since it wasn't known at eval time
			dbf.setProperty(property);
			
			dbFilters.add(dbf);
		} else {
			filters.put(property, (Function) f);
		}
	}
	
	/**
	 * Checks if the given object passes all of the filters in this 
	 * {@code Selector}. Note that only normal (function-based) filters are
	 * checked here, database filters are only applied at fetching time and will
	 * not be considered here.
	 * @param obj the object to check
	 */
	public boolean passes(Object obj) {
		for (Entry<String, Function> entry : filters.entrySet()) {
			String prop = entry.getKey();
			Function func = entry.getValue();
			
			Object o = getValue(obj, prop);

			Object ret = func.call(
					getContext(), getScope(), getThisObj(), new Object[] {o});
			
			// convert the return value to a boolean
			Boolean pass = (Boolean) Context.jsToJava(ret, Boolean.class);
			
			if (!pass) {
				return false;
			}
		}
		
		return true;
	}
	
	private Object getValue(Object o, String prop) {
		try {
			BeanInfo info = Introspector.getBeanInfo(o.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if (prop.equals(pd.getName())) {
					Method read = pd.getReadMethod();
					return read.invoke(o);
				}
			}
		} catch (Exception ex) {
			logger.error("Failed to check property: " + prop, ex);
		}
		
		return null;
	}
	
	public Collection<Object> fetchDataset() {
		LoadType l = Database.ofy().load().type(type);
		
		// if there's no db filters, just load all of the type
		if (dbFilters.isEmpty()) {
			return Database.ofy().load().keys(l.keys()).values();
		} else {
			Query q = null;
			// apply each filter
			for (DatabaseFilter f : dbFilters) {
				if (q == null) {
					q = f.apply(l);
				} else {
					q = f.apply(q);
				}
			}

			return Database.ofy().load().keys(q.keys()).values();
		}
	}
	
	/**
	 * Fetches all objects from the database that match the parameters
	 * configured in this selector. 
	 * @return a list of matching objects
	 */
	public List<Object> fetchMatching() {
		Collection<Object> candidates = fetchDataset();
		
		List<Object> ret = new ArrayList<Object>();
		for (Object o : candidates) {
			if (passes(o)) {
				ret.add(o);
			}
		}
		
		return ret;
	}
	
}
