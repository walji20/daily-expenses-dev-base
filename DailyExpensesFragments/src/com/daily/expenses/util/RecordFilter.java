package com.daily.expenses.util;

import java.util.Map;

import com.daily.expenses.database.DailyTables;

/**
 * Filter Records by passed selection parameters.
 *
 */
public final class RecordFilter 
{
    /**
     * Privates Klassenattribut,
     * wird beim erstmaligen Gebrauch (nicht beim Laden) der Klasse erzeugt
     */
    private static RecordFilter instance;
 
    private SelectionBuilder mSelectionBuilder = null;


	/** Konstruktor ist privat, Klasse darf nicht von außen instanziiert werden. */

    public RecordFilter() {
    	// Initialize SelectionBuilder 
    	mSelectionBuilder = new SelectionBuilder();
    	this.reset();
	}

	/**
     * Statische Methode „getInstance()“ liefert die einzige Instanz der Klasse zurück.
     * Ist synchronisiert und somit thread-sicher.
     */
   
    public synchronized static RecordFilter getInstance() 
    {
    	if (instance == null) 
    	{
    		instance = new RecordFilter();
    	}
    	return instance;
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