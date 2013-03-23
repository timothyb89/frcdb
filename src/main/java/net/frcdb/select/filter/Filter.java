package net.frcdb.select.filter;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * A wrapper class for delay-invoked filtering functions.
 * @author tim
 */
public abstract class Filter extends BaseFunction {

	@Override
	public Object call(
			Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return apply(scope, args);
	}
	
	/**
	 * Returns the actual filtering function
	 * @param scope the current JS scope
	 * @param outerArgs the args for the outer (immediately-executed) function
	 * @return a boolean function to filter objects based on a single param
	 */
	public abstract Function apply(Scriptable scope, Object[] outerArgs);
	
}
