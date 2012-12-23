/**
 * 
 */
package com.daily.expenses.contentprovider;

/**
 * @author No3x
 *
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.daily.expenses.database.DailyDatabaseHelper;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.util.LogUtils;
import com.daily.expenses.util.SelectionBuilder;

import static com.daily.expenses.util.LogUtils.LOGV;
import static com.daily.expenses.util.LogUtils.LOGD;
import static com.daily.expenses.util.LogUtils.makeLogTag;

public class DailyContentProvider extends ContentProvider {
	private static final String TAG = LogUtils.makeLogTag(DailyContentProvider.class);

	// database
	private DailyDatabaseHelper database;

	// Used for the UriMacher
	private static final int RECORDS = 10;
	private static final int RECORD_ID = 20;
	private static final int CATEGORIES = 30;
	private static final int CATEGORY_ID = 40;

	private static final String AUTHORITY = "com.daily.expenses.contentprovider";

	private static final String RECORDS_PATH = "records";
	private static final String CATEGORIES_PATH = "categories";

	public static final Uri RECORDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORDS_PATH);
	public static final Uri CATEGORIES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CATEGORIES_PATH);
	/* TODO: implement filter. At the moment no filter is implemented */
	public static final Uri RECORDS_CONTENT_FILTER_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORDS_PATH);
	public static final Uri CATEGORIES_CONTENT_FILTER_URI = Uri.parse("content://" + AUTHORITY + "/" + CATEGORIES_PATH);

	public static final String RECORDS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/records";
	public static final String RECORDS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/record";
	public static final String CATEGORIES_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/categories";
	public static final String CATEGORIES_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/categories";

	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sUriMatcher.addURI(AUTHORITY, RECORDS_PATH, RECORDS);
		sUriMatcher.addURI(AUTHORITY, RECORDS_PATH + "/#", RECORD_ID);
		sUriMatcher.addURI(AUTHORITY, CATEGORIES_PATH, CATEGORIES);
		sUriMatcher.addURI(AUTHORITY, CATEGORIES_PATH + "/#", CATEGORY_ID);
	}

	@Override
	public boolean onCreate() {
		database = new DailyDatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		LOGV(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");

		SQLiteDatabase db = database.getWritableDatabase();
		
		final int match = sUriMatcher.match(uri);
		switch (match) {
			default: {
	            // Most cases are handled with simple SelectionBuilder
	            final SelectionBuilder builder = buildExpandedSelection(uri, match);
	            Cursor cursor = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
	            cursor.setNotificationUri(getContext().getContentResolver(), uri);
	            return cursor;
	        }
		}
	}
	
	 /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS: {
                return builder.table(DailyTables.TABLE_RECORDS);
            }
            case RECORD_ID: {
                final String recordId =  uri.getLastPathSegment();
                return builder.table(DailyTables.TABLE_RECORDS)
                        .where(DailyTables.TABLE_RECORDS_COLUMN_ID + "=?", recordId);
            }
            case CATEGORIES: {
                return builder.table(DailyTables.TABLE_CATEGORIES);
            }
            case CATEGORY_ID: {
                final String categoryId = uri.getLastPathSegment();
                return builder.table(DailyTables.TABLE_CATEGORIES)
                        .where(DailyTables.TABLE_CATEGORIES_COLUMN_ID + "=?", categoryId);
            }
          
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }
	 /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case RECORDS: {
                return builder
                        .table(DailyTables.TABLE_RECORDS);
            }
            case RECORD_ID: {
            	final String recordId = uri.getLastPathSegment();
                return builder
                        .table(DailyTables.TABLE_RECORDS)
                        .where(DailyTables.TABLE_RECORDS_COLUMN_ID + "=?", recordId);
            }
            case CATEGORIES: {
            	return builder
            			.table(DailyTables.TABLE_CATEGORIES);
            }
            case CATEGORY_ID: {
            	final String categorydId = uri.getLastPathSegment();
            	return builder
            			.table(DailyTables.TABLE_CATEGORIES)
            			.where(DailyTables.TABLE_CATEGORIES_COLUMN_ID + "=?", categorydId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }
	@Override
	public String getType(Uri uri) {
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case RECORDS:
			return RECORDS_CONTENT_TYPE;
		case RECORD_ID:
			return RECORDS_CONTENT_TYPE;
		case CATEGORIES:
			return CATEGORIES_CONTENT_TYPE;
		case CATEGORY_ID:
			return CATEGORIES_CONTENT_TYPE;
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = database.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long id = 0;
        switch (match) {
            case RECORDS: {
                id = db.insertOrThrow(DailyTables.TABLE_RECORDS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                Uri u = Uri.parse(RECORDS_PATH + "/" + id);
                Log.d(TAG, "" + u);
                return u;
            }
            case CATEGORIES: {
                id = db.insertOrThrow(DailyTables.TABLE_CATEGORIES, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CATEGORIES_PATH + "/" + id);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete(uri=" + uri + ")");
		int rowsDeleted = 0;
        final SQLiteDatabase sqlDB = database.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        rowsDeleted = builder.where(selection, selectionArgs).delete(sqlDB);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		LOGV(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase sqlDB = database.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int rowsUpdated = builder.where(selection, selectionArgs).update(sqlDB, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = DailyTables.TABLE_RECORDS_AVAILABLE_COLUMS;
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

}