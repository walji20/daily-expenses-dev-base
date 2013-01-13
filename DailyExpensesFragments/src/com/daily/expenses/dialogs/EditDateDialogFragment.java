package com.daily.expenses.dialogs;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.daily.expenses.R;

public class EditDateDialogFragment extends DialogFragment {
	public DatePicker mDatePicker;
	long currentUnixTime;
	
	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
	public interface EditDateDialogListener {
		public void onEditDateDialogPositiveClick(int year, int month, int day);
		public void onEditDateDialogNegativeClick(DialogFragment dialog);
	}
	
	// Use this instance of the interface to deliver action events
	EditDateDialogListener mListener = new EditDateDialogListener() {
		
		@Override
		public void onEditDateDialogPositiveClick(int year, int month, int day) {
			//Should always overridden by interface implementing class
		}
		
		@Override
		public void onEditDateDialogNegativeClick(DialogFragment dialog) {
			//Should always overridden by interface implementing class
		}
	};
	 
    
	public void setListener(EditDateDialogListener listener) {
		mListener = listener;
	}
	
	public static EditDateDialogFragment newInstance( long currentUnixTime ) {
		EditDateDialogFragment p = new EditDateDialogFragment();
		Bundle args = new Bundle();
		args.putLong("currentUnixTime", currentUnixTime);
		p.setArguments(args);
		return p;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		currentUnixTime = getArguments().getLong("currentUnixTime");
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.fragment_dialog_edit_date, null);
		mDatePicker = (DatePicker) v.findViewById(R.id.dialog_edit_date); 
		
		//Just update date if it's not a new record 
		if(this.currentUnixTime > 0) {
			/* Convert unix time stamp to mills */
			long unixTs = this.currentUnixTime;
			Calendar c = Calendar.getInstance();
			long unixTms = TimeUnit.SECONDS.toMillis( unixTs );
			c.setTimeInMillis( unixTms );
			
			Log.d("Timestamp from DatePicker: ", "" + unixTs );
			Log.d("converted Time symbols from DatePicker: ", "" + c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH));
			mDatePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		} else {
			// new record - DatePicker should set current date
		}
		return new AlertDialog.Builder(getActivity()).setTitle("Set Date...").setView(v).setCancelable(true)
			.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("", "Dialog confirmed");
				
				//Return Y, M, D
				mListener.onEditDateDialogPositiveClick(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
				
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
