package net.frcdb.select;

import net.frcdb.select.filter.DatabaseFilterFunction;
import net.frcdb.select.filter.EqualsFilter;
import net.frcdb.select.filter.GreaterThanFilter;
import net.frcdb.select.filter.LessThanFilter;
import net.frcdb.select.filter.StartsWithFilter;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;


/**
 * Allows for the selection of objects using a small javascript API, based on
 * Rhino. 
 * @author tim
 */
public class SelectorFactory {
	
	private SelectorFactory() {
		
	}
	
	public static Selector createSelector(String text) {
		SandboxedContextFactory factory = new SandboxedContextFactory();
		Context cx = factory.makeContext();
		factory.enterContext(cx);
		
		Selector ret = null;
		try {
			ScriptableObject global = cx.initStandardObjects();
			global.defineProperty("select", new SelectFunction(), 0);
			global.defineProperty("db", new DatabaseFilterFunction(), 0);
			global.defineProperty("lt", new LessThanFilter(), 0);
			global.defineProperty("gt", new GreaterThanFilter(), 0);
			global.defineProperty("eq", new EqualsFilter(), 0);
			global.defineProperty("startsWith", new StartsWithFilter(), 0);
			global.defineProperty("now", DateFunctions.now, 0);
			global.defineProperty("offset", DateFunctions.offset, 0);
			global.defineProperty("date", DateFunctions.date, 0);
			
			ret = (Selector) cx.evaluateString(global, text, "Selector", 0, null);
			
			Context.exit();
			return ret;
		} catch (Exception ex) {
			Context.exit();
			throw new IllegalArgumentException("Error in selector", ex);
		}
	}
	
	public static class SandboxedClassShutter implements ClassShutter {

		@Override
		public boolean visibleToScripts(String fullClassName) {
			return fullClassName.startsWith("net.frcdb.api");
		}
		
	}
	
	public static class SandboxedNativeJavaObject extends NativeJavaObject {

		public SandboxedNativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
			super(scope, javaObject, staticType);
		}

		@Override
		public Object get(String name, Scriptable start) {
			if (name.equals("getClass")) {
				// deny access to getClass
				return NOT_FOUND;
			}
			
			return super.get(name, start);
		}
		
	}
	
	public static class SandboxedWrapFactory extends WrapFactory {

		@Override
		public Scriptable wrapAsJavaObject(
				Context cx, Scriptable scope,
				Object javaObject, Class<?> staticType) {
			return new SandboxedNativeJavaObject(scope, javaObject, staticType);
		}
		
	}
	
	public static class SandboxedContextFactory extends ContextFactory {

		@Override
		protected Context makeContext() {
			Context cx = super.makeContext();
			cx.setWrapFactory(new SandboxedWrapFactory());
			return cx;
		}
		
	}
	
}
