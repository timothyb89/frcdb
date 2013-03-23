package net.frcdb.select.filter;

import net.frcdb.select.QuickFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author tim
 */
public class StartsWithFilter extends Filter {

	@Override
	public Function apply(Scriptable scope, final Object[] outerArgs) {
		return new QuickFunction() {

			@Override
			public Object call(Scriptable scope, Object[] args) {
				String b = (String) Context.jsToJava(outerArgs[0], String.class);
				String a = (String) Context.jsToJava(args[0], String.class);
				
				return a.startsWith(b);
			}
			
		};
	}
	
}
