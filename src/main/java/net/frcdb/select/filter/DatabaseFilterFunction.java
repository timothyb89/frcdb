package net.frcdb.select.filter;

import com.googlecode.objectify.cmd.Loader;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * A JS wrapper function to return a DatabaseFilter 
 * @author tim
 */
public class DatabaseFilterFunction extends BaseFunction {

	@Override
	public Object call(
			Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		String condition = (String) Context.jsToJava(args[0], String.class);
		Object value = args[1];
		
		return new DatabaseFilter(condition, value);
	}
	
}
