package net.frcdb.select.filter;

import net.frcdb.select.QuickFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author tim
 */
public class GreaterThanFilter extends Filter {
	
	@Override
	public Function apply(final Scriptable scope, final Object[] outerArgs) {
		return new QuickFunction() {

			@Override
			public Object call(Scriptable scope, Object[] args) {
				if (outerArgs.length != 1 && args.length != 1) {
					throw new IllegalArgumentException(
							"Invalid method invocation");
				}
				
				Comparable b = (Comparable)
						Context.jsToJava(outerArgs[0], Comparable.class);
				Comparable a = (Comparable)
						Context.jsToJava(args[0], Comparable.class);
				
				return a.compareTo(b) > 0;
			}
			
		};
	}
	
}
