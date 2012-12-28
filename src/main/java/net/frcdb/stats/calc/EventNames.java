package net.frcdb.stats.calc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.frcdb.api.event.Event;
import net.frcdb.db.Database;

/**
 *
 * @author tim
 */
public class EventNames {

	public static void main(String[] args) {
		Database db = new Database();

		for (Event evt : db.getEvents()) {
			System.out.println("Event: " + evt.getName());
			System.out.print("Short name: ");

			String sname = readLine();
			evt.setShortName(sname);

			db.store(evt);
		}
	}

	private static String readLine() {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);

		try {
			return in.readLine();
		} catch (Exception ex) {
			return null;
		}
	}

}
