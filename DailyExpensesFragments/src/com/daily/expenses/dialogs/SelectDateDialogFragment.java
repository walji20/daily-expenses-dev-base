package com.daily.expenses.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
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
import android.view.LayoutInflater.Filter;
import android.widget.DatePicker;

import com.daily.expenses.R;
import com.daily.expenses.RecordListFragment;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.util.Clockwork;
import com.daily.expenses.util.Maps;
import com.daily.expenses.util.RecordFilter;
import com.daily.expenses.util.ValuePair;

public class SelectDateDialogFragment extends DialogFragment {
	DatePicker mDatePickerFrom;
	DatePicker mDatePickerTo;

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
	public interface SelectDateDialog {
		public void onSelectDateDialogPositiveClick(ValuePair dates);
		public void onSelectDateDialogDialogNeutralClick();
		public void onSelectDateDialogNegativeClick();
	}
	
	// Use this instance of the interface to deliver action events
	SelectDateDialog mListener = new SelectDateDialog() {
		
		@Override
		public void onSelectDateDialogPositiveClick(ValuePair dates) {
			//Should always overridden by interface implementing class
		}
		
		@Override
		public void onSelectDateDialogDialogNeutralClick() {
			//Should always overridden by interface implementing class
		}
		
		@Override
		public void onSelectDateDialogNegativeClick() {
			//Should always overridden by interface implementing class
		}
	};

	public void setListener(SelectDateDialog listener) {
		mListener = listener;
	}
	
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
				
				long from = Clockwork.getMillis(mDatePickerFrom);
	        	long to = Clockwork.getMillis(mDatePickerFrom);
				ValuePair dates = Clockwork.getMaximumRange(Clockwork.DAY, from, to);
				
	        	mListener.onSelectDateDialogPositiveClick(dates);
			}
		}).setNeutralButton("Delete Filter", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("", "Delete Filter");
				
				mListener.onSelectDateDialogDialogNeutralClick();
				dialog.cancel();
			}
		}).setNegativeButton("Abort", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("", "Dialog abort");
				mListener.onSelectDateDialogNegativeClick();
				dialog.cancel();
			}
		}).create();
	}
}