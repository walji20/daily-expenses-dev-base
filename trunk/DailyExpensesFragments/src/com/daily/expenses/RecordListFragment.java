package com.daily.expenses;

import static com.daily.expenses.util.LogUtils.LOGD;
import static com.daily.expenses.util.LogUtils.makeLogTag;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyDatabaseHelper;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.dialogs.SelectDateDialogFragment;
import com.daily.expenses.dialogs.SelectDateDialogFragment.SelectDateDialog;
import com.daily.expenses.util.Clockwork;
import com.daily.expenses.util.Maps;
import com.daily.expenses.util.RecordFilter;
import com.daily.expenses.util.ValuePair;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link RecordDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class RecordListFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<Cursor>, SelectDateDialog {
	
	
	private static final String TAG = makeLogTag(DailyDatabaseHelper.class);
	
	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;
    
	// If non-null, this is the current filter the user has provided.
	private Uri mCurFilter;
	
	private RecordFilter ListFilter = new RecordFilter();

	String[] RECORDS_OVERVIEW_PROJECTION = new String[] { DailyTables.TABLE_RECORDS_COLUMN_ID, DailyTables.TABLE_RECORDS_COLUMN_TITLE, DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE  };
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(long id);

		boolean onContextItemSelected(MenuItem item);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(long id) {
		}

		@Override
		public boolean onContextItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	public Uri getmCurFilter() {
		return mCurFilter;
	}
	
	public RecordFilter getListFilter() {
		return ListFilter;
	}

	public void setListFilter(RecordFilter listFilter) {
		ListFilter = listFilter;
	}

	public void setmCurFilter(Uri mCurFilter) {
		this.mCurFilter = mCurFilter;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RecordListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Important, otherwise the definitions for the fragment’s onCreateOptionsMenu() and onOptionsItemSelected() methods, and optionally onPrepareOptionsMenu(), onOptionsMenuClosed(), and onDestroyOptionsMenu() methods are not called */
		setHasOptionsMenu(true); 
		
		if (savedInstanceState != null) {
			
			SelectDateDialogFragment sd = (SelectDateDialogFragment) getSherlockActivity().getSupportFragmentManager().findFragmentByTag("SelectDateDialog"); // "tag" is the string set as the tag for the dialog when you show it
			if (sd != null) {
				// the dialog exists so update its listener
				sd.setListener(this);
			}
		}
		
		/*
		 * Red[128225] Ensure compatibility for pre API 11 devices - list
		 * highlighting
		 */
		int layout = android.R.layout.simple_list_item_2;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			layout = android.R.layout.simple_list_item_activated_2;
		}

		int[] to = new int[] { 0, android.R.id.text1, android.R.id.text2 };

		getActivity().getSupportLoaderManager().initLoader(0, null, this); 
		mAdapter = new SimpleCursorAdapter(this.getActivity(), layout, null, RECORDS_OVERVIEW_PROJECTION, to, 0);
		mAdapter.setViewBinder(new ViewBinder() {
			/* Override this View to convert unix to readable date */
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

		        if (columnIndex == cursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE)) {
		                String createDate = cursor.getString(columnIndex);
		                TextView textView = (TextView) view;
		                Calendar c = Calendar.getInstance();
		                c.setTimeInMillis(TimeUnit.SECONDS.toMillis(cursor.getLong(cursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE))));
		                textView.setText( Clockwork.convertToHumanReadable( getActivity(), cursor.getLong(cursor.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE)) ));
		                return true;
		         }
				return false;
			}
		});
		setListAdapter(mAdapter);
		
	}

	public void fillData() {
		if( mAdapter != null ) {
			getActivity().getSupportLoaderManager().restartLoader(0, null, this);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		Uri baseUri;
		
		if (getmCurFilter() != null) {
			baseUri = getmCurFilter();
			LOGD(TAG, baseUri.toString());
		} else {
			baseUri = DailyContentProvider.RECORDS_CONTENT_URI;
		}
		
		// Get current filter parameters
		RecordFilter filter = getListFilter();
		String select = filter.getSelection();
		String[] selectArgs = filter.getSelectionArgs();
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		return new CursorLoader(getActivity(), baseUri, RECORDS_OVERVIEW_PROJECTION, select, selectArgs, DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + " COLLATE LOCALIZED DESC");
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.filter: {
				 LOGD(TAG, "Filter called.");
				
				 
				 SelectDateDialogFragment dialog = SelectDateDialogFragment.newInstance();
				 dialog.setListener((SelectDateDialog) this);
				 dialog.show(getActivity().getSupportFragmentManager(), "SelectDateDialog");
				 getSherlockActivity().getSupportActionBar().getDisplayOptions();
				 
				 return true;
			}
	        case R.id.filter_delete: {
	        	
	        	return true;
	        }
	       
	        default:
	        // Not one of ours. Perform default menu processing
	        return super.onOptionsItemSelected(item);
	       }
	 }

	@Override
	public void onSelectDateDialogPositiveClick(ValuePair dates) {

		RecordFilter filter = new RecordFilter();
		Map<String, String> selectionMap = Maps.newHashMap();
		selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + ">=?", "" + dates.getValue1());
		selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + "<=?", ""+  dates.getValue2());
		filter.set(selectionMap);
		setListFilter(filter);
		fillData();
	}

	@Override
	public void onSelectDateDialogDialogNeutralClick() {
		
		RecordFilter filter = new RecordFilter();
		filter.reset();
		
		setListFilter(filter);
		fillData();
	}

	@Override
	public void onSelectDateDialogNegativeClick() {
		// TODO Auto-generated method stub
		
	}
}
