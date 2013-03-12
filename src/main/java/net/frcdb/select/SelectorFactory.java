package net.frcdb.select;

import java.util.Date;
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
			global.defineProperty("select", new SelectFunction(), ScriptableObject.DONTENUM);
			global.defineProperty("lt", Filters.lt, ScriptableObject.DONTENUM);
			global.defineProperty("now", Filters.now, ScriptableObject.DONTENUM);
			
			ret = (Selector) cx.evaluateString(global, text, "Selector", 0, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Context.exit();
			return ret;
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
	
	public static void main(String[] args) {
		Selector s = SelectorFactory.createSelector("select('DateTest', { date: lt(now()) });");
		System.out.println("pass? " + s.passes(new Date(113, 2, 10)));
	}
	
}
