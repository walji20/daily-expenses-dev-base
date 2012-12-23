/**
 * 
 */
package com.daily.expenses.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * @author No3x
 *
 */
public class DailyTables {

	// Database table
	public static final String TABLE_RECORDS = "record";
	public static final String TABLE_RECORDS_COLUMN_ID = "_id";
	public static final String TABLE_RECORDS_COLUMN_TITLE = "title";
	public static final String TABLE_RECORDS_COLUMN_DESCRIPTION = "description";
	public static final String TABLE_RECORDS_COLUMN_AMOUNT = "amount";
	public static final String TABLE_RECORDS_COLUMN_BOOKING_TYPE = "bookingType";
	public static final String TABLE_RECORDS_COLUMN_PERIOD_TYPE = "periodType";
	public static final String TABLE_RECORDS_COLUMN_CATEGORY_TYPE = "categoryType";
	public static final String TABLE_RECORDS_COLUMN_UNIX_DATE = "unixDate";
	public static final String TABLE_RECORDS_COLUMN_PAY_STATE = "payState";
    public static final String[] TABLE_RECORDS_AVAILABLE_COLUMS = { 
		  TABLE_RECORDS_COLUMN_ID, 
		  TABLE_RECORDS_COLUMN_TITLE, 
		  TABLE_RECORDS_COLUMN_DESCRIPTION, 
		  TABLE_RECORDS_COLUMN_AMOUNT, 
		  TABLE_RECORDS_COLUMN_BOOKING_TYPE, 
		  TABLE_RECORDS_COLUMN_PERIOD_TYPE, 
		  TABLE_RECORDS_COLUMN_CATEGORY_TYPE, 
		  TABLE_RECORDS_COLUMN_UNIX_DATE, 
		  TABLE_RECORDS_COLUMN_PAY_STATE
	 };
	  

	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "CREATE TABLE " 
	      + TABLE_RECORDS
	      + "(" 
	      + TABLE_RECORDS_COLUMN_ID + " integer primary key autoincrement, " 
	      + TABLE_RECORDS_COLUMN_TITLE + " text not null, " 
	      + TABLE_RECORDS_COLUMN_DESCRIPTION + " text not null, " 
	      + TABLE_RECORDS_COLUMN_AMOUNT + " double not null, " 
	      + TABLE_RECORDS_COLUMN_BOOKING_TYPE + " int not null, " 
	      + TABLE_RECORDS_COLUMN_PERIOD_TYPE + " int not null, " 
	      + TABLE_RECORDS_COLUMN_CATEGORY_TYPE + " int not null, " 
	      + TABLE_RECORDS_COLUMN_UNIX_DATE + " long not null, " 
	      + TABLE_RECORDS_COLUMN_PAY_STATE + " int not null" 
	      + ");";
	  
	  // Database dummy items statement
	  private static final String TABLE_RECORDS_DUMMY_DATA = "insert into " 
	      + TABLE_RECORDS
	      + "(" 
	      + TABLE_RECORDS_COLUMN_TITLE + ", " 
	      + TABLE_RECORDS_COLUMN_DESCRIPTION + ", " 
	      + TABLE_RECORDS_COLUMN_AMOUNT + ", " 
	      + TABLE_RECORDS_COLUMN_BOOKING_TYPE + ", " 
	      + TABLE_RECORDS_COLUMN_PERIOD_TYPE + ", " 
	      + TABLE_RECORDS_COLUMN_CATEGORY_TYPE + ", " 
	      + TABLE_RECORDS_COLUMN_UNIX_DATE + ", " 
	      + TABLE_RECORDS_COLUMN_PAY_STATE
	      + ")"
	      + " VALUES"
	      + "(" 
	      + "'Test Title 1', "
	      + "'Test Description 1', "
	      + "'114', "
	      + "'1', "
	      + "'1', "
	      + "'1', "
	      + "'1355765617', "
	      + "'1'"
	      + ");";
	public static final String TABLE_CATEGORIES = "categories";
	public static final String TABLE_CATEGORIES_COLUMN_ID = "_id";
	  
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(TABLE_RECORDS_DUMMY_DATA);
		database.execSQL(TABLE_RECORDS_DUMMY_DATA);
		Log.d(DailyTables.class.getName(), "Created and filled database.");
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DailyTables.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
		onCreate(database);
	}
} 