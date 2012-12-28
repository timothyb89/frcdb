package net.frcdb.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author tim
 */
public class DateUtil {

	public static final String[] MONTHS = {
		"January",
		"February",
		"March",
		"April",
		"May",
		"June",
		"July",
		"August",
		"September",
		"November",
		"December"
	};

	public static final String EVENT_REGEX =
			"(\\d\\d?)\\-(\\w+) \\- (\\d\\d?)\\-(\\w+)\\-(\\d\\d\\d\\d)";

	/**
	 * Parses a date based on the date string appearing in the FIRST databases.
	 * @param dateStr The date string to parse
	 * @return A parsed Date object
	 */
	public static Date[] parseEventDateRange(String dateStr) {
		List<String> groups = ListUtil.extract(EVENT_REGEX, dateStr);

		if (groups == null) {
			return null;
		}

		int startDay = Integer.parseInt(groups.get(1));
		int startMonth = getMonth(groups.get(2));

		int endDay = Integer.parseInt(groups.get(3));
		int endMonth = getMonth(groups.get(4));

		int year = Integer.parseInt(groups.get(5));

		Calendar start = new GregorianCalendar(year, startMonth, startDay);
		Calendar end = new GregorianCalendar(year, endMonth, endDay);

		return new Date[] {start.getTime(), end.getTime()};
	}

	/**
	 * Gets the month from the abbreviation given (like Mar, Apr, Jul, etc)
	 * @param str A string containing an abbreviated date
	 * @return a month (0-11) or -1 if the string is unknown
	 */
	public static int getMonth(String str) {
		for (int i = 0; i < MONTHS.length; i++) {
			String month = MONTHS[i];

			if (month.toLowerCase().startsWith(str.toLowerCase())) {
				return i;
			}
		}

		System.out.println("[Warning] Unknown month '" + str + "'");

		return -1;
	}
	
	/**
	 * End dates should be at the end of the day
	 * @param date 
	 */
	public static Date fixEndDate(Date date) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(date);
		
		c.set(Calendar.HOUR_OF_DAY, 23);
		
		return c.getTime();
	}

	public static Date parse(String date) {
		String[] fields = date.split("-");
		
		int year = Integer.parseInt(fields[0]);
		int month = Integer.parseInt(fields[1]);
		int day = Integer.parseInt(fields[2]);
		
		return new GregorianCalendar(year, month - 1, day).getTime();
	}
	
	public static void main(String[] args) {
		String date = "19-Mar - 20-Mar-2010";

		System.out.println("Parsing: " + date);

		Date[] dates = parseEventDateRange(date);

		System.out.println("Start: " + dates[0]);
		System.out.println("End:   " + dates[1]);
	}

}
