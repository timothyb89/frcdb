package net.frcdb.select;

import java.util.Map;
import java.util.Set;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author tim
 */
public class SelectFunction extends BaseFunction {

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Need exactly 2 arguments");
		}
		
		// get the selector type
		String type = (String) Context.jsToJava(args[0], String.class);
		NativeObject params = (NativeObject) args[1];
		
		Selector ret = new Selector(cx, scope, params, type);
		
		// get the filter functions
		for (Map.Entry<Object, Object> e : params.entrySet()) {
			String prop = (String) e.getKey();
			Function f = (Function) e.getValue();
			
			ret.setFilter(prop, f);
		}
		
		return ret;
	}
	
}
