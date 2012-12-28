package com.daily.expenses.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.daily.expenses.R;
import com.daily.expenses.RecordListFragment;
import com.daily.expenses.contentprovider.DailyContentProvider;

public class SelectDateDialogFragment extends DialogFragment {
	DatePicker mDatePickerFrom;
	DatePicker mDatePickerTo;
	
	public static SelectDateDialogFragment newInstance() {
		SelectDateDialogFragment p = new SelectDateDialogFragment();
		return p;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.fragment_dialog_select_date, null);
		mDatePickerFrom = (DatePicker) v.findViewById(R.id.dialog_select_date_from); 
		mDatePickerTo = (DatePicker) v.findViewById(R.id.dialog_select_date_to); 
		
		return new AlertDialog.Builder(getActivity()).setTitle("Select Date...").setView(v).setCancelable(true).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("", "Dialog confirmed");
				
				DatePicker[] datePicker = { mDatePickerFrom, mDatePickerTo };
				ArrayList<Long> resultSet = new ArrayList<Long>();
				
				for (DatePicker currentDatePicker : datePicker) {
					
					int unixDateYear = currentDatePicker.getYear();
					int unixDateMonth = currentDatePicker.getMonth();
					int unixDateDay = currentDatePicker.getDayOfMonth();
					
					Calendar c = Calendar.getInstance();
					c.set(unixDateYear, unixDateMonth, unixDateDay);
					// get ms from calendar
					long unixTms = c.getTimeInMillis();
					// convert ms to s
					long unixTs = TimeUnit.MILLISECONDS.toSeconds(unixTms);
					Log.d("Timestamp from DatePicker: ", "" + unixTs);
					resultSet.add(unixTs);
				}
				
				//TODO: improve
				// ugly way to inform spinner about changed data
				RecordListFragment attachedFragment = (RecordListFragment) getFragmentManager().findFragmentById(R.id.record_list);
				if(attachedFragment != null) {
					
					Uri mCurFilter = DailyContentProvider.buildBlocksBetweenDirUri(resultSet.get(0), resultSet.get(1));
					attachedFragment.setmCurFilter(mCurFilter);
					/* refill data */
					attachedFragment.fillData();
				}
			}
		}).setNegativeButton("Abort", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				Log.d("", "Dialog abort");
			}
		}).create();
	}
}