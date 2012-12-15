package com.daily.expenses;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.ListActivity;
import android.database.Cursor;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ContactsActivity extends SherlockActivity {
	
	ListView listContacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		
		 listContacts = (ListView)findViewById(R.id.conactlist);
		  
		  Uri queryUri = ContactsContract.Contacts.CONTENT_URI;

		  String[] projection = new String[] {
		    ContactsContract.Contacts._ID,
		    ContactsContract.Contacts.DISPLAY_NAME,
		    ContactsContract.Contacts.HAS_PHONE_NUMBER,
		    ContactsContract.Contacts.LOOKUP_KEY};
		  
		  String selection = ContactsContract.Contacts.DISPLAY_NAME + " IS NOT NULL";
		  
		  CursorLoader cursorLoader = new CursorLoader(
		            this, 
		            queryUri, 
		            projection, 
		            selection, 
		            null, 
		            null);
		  
		  Cursor cursor = cursorLoader.loadInBackground();
		  
		  String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
		        int[] to = {android.R.id.text1};
		        
		        ListAdapter adapter = new SimpleCursorAdapter(
		                this, 
		                android.R.layout.simple_list_item_1, 
		                cursor, 
		                from, 
		                to, 
		                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		        listContacts.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getSupportMenuInflater().inflate(R.menu.activity_contacts, menu);
		return true;
	}

}
