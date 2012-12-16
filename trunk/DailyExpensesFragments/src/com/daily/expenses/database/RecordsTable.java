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
	  public static final String TABLE_TODO = "record";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_CATEGORY = "category";
	  public static final String COLUMN_SUMMARY = "summary";
	  public static final String COLUMN_DESCRIPTION = "description";

	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "create table " 
	      + TABLE_TODO
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_CATEGORY + " text not null, " 
	      + COLUMN_SUMMARY + " text not null," 
	      + COLUMN_DESCRIPTION
	      + " text not null" 
	      + ");";
	  
	  // Database dummy items statement
	  private static final String DATABASE_DUMMY_DATA_CATEGORY = "insert into " 
	      + TABLE_TODO
	      + "(" 
	      + COLUMN_CATEGORY + ", " 
	      + COLUMN_SUMMARY + ", " 
	      + COLUMN_DESCRIPTION
	      + ")"
	      + " VALUES"
	      + "(" 
	      + "'TestCategory1', "
	      + "'TestSummary1', "
	      + "'TestDescription1'"
	      + ");";
	  
	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	    database.execSQL(DATABASE_DUMMY_DATA_CATEGORY);
	    Log.d(RecordsTable.class.getName(), "Created and filled database.");
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(RecordsTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
	    onCreate(database);
	  }
	} 