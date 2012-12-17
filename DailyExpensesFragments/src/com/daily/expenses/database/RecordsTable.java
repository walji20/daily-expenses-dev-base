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
public class RecordsTable {

	  // Database table
	  public static final String TABLE_RECORD = "record";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_TITLE = "title";
	  public static final String COLUMN_DESCRIPTION = "description";
	  public static final String COLUMN_AMOUNT = "amount";
	  public static final String COLUMN_BOOKING_TYPE = "bookingType";
	  public static final String COLUMN_PERIOD_TYPE = "periodType";
	  public static final String COLUMN_CATEGORY_TYPE = "categoryType";
	  public static final String COLUMN_UNIX_DATE = "unixDate";
	  public static final String COLUMN_PAY_STATE = "payState";
	  public static final String[] TABLE_RECORD_AVAILABLE_COLUMS = { 
		  COLUMN_ID, 
		  COLUMN_TITLE, 
		  COLUMN_DESCRIPTION, 
		  COLUMN_AMOUNT, 
		  COLUMN_BOOKING_TYPE, 
		  COLUMN_PERIOD_TYPE, 
		  COLUMN_CATEGORY_TYPE, 
		  COLUMN_UNIX_DATE, 
		  COLUMN_PAY_STATE
	 };
	  

	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "CREATE TABLE " 
	      + TABLE_RECORD
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_TITLE + " text not null, " 
	      + COLUMN_DESCRIPTION + " text not null, " 
	      + COLUMN_AMOUNT + " double not null, " 
	      + COLUMN_BOOKING_TYPE + " int not null, " 
	      + COLUMN_PERIOD_TYPE + " int not null, " 
	      + COLUMN_CATEGORY_TYPE + " int not null, " 
	      + COLUMN_UNIX_DATE + " long not null, " 
	      + COLUMN_PAY_STATE + " int not null" 
	      + ");";
	  
	  // Database dummy items statement
	  private static final String DATABASE_DUMMY_DATA_RECORDS = "insert into " 
	      + TABLE_RECORD
	      + "(" 
	      + COLUMN_TITLE + ", " 
	      + COLUMN_DESCRIPTION + ", " 
	      + COLUMN_AMOUNT + ", " 
	      + COLUMN_BOOKING_TYPE + ", " 
	      + COLUMN_PERIOD_TYPE + ", " 
	      + COLUMN_CATEGORY_TYPE + ", " 
	      + COLUMN_UNIX_DATE + ", " 
	      + COLUMN_PAY_STATE
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
	  
	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	    database.execSQL(DATABASE_DUMMY_DATA_RECORDS);
	    Log.d(RecordsTable.class.getName(), "Created and filled database.");
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(RecordsTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
	    onCreate(database);
	  }
	} 