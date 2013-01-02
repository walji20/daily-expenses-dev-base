package com.daily.expenses.util;

import java.util.Map;

import com.daily.expenses.database.DailyTables;

/**
 * Filter Records by passed selection parameters.
 *
 */
public final class RecordFilter 
{
    private SelectionBuilder mSelectionBuilder = null;

    public RecordFilter() {
    	// Initialize SelectionBuilder 
    	mSelectionBuilder = new SelectionBuilder();
    	this.reset();
	}

	public void set( Map<String, String> m) {
		reset();
		mSelectionBuilder.table(DailyTables.TABLE_RECORDS);
		
		for( Map.Entry<String, String> entry : m.entrySet() )
		{
		  String key = entry.getKey();
		  String value = entry.getValue();
		  
		  mSelectionBuilder.where( key, "" + value );
		  
		}
    }
	
	/**
	 * Reset the filter to show  with default filter
	 */
	public void reset() {
		mSelectionBuilder.reset();
		mSelectionBuilder.table(DailyTables.TABLE_RECORDS)
			.where(DailyTables.TABLE_RECORDS_COLUMN_TITLE + "!=?",  "IS NULL");
	}
    
    public String getSelection() {
		return mSelectionBuilder.getSelection();
    	
    }
    
    public String[] getSelectionArgs() {
		return mSelectionBuilder.getSelectionArgs();
    }
}