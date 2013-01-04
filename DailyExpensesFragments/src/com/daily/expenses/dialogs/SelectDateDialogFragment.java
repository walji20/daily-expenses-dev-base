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
				
				//TODO: improve
				// ugly way to inform spinner about changed data
				RecordListFragment attachedFragment = (RecordListFragment) getFragmentManager().findFragmentById(R.id.record_list);
				if(attachedFragment != null) {
					RecordFilter filter = new RecordFilter();
					Map<String, String> selectionMap = Maps.newHashMap();
					selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + ">=?", "" + dates.getValue1());
					selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + "<=?", ""+  dates.getValue2());
					filter.set(selectionMap);
					attachedFragment.setListFilter(filter);
					/* refill data */
					attachedFragment.fillData();
				}
			}
		}).setNeutralButton("Delete Filter", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("", "Delete Filter");
				RecordFilter filter = new RecordFilter();
				filter.reset();
				RecordListFragment attachedFragment = (RecordListFragment) getFragmentManager().findFragmentById(R.id.record_list);
				if(attachedFragment != null) {
					attachedFragment.setListFilter(filter);
					attachedFragment.fillData();
				}
				dialog.cancel();
			}
		}).setNegativeButton("Abort", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("", "Dialog abort");
				dialog.cancel();
			}
		}).create();
	}
}