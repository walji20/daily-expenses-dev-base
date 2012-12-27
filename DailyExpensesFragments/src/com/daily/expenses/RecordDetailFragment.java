package com.daily.expenses;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.dialogs.EditDateDialogFragment;
import com.daily.expenses.dialogs.SpinnerEditCategoryDialogFragment;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;


/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link RecordListActivity} in two-pane mode (on tablets) or a
 * {@link RecordDetailActivity} on handsets.
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
	// private RecordsContent.Record mItem;
	
	/* Validation */
	private Form mForm;
	/* GUI Elements */
	private Spinner mCategory;
	private EditText mTitleText;
	private EditText mDescriptionText;
	private EditText mAmountText;
	private CompoundButton mBookingTypeCheck;
	private EditText mPeriodTypeText;
	private CompoundButton mPayStateCheck;
	private Button mRecordEditButton;
	private SimpleCursorAdapter mCategoryAdapter = null;

	private Uri recordUri;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RecordDetailFragment() {
	}
	
	public Uri getRecordUri() {
		return recordUri;
	}
	
	private void setRecordUri(Uri recordUri) {
		this.recordUri = recordUri;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Important, otherwise the definitions for the fragment’s onCreateOptionsMenu() and onOptionsItemSelected() methods, and optionally onPrepareOptionsMenu(), onOptionsMenuClosed(), and onDestroyOptionsMenu() methods are not called */
		setHasOptionsMenu(true);
		if (getArguments() != null && getArguments().containsKey(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			/*
			 * mItem = RecordsContent.ITEM_MAP.get(getArguments().getParcelable(
			 * DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE));
			 */
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_record_detail, container, false);
		
		mCategory = (Spinner) rootView.findViewById(R.id.detail_categoryType);
		String[] mCategoryProjection = { DailyTables.TABLE_CATEGORIES_COLUMN_ID, DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
		int[] to = new int[] {  android.R.id.text1, android.R.id.text1 };
		Cursor mCategoryCursor = getActivity().getContentResolver().query(DailyContentProvider.CATEGORIES_CONTENT_URI, mCategoryProjection, null, null, null);
		mCategoryAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, mCategoryCursor, mCategoryProjection, to, 0 );
		mCategoryAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		mCategory.setAdapter(mCategoryAdapter);
		
		mCategory.setOnLongClickListener(new AdapterView.OnLongClickListener() { 
	        public boolean onLongClick(View v) { 
		        Cursor currentItem = (Cursor) mCategory.getSelectedItem();
		        if( currentItem != null ) {
		        	 Log.d("", currentItem.getString(currentItem.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE)) + " is long clicked");
		        	 DialogFragment newFragment = SpinnerEditCategoryDialogFragment.newInstance( currentItem.getInt(currentItem.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_ID)) );
		             newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
		        }
		        return true; 
		        //throw new RuntimeException("You long clicked an item!");
	        } 
	     }); 
		
		mCategory = (Spinner) rootView.findViewById(R.id.detail_categoryType);
		mTitleText = (EditText) rootView.findViewById(R.id.detail_title);
		mDescriptionText = (EditText) rootView.findViewById(R.id.detail_description);
		mAmountText = (EditText) rootView.findViewById(R.id.detail_amount);
		mBookingTypeCheck = (CompoundButton) rootView.findViewById(R.id.detail_bookingType);
		mPeriodTypeText = (EditText) rootView.findViewById(R.id.detail_periodType);
		mPayStateCheck = (CompoundButton) rootView.findViewById(R.id.detail_payState);
		mRecordEditButton = (Button) rootView.findViewById(R.id.record_edit_button);
		
		/* Add validation */
		mForm = new Form();
	    Validate validateTitle = new Validate(mTitleText);
	    Validate validateAmount = new Validate(mAmountText);
	    Validate validatePeriodType = new Validate(mPeriodTypeText);
	    
	    validateTitle.addValidator(new NotEmptyValidator(getActivity()));
	    validateAmount.addValidator(new NotEmptyValidator(getActivity()));
	    validatePeriodType.addValidator(new NotEmptyValidator(getActivity()));
	    
	    mForm.addValidates(validateTitle);
	    mForm.addValidates(validateAmount);
	    mForm.addValidates(validatePeriodType);
	    
		mRecordEditButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!mForm.validate()) {
					/* Form is not valid - but FormValidation should do the rest for us*/
				} else {
					// TODO: back to list
					saveState();
					getActivity().setResult(Activity.RESULT_OK);
					Toast.makeText(getActivity(), "Record saved", Toast.LENGTH_LONG).show();
				}
			}
		});

		// Bundle extras = this.getActivity().getIntent().getExtras();
		Bundle extras = this.getArguments();

		// Check from the saved Instance
		setRecordUri((savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE));

		// Or passed from the other activity
		if (extras != null) {
			setRecordUri((Uri) extras.getParcelable(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE));
			fillData(getRecordUri());
		} else {
			Toast.makeText(getActivity(), "New record.", Toast.LENGTH_SHORT).show();
		}

		return rootView;
	}

	private void fillData(Uri uri) {
		Log.d("todo", "todoUri: " + getRecordUri());
		String[] projection = { DailyTables.TABLE_RECORDS_COLUMN_ID, DailyTables.TABLE_RECORDS_COLUMN_TITLE, DailyTables.TABLE_RECORDS_COLUMN_DESCRIPTION, DailyTables.TABLE_RECORDS_COLUMN_AMOUNT,
				DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE, DailyTables.TABLE_RECORDS_COLUMN_PERIOD_TYPE, DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE, DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE,
				DailyTables.TABLE_RECORDS_COLUMN_PAY_STATE, };

		Cursor mRecordDetailCursor = getActivity().getContentResolver().query(getRecordUri(), projection, null, null, null);

		if (mRecordDetailCursor.moveToFirst()) {
			try {
				// get categoryType of record
				String categoryIdOfRecordDetail = mRecordDetailCursor.getString(mRecordDetailCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE));
				// get the cursor of the referenced category
				String[] categoryProjection = {  DailyTables.TABLE_CATEGORIES_COLUMN_ID,  DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
				Cursor categoryCursorOfRecordDetail = getActivity().getContentResolver().query(Uri.parse(DailyContentProvider.CATEGORIES_CONTENT_URI + "/" + categoryIdOfRecordDetail), categoryProjection, null, null, null);
				// get the title of the category
				String categoryTitleOfRecordDetail = null;
				if(categoryCursorOfRecordDetail.moveToFirst()) {
					categoryTitleOfRecordDetail = categoryCursorOfRecordDetail.getString(categoryCursorOfRecordDetail.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE));
				
					
//					for (boolean hasItem = categoryCursor.moveToFirst(); hasItem; hasItem = categoryCursor.moveToNext()) {
//					}
					 // iterate through the filled spinner and compare with the category of the record
					 for (int i = 0; i < mCategory.getCount(); i++) { 
						 Cursor currentCategoryCursor = (Cursor) mCategory.getItemAtPosition(i);
						 if(currentCategoryCursor != null) {
							 String currentCategoryTitle = (String) currentCategoryCursor.getString(currentCategoryCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE)); 
							 if(currentCategoryTitle.equalsIgnoreCase(categoryTitleOfRecordDetail)) { 
								 // the categories match - select the category
								 mCategory.setSelection(i); 
								 break;
							 } 
						 }
					 }
					 categoryCursorOfRecordDetail.close();	 
				}
				
				
				mTitleText.setText(mRecordDetailCursor.getString(mRecordDetailCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_TITLE)));
				mDescriptionText.setText(mRecordDetailCursor.getString(mRecordDetailCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_DESCRIPTION)));
				mAmountText.setText(mRecordDetailCursor.getString(mRecordDetailCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_AMOUNT)));
				mPeriodTypeText.setText(mRecordDetailCursor.getString(mRecordDetailCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_PERIOD_TYPE)));
				
				if (mRecordDetailCursor.getInt(mRecordDetailCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE)) == 1) {
					mBookingTypeCheck.setChecked(true);
				} else {
					mBookingTypeCheck.setChecked(false);
				}
				
				if (mRecordDetailCursor.getInt(mRecordDetailCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_PAY_STATE)) == 1) {
					mPayStateCheck.setChecked(true);
				} else {
					mPayStateCheck.setChecked(false);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Always close the cursor
		mRecordDetailCursor.close();
	}

	private void saveState() {

		long category = mCategory.getSelectedItemId();
		String titleText = mTitleText.getText().toString();
		String descriptionText = mDescriptionText.getText().toString();
		String amountText = mAmountText.getText().toString();
		String periodTypeText = mPeriodTypeText.getText().toString();
		
		boolean bookingType;
		if (mBookingTypeCheck.isChecked()) {
			bookingType = true;
		} else {
			bookingType = false;
		}
		
		boolean payState;
		if (mPayStateCheck.isChecked()) {
			payState = true;
		} else {
			payState = false;
		}

		ContentValues values = new ContentValues();
		values.put(DailyTables.TABLE_RECORDS_COLUMN_TITLE, titleText);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_DESCRIPTION, descriptionText);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_AMOUNT, amountText);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE, bookingType);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_PERIOD_TYPE, periodTypeText);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE, category);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_PAY_STATE, payState);

		if (getRecordUri() == null) {
			// New record
			setRecordUri(getActivity().getContentResolver().insert(DailyContentProvider.RECORDS_CONTENT_URI, values));
		} else {
			// Update record
			getActivity().getContentResolver().update(getRecordUri(), values, null, null);
		}
	}

	public void requestSave() {
		mRecordEditButton.performClick();
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_record_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Handle fragment menu item
        case R.id.menu_record_detail_save:
        	Log.d("save", "called");
        	requestSave();
            return true;
        case R.id.menu_record_detail_editDate: {
        	Log.d("editDate", "called");
        	
	    	 //TODO: transmit the column id to fragment and change date.
        	 int recordId =  Integer.parseInt(getRecordUri().getLastPathSegment());
	    	 DialogFragment editDateFragment = EditDateDialogFragment.newInstance( recordId );
	         editDateFragment.show(getActivity().getSupportFragmentManager(), "dialog");
	    	   
        	
        	return true;
        }
        default:
            // Not one of ours. Perform default menu processing
            return super.onOptionsItemSelected(item);
        }
    }

    public void refreshData() {
    	if( mCategoryAdapter != null) {
        	String[] categoryProjection = DailyTables.TABLE_CATEGORIES_AVAILABLE_COLUMS;
    		Cursor cursor = getActivity().getContentResolver().query(DailyContentProvider.CATEGORIES_CONTENT_URI, categoryProjection, null, null, null);
    		mCategoryAdapter.changeCursor(cursor);
    		fillData(getRecordUri());
    	}
    }
}
