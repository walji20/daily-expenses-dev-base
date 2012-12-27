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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SpinnerEditCategoryDialogFragment extends DialogFragment {
	EditText mCategoryTitle;
	Button mCategoryDelete;
	int mCurrentCategoryId;
	/* must be hold in class to communicate with listeners */
	Cursor mCurrentCategoryCursor = null;
	ContentResolver mContentResolver = null;
	//TODO: improve
	// ugly way to inform spinner about changed data
	RecordDetailFragment attachedFragment = null;

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
		mCategoryDelete = (Button) v.findViewById(R.id.dialog_category_delete);
		
		attachedFragment = (RecordDetailFragment) getFragmentManager().findFragmentById(R.id.record_detail_container);
		
		mCategoryDelete.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if( mCurrentCategoryId > -1 ) {
					mContentResolver.delete(Uri.parse(DailyContentProvider.CATEGORIES_CONTENT_URI + "/" + mCurrentCategoryId), null, null);
					
					if(attachedFragment != null) {
						attachedFragment.refreshData();
					}
					getDialog().cancel();
				}
			}
		});
		String[] categoryProjection = { DailyTables.TABLE_CATEGORIES_COLUMN_ID, DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
		final Uri categoryUri = Uri.parse(DailyContentProvider.CATEGORIES_CONTENT_URI + "/" + mCurrentCategoryId);
		mContentResolver = getActivity().getContentResolver();
		mCurrentCategoryCursor = mContentResolver.query( categoryUri, categoryProjection, null, null, null);
		
		if(mCurrentCategoryCursor.moveToFirst()) {
			
			mCategoryTitle.setText(mCurrentCategoryCursor.getString(mCurrentCategoryCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE)));
			return new AlertDialog.Builder(getActivity()).setTitle("Category Name...").setView(v).setCancelable(true).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d("", "Dialog confirmed");
					
					ContentValues values = new ContentValues();
					values.put(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE, mCategoryTitle.getText().toString());
					mContentResolver.update(categoryUri, values, null, null);
					mCurrentCategoryCursor.close();
					
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
			}).setNeutralButton("As new Category", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d("", "Dialog neutral");
					
					if(attachedFragment != null) {
						Uri currentRecordUri = attachedFragment.getRecordUri();
						
						int currentRecordId = Integer.parseInt(currentRecordUri.getLastPathSegment());
						if( currentRecordId > -1 ) {
							ContentValues categoryValues = new ContentValues();
							categoryValues.put(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE, mCategoryTitle.getText().toString());
							Uri categorySavedUri = mContentResolver.insert(DailyContentProvider.CATEGORIES_CONTENT_URI, categoryValues);
							int categorySavedId = Integer.parseInt(categorySavedUri.getLastPathSegment());
							if( categorySavedId > -1 ) {
								
								ContentValues recordValues = new ContentValues();
								recordValues.put(DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE, categorySavedId);
								mContentResolver.update(Uri.parse(DailyContentProvider.RECORDS_CONTENT_URI + "/" + currentRecordId), recordValues, null, null);
							}
						}
						attachedFragment.refreshData();
					}
					dialog.cancel();
				}
			}).create();
		} else {
			return null;	
		}
	}
}