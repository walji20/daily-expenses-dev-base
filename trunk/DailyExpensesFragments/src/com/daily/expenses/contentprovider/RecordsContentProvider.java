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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.daily.expenses.database.RecordsDatabaseHelper;
import com.daily.expenses.database.RecordsTable;

public class RecordsContentProvider extends ContentProvider {

  // database
  private RecordsDatabaseHelper database;

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
  /*TODO: implement filter. At the moment no filter is implemented */
  public static final Uri RECORDS_CONTENT_FILTER_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORDS_PATH);
  public static final Uri CATEGORIES_CONTENT_FILTER_URI = Uri.parse("content://" + AUTHORITY + "/" + CATEGORIES_PATH);

  public static final String RECORDS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/records";
  public static final String RECORDS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/record";
  public static final String CATEGORIES_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/categories";
  public static final String CATEGORIES_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/categories";

  private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH) ;
  static {
    sUriMatcher.addURI(AUTHORITY, RECORDS_PATH, RECORDS);
    sUriMatcher.addURI(AUTHORITY, RECORDS_PATH + "/#", RECORD_ID);
    sUriMatcher.addURI(AUTHORITY, CATEGORIES_PATH, CATEGORIES);
    sUriMatcher.addURI(AUTHORITY, CATEGORIES_PATH + "/#", CATEGORY_ID);
  }

  @Override
  public boolean onCreate() {
    database = new RecordsDatabaseHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Check if the caller has requested a column which does not exists
    checkColumns(projection);

    // Set the table
    queryBuilder.setTables(RecordsTable.TABLE_RECORDS );
    
    int uriType = sUriMatcher.match(uri);
    switch (uriType) {
    case RECORDS:
      break;
    case RECORD_ID:
      // Adding the ID to the original query
      queryBuilder.appendWhere(RecordsTable.TABLE_RECORDS_COLUMN_ID + "="
          + uri.getLastPathSegment());
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
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
    int uriType = sUriMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case RECORDS:
      id = sqlDB.insert(RecordsTable.TABLE_RECORDS, null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(RECORDS_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sUriMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case RECORDS:
      rowsDeleted = sqlDB.delete(RecordsTable.TABLE_RECORDS, selection,
          selectionArgs);
      break;
    case RECORD_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(RecordsTable.TABLE_RECORDS,
        		RecordsTable.TABLE_RECORDS_COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsDeleted = sqlDB.delete(RecordsTable.TABLE_RECORDS,
        		RecordsTable.TABLE_RECORDS_COLUMN_ID + "=" + id 
            + " and " + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = sUriMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case RECORDS:
      rowsUpdated = sqlDB.update(RecordsTable.TABLE_RECORDS, 
          values, 
          selection,
          selectionArgs);
      break;
    case RECORD_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(RecordsTable.TABLE_RECORDS, 
            values,
            RecordsTable.TABLE_RECORDS_COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(RecordsTable.TABLE_RECORDS, 
            values,
            RecordsTable.TABLE_RECORDS_COLUMN_ID + "=" + id 
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  private void checkColumns(String[] projection) {
    String[] available = RecordsTable.TABLE_RECORDS_AVAILABLE_COLUMS;
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