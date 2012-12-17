package com.daily.expenses;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.daily.expenses.contentprovider.RecordsContentProvider;
import com.daily.expenses.database.RecordsTable;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecordListActivity}
 * in two-pane mode (on tablets) or a {@link RecordDetailActivity}
 * on handsets.
 */
public class RecordDetailFragment extends SherlockFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "record_id";

    /**
     * The dummy content this fragment is presenting.
     */
    //private RecordsContent.Record mItem;

    private Spinner mCategory;
    private EditText mSummaryText;
    private EditText mDescriptionText;
    
    private Uri recordUri;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecordDetailFragment() {
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	     
		if (getArguments().containsKey(RecordsContentProvider.CONTENT_ITEM_TYPE)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			/*mItem = RecordsContent.ITEM_MAP.get(getArguments().getParcelable(
					RecordsContentProvider.CONTENT_ITEM_TYPE));*/
		}
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_detail, container, false);
        
        
    	mCategory = (Spinner) rootView.findViewById(R.id.category);
		mSummaryText = (EditText) rootView.findViewById(R.id.record_edit_summary);
		mDescriptionText = (EditText) rootView.findViewById(R.id.record_edit_description);
		Button mRecordEditButton = (Button) rootView.findViewById(R.id.record_edit_button);
		
		Bundle extras = this.getActivity().getIntent().getExtras();
		
		// Check from the saved Instance
		recordUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(RecordsContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			recordUri = (Uri) extras.getParcelable(RecordsContentProvider.CONTENT_ITEM_TYPE);
			fillData(recordUri);
		}  

        return rootView;
    }
    
private void fillData(Uri uri) {
	Log.d("todo", "todoUri: " + recordUri);
	String[] projection = { RecordsTable.COLUMN_ID,
							RecordsTable.COLUMN_CATEGORY,
							RecordsTable.COLUMN_SUMMARY,
							RecordsTable.COLUMN_DESCRIPTION,
							};
	
	Cursor cursor = getActivity().getContentResolver().query(recordUri, projection, null, null, null);
	
	if (cursor != null) {
		  cursor.moveToFirst();
		  
          String category = cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_CATEGORY));

          for (int i = 0; i < mCategory.getCount(); i++) {
              String s = (String) mCategory.getItemAtPosition(i);
              if (s.equalsIgnoreCase(category)) {
                      mCategory.setSelection(i);
              }
          }

          mSummaryText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_SUMMARY)));
          mDescriptionText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_DESCRIPTION)));

	}
  // Always close the cursor
  cursor.close();
}

}
