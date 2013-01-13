/**
 * 
 */
package com.daily.expenses.database;

/**
 * @author No3x
 *
 */
import static com.daily.expenses.util.LogUtils.LOGD;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daily.expenses.util.LogUtils;

public class DailyDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = LogUtils.makeLogTag(DailyDatabaseHelper.class);
	private static final String DATABASE_NAME = "daily.db";
	private static final int DATABASE_VERSION = 2;

	public DailyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		DailyTables.onCreate(database);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LOGD(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");
		
		//TODO: implement upgrade / downgrade functionality  
        /* TEMPLATE: 
		if (newVersion > oldVersion) {
            // Upgrade
            switch(oldVersion) {
                    case 2:
                            // Upgrade from version 2 to 3.
                            // It seems SQLite3 only allows to add one column at a time,
                            // so we need three SQL statements:
                            try {
                                    database.execSQL("ALTER TABLE " + NOTES_TABLE_NAME + " ADD COLUMN "
                                                    + Notes.TAGS + " TEXT;");
                                    database.execSQL("ALTER TABLE " + NOTES_TABLE_NAME + " ADD COLUMN "
                                                    + Notes.ENCRYPTED + " INTEGER;");
                                    database.execSQL("ALTER TABLE " + NOTES_TABLE_NAME + " ADD COLUMN "
                                                    + Notes.THEME + " TEXT;");
                            } catch (SQLException e) {
                                    Log.e(TAG, "Error executing SQL: ", e);
                                    // If the error is "duplicate column name" then everything is fine,
                                    // as this happens after upgrading 2->3, then downgrading 3->2,
                                    // and then upgrading again 2->3.
                            }
                            // fall through for further upgrades.
                           
                    case 3:
                            // Upgrade from version 3 to 4
                            try {
                                    database.execSQL("ALTER TABLE " + NOTES_TABLE_NAME + " ADD COLUMN "
                                                    + Notes.SELECTION_START + " INTEGER;");
                                    database.execSQL("ALTER TABLE " + NOTES_TABLE_NAME + " ADD COLUMN "
                                                    + Notes.SELECTION_END + " INTEGER;");
                                    database.execSQL("ALTER TABLE " + NOTES_TABLE_NAME + " ADD COLUMN "
                                                    + Notes.SCROLL_POSITION + " REAL;");
                            } catch (SQLException e) {
                                    Log.e(TAG, "Error executing SQL: ", e);
                            }
                           
                    case 4:
                            // add more columns here
                            break;
                           
                    default:
                            Log.w(TAG, "Unknown version " + oldVersion + ". Creating new database.");
                            database.execSQL("DROP TABLE IF EXISTS notes");
                            onCreate(database);
            }
    } else { // newVersion <= oldVersion
            // Downgrade
            Log.w(TAG, "Don't know how to downgrade. Will not touch database and hope they are compatible.");
            // Do nothing.
    }
    */
        
		DailyTables.onUpgrade(database, oldVersion, newVersion);
	}
}
