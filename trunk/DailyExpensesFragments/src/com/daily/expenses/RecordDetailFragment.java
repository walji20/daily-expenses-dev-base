package com.daily.expenses;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.daily.expenses.contentprovider.RecordsContentProvider;
import com.daily.expenses.database.RecordsTable;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validate.OrTwoRequiredValidate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
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
		/* Important, otherwise the definitions for the fragment’s onCreateOptionsMenu() and onOptionsItemSelected() methods, and optionally onPrepareOptionsMenu(), onOptionsMenuClosed(), and onDestroyOptionsMenu() methods are not called */
		setHasOptionsMenu(true);
		if (getArguments() != null && getArguments().containsKey(RecordsContentProvider.CONTENT_ITEM_TYPE)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			/*
			 * mItem = RecordsContent.ITEM_MAP.get(getArguments().getParcelable(
			 * RecordsContentProvider.CONTENT_ITEM_TYPE));
			 */
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_record_detail, container, false);
		
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
	    Validate validateDescription = new Validate(mDescriptionText);
	    Validate validateAmount = new Validate(mAmountText);
	    Validate validateBookingType = new Validate(mBookingTypeText);
	    Validate validatePeriodType = new Validate(mPeriodTypeText);
	    
	    validateTitle.addValidator(new NotEmptyValidator(getActivity()));
	    validateDescription.addValidator(new NotEmptyValidator(getActivity()));
	    validateAmount.addValidator(new NotEmptyValidator(getActivity()));
	    validateBookingType.addValidator(new NotEmptyValidator(getActivity()));
	    validatePeriodType.addValidator(new NotEmptyValidator(getActivity()));
	    
	    mForm.addValidates(validateTitle);
	    mForm.addValidates(validateDescription);
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
		recordUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(RecordsContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			recordUri = (Uri) extras.getParcelable(RecordsContentProvider.CONTENT_ITEM_TYPE);
			fillData(recordUri);
		} else {
			Toast.makeText(getActivity(), "New record.", Toast.LENGTH_SHORT).show();
		}

		return rootView;
	}

	private void fillData(Uri uri) {
		Log.d("todo", "todoUri: " + recordUri);
		String[] projection = { RecordsTable.TABLE_RECORDS_COLUMN_ID, RecordsTable.TABLE_RECORDS_COLUMN_TITLE, RecordsTable.TABLE_RECORDS_COLUMN_DESCRIPTION, RecordsTable.TABLE_RECORDS_COLUMN_AMOUNT,
				RecordsTable.TABLE_RECORDS_COLUMN_BOOKING_TYPE, RecordsTable.TABLE_RECORDS_COLUMN_PERIOD_TYPE, RecordsTable.TABLE_RECORDS_COLUMN_CATEGORY_TYPE, RecordsTable.TABLE_RECORDS_COLUMN_UNIX_DATE,
				RecordsTable.TABLE_RECORDS_COLUMN_PAY_STATE, };

		Cursor cursor = getActivity().getContentResolver().query(recordUri, projection, null, null, null);

		if (cursor != null)
			try {
				{
					cursor.moveToFirst();

					/*
					 * String category =
					 * cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable
					 * .COLUMN_CATEGORY));
					 * 
					 * for (int i = 0; i < mCategory.getCount(); i++) { String s =
					 * (String) mCategory.getItemAtPosition(i); if
					 * (s.equalsIgnoreCase(category)) { mCategory.setSelection(i); } }
					 */

					/* Convert unix time stamp to mills */
					long unixTs = cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.TABLE_RECORDS_COLUMN_UNIX_DATE));
					Calendar c = Calendar.getInstance();
					long unixTms = TimeUnit.SECONDS.toMillis( unixTs );
					c.setTimeInMillis( unixTms );
					
					Log.d("Timestamp from DatePicker: ", "" + unixTs );
					Log.d("converted Time symbols from DatePicker: ", "" + c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH));
					mUnixDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
					
					mTitleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.TABLE_RECORDS_COLUMN_TITLE)));
					mDescriptionText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.TABLE_RECORDS_COLUMN_DESCRIPTION)));
					mAmountText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.TABLE_RECORDS_COLUMN_AMOUNT)));
					mBookingTypeText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.TABLE_RECORDS_COLUMN_BOOKING_TYPE)));
					mPeriodTypeText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.TABLE_RECORDS_COLUMN_PERIOD_TYPE)));

					if (cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.TABLE_RECORDS_COLUMN_PAY_STATE)) == 1) {
						mPayStateCheck.setChecked(true);
					} else {
						mPayStateCheck.setChecked(false);
					}

				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		// Always close the cursor
		cursor.close();
	}

	private void saveState() {

		String category = mCategory.getSelectedItem().toString();
		String titleText = mTitleText.getText().toString();
		String descriptionText = mDescriptionText.getText().toString();
		String amountText = mAmountText.getText().toString();
		String bookingTypeText = mBookingTypeText.getText().toString();
		String periodTypeText = mPeriodTypeText.getText().toString();
		int unixDateYear = mUnixDate.getYear();
		int unixDateMonth = mUnixDate.getMonth();
		int unixDateDay = mUnixDate.getDayOfMonth();
		
		
		// Long unixDate = mUnixDate.get
		

		
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
		// TODO: do it :D
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_TITLE, titleText);
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_DESCRIPTION, descriptionText);
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_AMOUNT, amountText);
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_BOOKING_TYPE, bookingTypeText);
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_PERIOD_TYPE, periodTypeText);
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_CATEGORY_TYPE, category);
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_UNIX_DATE, unixTs);
		values.put(RecordsTable.TABLE_RECORDS_COLUMN_PAY_STATE, payState);

		if (recordUri == null) {
			// New record
			recordUri = getActivity().getContentResolver().insert(RecordsContentProvider.CONTENT_URI, values);
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
