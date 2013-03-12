package net.frcdb.select;

import java.lang.reflect.Member;
import java.util.Date;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author tim
 */
public class Filters {

	public static QuickFunction lt = new QuickFunction() {

		@Override
		public Object call(Scriptable scope, Object[] args) {
			if (args.length != 1) {
				throw new IllegalArgumentException("Need a comparison param");
			}
			
			final Comparable b = (Comparable)
					Context.jsToJava(args[0], Comparable.class);
			
			// oh god the nesting
			return new QuickFunction() {

				@Override
				public Object call(Scriptable scope, Object[] args) {
					if (args.length != 1) {
						throw new IllegalArgumentException();
					}
					
					Comparable a = (Comparable)
							Context.jsToJava(args[0], Comparable.class);
					
					return a.compareTo(b) < 0;
				}
				
			};
		}
		
	};
	
	public static QuickFunction gt = new QuickFunction() {

		@Override
		public Object call(Scriptable scope, Object[] args) {
			if (args.length != 1) {
				throw new IllegalArgumentException("Need a comparison param");
			}
			
			final Comparable b = (Comparable)
					Context.jsToJava(args[0], Comparable.class);
			
			// oh god the nesting
			return new QuickFunction() {

				@Override
				public Object call(Scriptable scope, Object[] args) {
					if (args.length != 1) {
						throw new IllegalArgumentException();
					}
					
					Comparable a = (Comparable)
							Context.jsToJava(args[0], Comparable.class);
					
					return a.compareTo(b) > 0;
				}
				
			};
		}
		
	};
	
	public static QuickFunction eq = new QuickFunction() {

		@Override
		public Object call(Scriptable scope, Object[] args) {
			if (args.length != 1) {
				throw new IllegalArgumentException("Need a comparison param");
			}
			
			final Comparable b = (Comparable)
					Context.jsToJava(args[0], Comparable.class);
			
			// oh god the nesting
			return new QuickFunction() {

				@Override
				public Object call(Scriptable scope, Object[] args) {
					if (args.length != 1) {
						throw new IllegalArgumentException();
					}
					
					Comparable a = (Comparable)
							Context.jsToJava(args[0], Comparable.class);
					
					return a.compareTo(b) == 0;
				}
				
			};
		}
		
	};
	
	public static QuickFunction now = new QuickFunction() {

		@Override
		public Object call(Scriptable scope, Object[] args) {
			return Context.javaToJS(new Date(), scope);
		}
		
	};
	
	public static abstract class QuickFunction extends BaseFunction {

		@Override
		public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
			return call(scope, args);
			
		}
		
		public abstract Object call(Scriptable scope, Object[] args);
		
	}
	
}
