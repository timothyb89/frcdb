package net.frcdb.select;

import java.util.Date;
import java.util.GregorianCalendar;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author tim
 */
public class DateFunctions {
	
	public static QuickFunction now = new QuickFunction() {

		@Override
		public Object call(Scriptable scope, Object[] args) {
			return new Date();
		}
		
	};
	
	public static QuickFunction date = new QuickFunction() {

		@Override
		public Object call(Scriptable scope, Object[] args) {
			int year = (Integer) Context.jsToJava(args[0], Integer.class);
			int month = (Integer) Context.jsToJava(args[1], Integer.class);
			int day = (Integer) Context.jsToJava(args[2], Integer.class);
			
			return new GregorianCalendar(year, month, day).getTime();
		}
		
	};
	
	public static QuickFunction offset = new QuickFunction() {

		@Override
		public Object call(Scriptable scope, Object[] args) {
			if (args.length == 1) {
				int offset = (Integer) Context.jsToJava(args[0], Integer.class);
				
				GregorianCalendar cal = new GregorianCalendar();
				cal.add(GregorianCalendar.DAY_OF_YEAR, offset);
				
				return cal.getTime();
			} else if (args.length == 2) {
				Date date = (Date) args[0];
				int offset = (Integer) Context.jsToJava(args[1], Integer.class);
				
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date);
				cal.add(GregorianCalendar.DAY_OF_YEAR, offset);
				
				return cal.getTime();
			} else {
				throw new IllegalArgumentException("Invailid arguments");
			}
		}
		
	};
	
}
