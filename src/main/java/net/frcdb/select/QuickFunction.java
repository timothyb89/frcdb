package net.frcdb.select;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * A Function implementation with less arguments
 *
 * @author tim
 */
public abstract class QuickFunction extends BaseFunction {

	@Override
	public Object call(
			Context cx, Scriptable scope,
			Scriptable thisObj, Object[] args) {
		return call(scope, args);

	}

	public abstract Object call(Scriptable scope, Object[] args);
	
}
