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
	public static final String TABLE_RECORDS = "records";
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
    public static final String TABLE_RECORDS_COLUMN_CATEGORY_TYPE_DEFAULT = "0";
   
    
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_CATEGORIES_COLUMN_ID = "_id";
    public static final String TABLE_CATEGORIES_COLUMN_ID_PARENT = "parentId";
    public static final String TABLE_CATEGORIES_COLUMN_TITLE = "title";
    public static final String TABLE_CATEGORIES_COLUMN_DESCRIPTION = "description";
    public static final String TABLE_CATEGORIES_COLUMN_RESOURCE_ICON = "resourceIcon";
    public static final String TABLE_CATEGORIES_COLUMN_LOCK = "lock";
    public static final String[] TABLE_CATEGORIES_AVAILABLE_COLUMS = { 
		TABLE_CATEGORIES_COLUMN_ID, 
		TABLE_CATEGORIES_COLUMN_ID_PARENT, 
		TABLE_CATEGORIES_COLUMN_TITLE, 
		TABLE_CATEGORIES_COLUMN_DESCRIPTION, 
		TABLE_CATEGORIES_COLUMN_RESOURCE_ICON 
	 };  

	  // Database creation SQL statement
	  private static final String TABLE_RECORDS_CREATE = "CREATE TABLE " 
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
	  
	  private static final String TABLE_CATEGORIES_CREATE = "CREATE TABLE " 
			  + TABLE_CATEGORIES
			  + "(" 
			  + TABLE_CATEGORIES_COLUMN_ID + " integer primary key autoincrement, " 
			  + TABLE_CATEGORIES_COLUMN_ID_PARENT + " int default '0' not null, " 
			  + TABLE_CATEGORIES_COLUMN_TITLE + " text not null, " 
			  + TABLE_CATEGORIES_COLUMN_DESCRIPTION + " text default '' not null, " 
			  + TABLE_CATEGORIES_COLUMN_RESOURCE_ICON + " int default '1' not null, " 
			  + TABLE_CATEGORIES_COLUMN_LOCK + " int default '0' not null " 
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
	      + "'Test Expense', "
	      + "'Test Description 1', "
	      + "'30', "
	      + "'1', "
	      + "'1', "
	      + "'1', "
	      + "'1355765617', "
	      + "'1'"
	      + ");";
	  private static final String TABLE_RECORDS_DUMMY_DATA2 = "insert into " 
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
			  + "'Test Income', "
			  + "'Test Description 1', "
			  + "'100', "
			  + "'0', "
			  + "'1', "
			  + "'1', "
			  + "'1355765617', "
			  + "'1'"
			  + ");";
	  
	  private static final String TABLE_CATEGORIES_DUMMY_DATA = "insert into " 
			  + TABLE_CATEGORIES
			  + "(" 
			  + TABLE_CATEGORIES_COLUMN_ID_PARENT + ", " 
			  + TABLE_CATEGORIES_COLUMN_TITLE + ", " 
			  + TABLE_CATEGORIES_COLUMN_DESCRIPTION + ", " 
			  + TABLE_CATEGORIES_COLUMN_RESOURCE_ICON + ", "
			  + TABLE_CATEGORIES_COLUMN_LOCK
			  + ")"
			  + " VALUES"
			  + "(" 
			  + "'0', "
			  + "'Default', "
			  + "'Holds uncategorized records.', "
			  + "'114', "
			  + "'1'"
			  + ");";
	  private static final String TABLE_CATEGORIES_DUMMY_DATA2 = "insert into " 
			  + TABLE_CATEGORIES
			  + "(" 
			  + TABLE_CATEGORIES_COLUMN_ID_PARENT + ", " 
			  + TABLE_CATEGORIES_COLUMN_TITLE + ", " 
			  + TABLE_CATEGORIES_COLUMN_DESCRIPTION + ", " 
			  + TABLE_CATEGORIES_COLUMN_RESOURCE_ICON
			  + ")"
			  + " VALUES"
			  + "(" 
			  + "'0', "
			  + "'Category 2', "
			  + "'Test Description for Category 2', "
			  + "'116'"
			  + ");";
	  
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_RECORDS_CREATE);
		database.execSQL(TABLE_CATEGORIES_CREATE);
		database.execSQL(TABLE_RECORDS_DUMMY_DATA);
		database.execSQL(TABLE_RECORDS_DUMMY_DATA2);

		database.execSQL(TABLE_CATEGORIES_DUMMY_DATA);
		database.execSQL(TABLE_CATEGORIES_DUMMY_DATA2);
		Log.d(DailyTables.class.getName(), "Created and filled database.");
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DailyTables.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
		onCreate(database);
	}
} 