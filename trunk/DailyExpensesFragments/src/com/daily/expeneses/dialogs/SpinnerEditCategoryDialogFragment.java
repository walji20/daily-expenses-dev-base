package com.daily.expeneses.dialogs;

import com.daily.expenses.R;
import com.daily.expenses.RecordDetailFragment;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;

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
import android.widget.EditText;

public class SpinnerEditCategoryDialogFragment extends DialogFragment {
	EditText mCategoryTitle;
	int mCurrentCategoryId;
	/* must be hold in class to communicate with listeners */
	Cursor mCurrentCategoryCursor = null;
	ContentResolver mCurrentCategorContentResolver = null;

	public static SpinnerEditCategoryDialogFragment newInstance( int currentCategoryId ) {
		SpinnerEditCategoryDialogFragment p = new SpinnerEditCategoryDialogFragment();
		Bundle args = new Bundle();
		args.putInt("currentCategoryId", currentCategoryId);
		p.setArguments(args);
		return p;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mCurrentCategoryId = getArguments().getInt("currentCategoryId");
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.fragment_dialog_category_spinner, null);
		mCategoryTitle = (EditText) v.findViewById(R.id.dialog_category_title);
		String[] categoryProjection = { DailyTables.TABLE_CATEGORIES_COLUMN_ID, DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
		final Uri categoryUri = Uri.parse(DailyContentProvider.CATEGORIES_CONTENT_URI + "/" + mCurrentCategoryId);
		mCurrentCategorContentResolver = getActivity().getContentResolver();
		mCurrentCategoryCursor = mCurrentCategorContentResolver.query( categoryUri, categoryProjection, null, null, null);
		
		if(mCurrentCategoryCursor.moveToFirst()) {
			
			mCategoryTitle.setText(mCurrentCategoryCursor.getString(mCurrentCategoryCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE)));
			return new AlertDialog.Builder(getActivity()).setTitle("Category Name...").setView(v).setCancelable(true).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d("", "Dialog confirmed");
					
					ContentValues values = new ContentValues();
					values.put(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE, mCategoryTitle.getText().toString());
					mCurrentCategorContentResolver.update(categoryUri, values, null, null);
					mCurrentCategoryCursor.close();
					
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