package com.daily.expenses.dialogs;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.daily.expenses.R;
import com.daily.expenses.RecordDetailFragment;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;

public class EditDateDialogFragment extends DialogFragment {
	DatePicker mDatePicker;
	int mCurrentRecordId;
	/* must be hold in class to communicate with listeners */
	Cursor mCurrentRecordCursor = null;
	ContentResolver mCurrentRecordContentResolver = null;

	public static EditDateDialogFragment newInstance( int currentCategoryId ) {
		EditDateDialogFragment p = new EditDateDialogFragment();
		Bundle args = new Bundle();
		args.putInt("currentRecordId", currentCategoryId);
		p.setArguments(args);
		return p;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mCurrentRecordId = getArguments().getInt("currentRecordId");
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.fragment_dialog_edit_date, null);
		mDatePicker = (DatePicker) v.findViewById(R.id.dialog_edit_date); 
		String[] recordProjection = DailyTables.TABLE_RECORDS_AVAILABLE_COLUMS;
		final Uri recordUri = Uri.parse(DailyContentProvider.RECORDS_CONTENT_URI + "/" + mCurrentRecordId);
		mCurrentRecordContentResolver = getActivity().getContentResolver();
		mCurrentRecordCursor = mCurrentRecordContentResolver.query( recordUri, recordProjection, null, null, null);
		
		if(mCurrentRecordCursor.moveToFirst()) {
			
			
			/* Convert unix time stamp to mills */
			long unixTs = mCurrentRecordCursor.getInt(mCurrentRecordCursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE));
			Calendar c = Calendar.getInstance();
			long unixTms = TimeUnit.SECONDS.toMillis( unixTs );
			c.setTimeInMillis( unixTms );
			
			Log.d("Timestamp from DatePicker: ", "" + unixTs );
			Log.d("converted Time symbols from DatePicker: ", "" + c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH));
			mDatePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			
			return new AlertDialog.Builder(getActivity()).setTitle("Set Date...").setView(v).setCancelable(true).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d("", "Dialog confirmed");
					
					
					int unixDateYear = mDatePicker.getYear();
					int unixDateMonth = mDatePicker.getMonth();
					int unixDateDay = mDatePicker.getDayOfMonth();
					
					Calendar c = Calendar.getInstance();
					c.set(unixDateYear, unixDateMonth, unixDateDay);
					// get ms from calendar
					long unixTms = c.getTimeInMillis();
					// convert ms to s
					long unixTs = TimeUnit.MILLISECONDS.toSeconds(unixTms);
					Log.d("Timestamp from DatePicker: ", "" + unixTs);
					
					ContentValues values = new ContentValues();

					values.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE, unixTs);
					
					mCurrentRecordContentResolver.update(recordUri, values, null, null);
					mCurrentRecordCursor.close();
					
					//TODO: improve
					// ugly way to inform spinner about changed data
					RecordDetailFragment attachedFragment = (RecordDetailFragment) getFragmentManager().findFragmentById(R.id.record_detail_container);
					if(attachedFragment != null) {
						attachedFragment.refreshData();
					}
				}
			}).setNegativeButton("Abort", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					Log.d("", "Dialog abort");
				}
			}).create();
		} else {
			return null;	
		}
	}
}