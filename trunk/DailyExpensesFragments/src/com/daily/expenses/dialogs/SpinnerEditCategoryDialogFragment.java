package com.daily.expenses.dialogs;

import com.daily.expenses.R;
import com.daily.expenses.RecordDetailFragment;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.dialogs.EditDateDialogFragment.EditDateDialogListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SpinnerEditCategoryDialogFragment extends DialogFragment {
	EditText mCategoryTitle;
	Button mCategoryDelete;
	private int mCurrentCategoryId;
	private String mCurrentCategoryTitle;
	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
	public interface SpinnerEditCategoryDialogListener {
		public void onSpinnerEditCategoryDialogPositiveClick(int categoryId, String categoryTitle);
		public void onSpinnerEditCategoryDialogNeutralClick(int categoryId, String categoryTitle);
		public void onSpinnerEditCategoryDialogNegativeClick(DialogFragment dialog);
		public void onSpinnerEditCategoryDialogDeleteClick(int categoryId);
	}
	
	// Use this instance of the interface to deliver action events
	SpinnerEditCategoryDialogListener mListener = new SpinnerEditCategoryDialogListener() {
		
		@Override
		public void onSpinnerEditCategoryDialogPositiveClick(int categoryId, String categoryTitle) {
			//Should always overridden by interface implementing class
		}
		
		@Override
		public void onSpinnerEditCategoryDialogNeutralClick(int categoryId, String categoryTitle) {
			//Should always overridden by interface implementing class
		}
		
		@Override
		public void onSpinnerEditCategoryDialogNegativeClick(DialogFragment dialog) {
			//Should always overridden by interface implementing class
		}

		@Override
		public void onSpinnerEditCategoryDialogDeleteClick(int categoryId) {
			// TODO Auto-generated method stub
			
		}
	};

	public void setListener(SpinnerEditCategoryDialogListener listener) {
		mListener = listener;
	}
	 
    
	public static SpinnerEditCategoryDialogFragment newInstance( int currentCategoryId, String currentCategoryTitle ) {
		SpinnerEditCategoryDialogFragment p = new SpinnerEditCategoryDialogFragment();
		Bundle args = new Bundle();
		args.putInt("currentCategoryId", currentCategoryId);
		args.putString("currentCategoryTitle", currentCategoryTitle);
		p.setArguments(args);
		return p;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mCurrentCategoryId = getArguments().getInt("currentCategoryId");
		mCurrentCategoryTitle = getArguments().getString("currentCategoryTitle");
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.fragment_dialog_category_spinner, null);
		mCategoryTitle = (EditText) v.findViewById(R.id.dialog_category_title);
		mCategoryDelete = (Button) v.findViewById(R.id.dialog_category_delete);
		
		
		mCategoryDelete.setOnClickListener( new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				
				mListener.onSpinnerEditCategoryDialogDeleteClick(mCurrentCategoryId);
				getDialog().cancel();
				
//				if( mCurrentCategoryId > -1 ) {
//					mContentResolver.delete(Uri.parse(DailyContentProvider.CATEGORIES_CONTENT_URI + "/" + mCurrentCategoryId), null, null);
//					
//					if(attachedFragment != null) {
//						attachedFragment.refreshData();
//					}
//					getDialog().cancel();
//				}
			}
				
		});

			
			mCategoryTitle.setText( mCurrentCategoryTitle );
			
			return new AlertDialog.Builder(getActivity()).setTitle("Category Name...").setView(v).setCancelable(true).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d("", "Dialog confirmed");
					mListener.onSpinnerEditCategoryDialogPositiveClick(mCurrentCategoryId, mCurrentCategoryTitle);
					
				}
			}).setNegativeButton("Abort", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d("", "Dialog abort");
					
					dialog.cancel();
				}
			}).setNeutralButton("As new Category", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d("", "Dialog neutral");
					
					mListener.onSpinnerEditCategoryDialogNeutralClick(mCurrentCategoryId, mCategoryTitle.getText().toString());
					dialog.cancel();
				}
			}).create();
	}
}