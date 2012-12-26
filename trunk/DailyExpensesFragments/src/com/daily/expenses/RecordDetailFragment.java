package com.daily.expenses;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;
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
	private Spinner mCategoryReal;
	private Spinner mCategory;
	private DatePicker mUnixDate;
	private EditText mTitleText;
	private EditText mDescriptionText;
	private EditText mAmountText;
	private EditText mBookingTypeText;
	private EditText mPeriodTypeText;
	private CheckBox mPayStateCheck;
	private Button mRecordEditButton;

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
		/* Important, otherwise the definitions for the fragment�s onCreateOptionsMenu() and onOptionsItemSelected() methods, and optionally onPrepareOptionsMenu(), onOptionsMenuClosed(), and onDestroyOptionsMenu() methods are not called */
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
		
		mCategoryReal = (Spinner) rootView.findViewById(R.id.detail_categoryType_real);
		String[] mCategoryProjection = { DailyTables.TABLE_CATEGORIES_COLUMN_ID, DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
		int[] to = new int[] {  android.R.id.text1, android.R.id.text1 };
		Cursor mCategoryCursor = getActivity().getContentResolver().query(DailyContentProvider.CATEGORIES_CONTENT_URI, mCategoryProjection, null, null, null);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, mCategoryCursor, mCategoryProjection, to, 0 );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		mCategoryReal.setAdapter(adapter);
		
		mCategory = (Spinner) rootView.findViewById(R.id.detail_categoryType);
		mCategory = (Spinner) rootView.findViewById(R.id.detail_categoryType);
		mUnixDate = (DatePicker) rootView.findViewById(R.id.detail_datePicker01);
		mTitleText = (EditText) rootView.findViewById(R.id.detail_title);
		mDescriptionText = (EditText) rootView.findViewById(R.id.detail_description);
		mAmountText = (EditText) rootView.findViewById(R.id.detail_amount);
		mBookingTypeText = (EditText) rootView.findViewById(R.id.detail_bookingType);
		mPeriodTypeText = (EditText) rootView.findViewById(R.id.detail_periodType);
		mPayStateCheck = (CheckBox) rootView.findViewById(R.id.detail_payState);
		mRecordEditButton = (Button) rootView.findViewById(R.id.record_edit_button);
		
		/* Add validation */
		mForm = new Form();
	    Validate validateTitle = new Validate(mTitleText);
	    Validate validateAmount = new Validate(mAmountText);
	    Validate validateBookingType = new Validate(mBookingTypeText);
	    Validate validatePeriodType = new Validate(mPeriodTypeText);
	    
	    validateTitle.addValidator(new NotEmptyValidator(getActivity()));
	    validateAmount.addValidator(new NotEmptyValidator(getActivity()));
	    validateBookingType.addValidator(new NotEmptyValidator(getActivity()));
	    validatePeriodType.addValidator(new NotEmptyValidator(getActivity()));
	    
	    mForm.addValidates(validateTitle);
	    mForm.addValidates(validateAmount);
	    mForm.addValidates(validateBookingType);
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
		recordUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			recordUri = (Uri) extras.getParcelable(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE);
			fillData(recordUri);
		} else {
			Toast.makeText(getActivity(), "New record.", Toast.LENGTH_SHORT).show();
		}

		return rootView;
	}

	private void fillData(Uri uri) {
		Log.d("todo", "todoUri: " + recordUri);
		String[] projection = { DailyTables.TABLE_RECORDS_COLUMN_ID, DailyTables.TABLE_RECORDS_COLUMN_TITLE, DailyTables.TABLE_RECORDS_COLUMN_DESCRIPTION, DailyTables.TABLE_RECORDS_COLUMN_AMOUNT,
				DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE, DailyTables.TABLE_RECORDS_COLUMN_PERIOD_TYPE, DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE, DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE,
				DailyTables.TABLE_RECORDS_COLUMN_PAY_STATE, };

		Cursor mRecordCursor = getActivity().getContentResolver().query(recordUri, projection, null, null, null);

		if (mRecordCursor != null)
			try {
				mRecordCursor.moveToFirst();
				
				SimpleCursorAdapter mSCA = (SimpleCursorAdapter) mCategoryReal.getAdapter();
				Cursor mCategoryCursor = mSCA.getCursor();
				if(mCategoryCursor != null) {
					
					String recrodCategorySpecId = mRecordCursor.getString(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE));
					String[] specP = {  DailyTables.TABLE_CATEGORIES_COLUMN_ID,  DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
					Cursor mCategorySpecCursor = getActivity().getContentResolver().query(Uri.parse(DailyContentProvider.CATEGORIES_CONTENT_URI + "/" + recrodCategorySpecId), specP, null, null, null);
					String CategorySpecTitle = null;
					if(mCategorySpecCursor.moveToFirst()) {
						CategorySpecTitle = mCategorySpecCursor.getString(mCategorySpecCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE));
					}
					
					for (boolean hasItem = mCategoryCursor.moveToFirst(); hasItem; hasItem = mCategoryCursor.moveToNext()) {
					    
					}
					
					 for (int i = 0; i < mCategoryReal.getCount(); i++) { 
						 Cursor theCursor = (Cursor) mCategoryReal.getItemAtPosition(i);
						 String s = (String) theCursor.getString(theCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE)); 
						 if(s.equalsIgnoreCase(CategorySpecTitle)) { 
							 mCategoryReal.setSelection(i); 
							 break;
						 } 
					 }
					 
					Log.d("t", "succsess");
				}
				/*
				 * String category =
				 * cursor.getString(cursor.getColumnIndexOrThrow(DailyTables
				 * .COLUMN_CATEGORY));
				 * 
				 * for (int i = 0; i < mCategory.getCount(); i++) { String s =
				 * (String) mCategory.getItemAtPosition(i); if
				 * (s.equalsIgnoreCase(category)) { mCategory.setSelection(i); } }
				 */

				/* Convert unix time stamp to mills */
				long unixTs = mRecordCursor.getInt(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE));
				Calendar c = Calendar.getInstance();
				long unixTms = TimeUnit.SECONDS.toMillis( unixTs );
				c.setTimeInMillis( unixTms );
				
				Log.d("Timestamp from DatePicker: ", "" + unixTs );
				Log.d("converted Time symbols from DatePicker: ", "" + c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH));
				mUnixDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				
				mTitleText.setText(mRecordCursor.getString(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_TITLE)));
				mDescriptionText.setText(mRecordCursor.getString(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_DESCRIPTION)));
				mAmountText.setText(mRecordCursor.getString(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_AMOUNT)));
				mBookingTypeText.setText(mRecordCursor.getString(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE)));
				mPeriodTypeText.setText(mRecordCursor.getString(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_PERIOD_TYPE)));

				if (mRecordCursor.getInt(mRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_PAY_STATE)) == 1) {
					mPayStateCheck.setChecked(true);
				} else {
					mPayStateCheck.setChecked(false);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		// Always close the cursor
		mRecordCursor.close();
	}

	private void saveState() {

		long category = mCategoryReal.getSelectedItemId();
		String titleText = mTitleText.getText().toString();
		String descriptionText = mDescriptionText.getText().toString();
		String amountText = mAmountText.getText().toString();
		String bookingTypeText = mBookingTypeText.getText().toString();
		String periodTypeText = mPeriodTypeText.getText().toString();
		int unixDateYear = mUnixDate.getYear();
		int unixDateMonth = mUnixDate.getMonth();
		int unixDateDay = mUnixDate.getDayOfMonth();
		
		
		Calendar c = Calendar.getInstance();
		c.set(unixDateYear, unixDateMonth, unixDateDay);
		// get ms from calendar
		long unixTms = c.getTimeInMillis();
		// convert ms to s
		long unixTs = TimeUnit.MILLISECONDS.toSeconds(unixTms);
		Log.d("Timestamp from DatePicker: ", "" + unixTs);
		
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
		values.put(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE, bookingTypeText);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_PERIOD_TYPE, periodTypeText);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE, category);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE, unixTs);
		values.put(DailyTables.TABLE_RECORDS_COLUMN_PAY_STATE, payState);

		if (recordUri == null) {
			// New record
			recordUri = getActivity().getContentResolver().insert(DailyContentProvider.RECORDS_CONTENT_URI, values);
		} else {
			// Update record
			getActivity().getContentResolver().update(recordUri, values, null, null);
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
        case R.id.menu_record_detail_save:
            // Handle fragment menu item
        	Log.d("save", "called");
        	requestSave();
            return true;
        default:
            // Not one of ours. Perform default menu processing
            return super.onOptionsItemSelected(item);
        }
    }
}