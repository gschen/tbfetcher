package edu.fudan.tbfetcher.utils;

import java.text.DateFormat;
import java.util.Date;

public class DateTimeUtil {

	public static String getCurrentDateTime() {
		String dateTime = null;
		Date now = new Date();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);

		dateTime = df.format(now);

		return dateTime;
	}
}
