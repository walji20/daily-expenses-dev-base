package com.daily.expenses.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;

import com.daily.expenses.database.DailyTables;

/**
 * Contains helper functions for time and date related conversions 
 *
 */
public class Clockwork {

	/**
	 * Returns a readable string with date from unix timestamp
	 * Depends on locale of the device
	 * API9: c.getDisplayName(Calendar.MONTH, Calendar.LONG , ls);
	 * @param context context to get the locale
	 * @param unixTime unix timestamp in seconds
	 * @return the human readable string
	 */
	public static String convertToHumanReadable(Context context, long unixTime) {

		Calendar c = Calendar.getInstance();
		Locale ls = context.getResources().getConfiguration().locale;
	    
		long unixTimeMs = TimeUnit.SECONDS.toMillis(unixTime);
		c.setTimeInMillis(unixTimeMs);
		 
		SimpleDateFormat date = new SimpleDateFormat("EEE, d MMM, yyyy", ls);
		String humanReadableString = date.format(new Date(unixTimeMs));

		return humanReadableString; 
	}
}
