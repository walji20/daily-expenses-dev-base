package com.daily.expenses.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;

import com.daily.expenses.RecordDetailActivity;
import com.daily.expenses.database.DailyDatabaseHelper;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.util.LogUtils;
/**
 * Contains helper functions for time and date related conversions 
 *
 */
public class Clockwork {
	private static final String TAG = LogUtils.makeLogTag(DailyDatabaseHelper.class);
	/**
	 * calculate in maximum range of day
	 */
	public static final int DAY = GregorianCalendar.DAY_OF_MONTH;
	/**
	 * calculate in maximum range of month
	 */
	public static final int MONTH = GregorianCalendar.MONTH;
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
	
	/**
	 * @param rangeType Identifier for maximum range. See {@link Clockwork#DAY}, {@link Clockwork#MONTH}
	 * @param unixTmsFrom from unixtimestamp in millis 
	 * @param unixTmsTo to unixtimestamp in millis 
	 * @return maximum range
	 */
	public static ValuePair getMaximumRange(int rangeType, long unixTmsFrom, long unixTmsTo) {
		long unixTsFrom;
		long unixTsTo;
		
		if(rangeType >= MONTH) {
			Log.d(TAG, "in: rangeType: create range to month");
    	}
		
		Log.d(TAG, "in: from: " + new Date(unixTmsFrom).toString());
		Log.d(TAG, "in: to: " + new Date(unixTmsTo).toString());
		
		// Set the maximum range
		Calendar gcFrom = GregorianCalendar.getInstance();
		gcFrom.setTimeInMillis(unixTmsTo);
		gcFrom.set(GregorianCalendar.SECOND, gcFrom.getActualMinimum(GregorianCalendar.SECOND));
		gcFrom.set(GregorianCalendar.MINUTE, gcFrom.getActualMinimum(GregorianCalendar.MINUTE));
		gcFrom.set(GregorianCalendar.HOUR_OF_DAY, gcFrom.getActualMinimum(GregorianCalendar.HOUR_OF_DAY));
		if(rangeType == MONTH) {
			gcFrom.set(GregorianCalendar.DAY_OF_MONTH, gcFrom.getActualMinimum(GregorianCalendar.DAY_OF_MONTH));
		}
		
		Calendar gcTo = GregorianCalendar.getInstance();
		gcTo.setTimeInMillis(unixTmsTo);
		gcTo.set(GregorianCalendar.SECOND, gcTo.getActualMaximum(GregorianCalendar.SECOND));
    	gcTo.set(GregorianCalendar.MINUTE, gcTo.getActualMaximum(GregorianCalendar.MINUTE));
    	gcTo.set(GregorianCalendar.HOUR_OF_DAY, gcTo.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
    	if(rangeType == MONTH) {
    		gcTo.set(GregorianCalendar.DAY_OF_MONTH, gcTo.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
    	}
    	// Get changed time
    	unixTmsFrom = gcFrom.getTimeInMillis();
    	unixTmsTo = gcTo.getTimeInMillis();
    	
    	// Convert back to s
    	unixTsFrom = TimeUnit.MILLISECONDS.toSeconds(unixTmsFrom);
    	unixTsTo = TimeUnit.MILLISECONDS.toSeconds(unixTmsTo);
    	
    	// check range
    	if (unixTsFrom < 0) {
    		throw new RuntimeException("Time for 'From' date conversion failed");
    	} 
    	if (unixTsTo < 0) {
    		throw new RuntimeException("Time for 'To' date conversion failed");
    	}
    	
    	Log.d(TAG, "out: from: " + new Date(unixTmsFrom).toString());
		Log.d(TAG, "out: to: " + new Date(unixTmsTo).toString());
    	
    	// return results 
		return new ValuePair(unixTsFrom, unixTsTo);
	}

	public static long getMillis(DatePicker DatePicker) {
		
		Calendar gc = GregorianCalendar.getInstance();
		gc.set(DatePicker.getYear(), DatePicker.getMonth(), DatePicker.getDayOfMonth());
		
		return gc.getTimeInMillis();
	}
}
