package com.daily.expenses;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.daily.expenses.contentprovider.RecordsContentProvider;
import com.daily.expenses.database.RecordsTable;

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

		mRecordEditButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// TODO: other checks, maybe use validation library
				if (TextUtils.isEmpty(mTitleText.getText().toString())) {
					Toast.makeText(getActivity(), "Please maintain a title", Toast.LENGTH_LONG).show();
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
			Toast.makeText(getActivity(), "Something went wrong when passing the uri or this is a new record.", Toast.LENGTH_SHORT).show();
		}

		return rootView;
	}

	private void fillData(Uri uri) {
		Log.d("todo", "todoUri: " + recordUri);
		String[] projection = { RecordsTable.COLUMN_ID, RecordsTable.COLUMN_TITLE, RecordsTable.COLUMN_DESCRIPTION, RecordsTable.COLUMN_AMOUNT,
				RecordsTable.COLUMN_BOOKING_TYPE, RecordsTable.COLUMN_PERIOD_TYPE, RecordsTable.COLUMN_CATEGORY_TYPE, RecordsTable.COLUMN_UNIX_DATE,
				RecordsTable.COLUMN_PAY_STATE, };

		Cursor cursor = getActivity().getContentResolver().query(recordUri, projection, null, null, null);

		if (cursor != null) {
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

			String strDateTime = mUnixDate.getYear() + "-" + (mUnixDate.getMonth() + 1) + "-" + mUnixDate.getDayOfMonth();
			Log.d("strDateTime", strDateTime);
			mTitleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_TITLE)));
			mDescriptionText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_DESCRIPTION)));
			mAmountText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_AMOUNT)));
			mBookingTypeText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_BOOKING_TYPE)));
			mPeriodTypeText.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_PERIOD_TYPE)));

			if (cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COLUMN_PAY_STATE)) == 1) {
				mPayStateCheck.setChecked(true);
			} else {
				mPayStateCheck.setChecked(false);
			}

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

		// Long unixDate = mUnixDate.get

		boolean payState;
		if (mPayStateCheck.isChecked()) {
			payState = true;
		} else {
			payState = false;
		}

		ContentValues values = new ContentValues();
		// TODO: do it :D
		values.put(RecordsTable.COLUMN_TITLE, titleText);
		values.put(RecordsTable.COLUMN_DESCRIPTION, descriptionText);
		values.put(RecordsTable.COLUMN_AMOUNT, amountText);
		values.put(RecordsTable.COLUMN_BOOKING_TYPE, bookingTypeText);
		values.put(RecordsTable.COLUMN_PERIOD_TYPE, periodTypeText);
		values.put(RecordsTable.COLUMN_CATEGORY_TYPE, category);
		/* TODO: replace by user input */
		values.put(RecordsTable.COLUMN_UNIX_DATE, 1355765617);
		values.put(RecordsTable.COLUMN_PAY_STATE, payState);

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
}
