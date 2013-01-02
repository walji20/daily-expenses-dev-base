package com.daily.expenses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyDatabaseHelper;
import com.daily.expenses.util.LogUtils;

import static com.daily.expenses.util.LogUtils.*;
/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link RecordDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link RecordListFragment} and the item details (if present) is a
 * {@link RecordDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link RecordListFragment.Callbacks} interface to listen for item selections.
 */
public class RecordListActivity extends SherlockFragmentActivity implements RecordListFragment.Callbacks {
	private static final String TAG = LogUtils.makeLogTag(DailyDatabaseHelper.class);
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	private RecordDetailFragment recordDetailFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);

		if (findViewById(R.id.record_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((RecordListFragment) getSupportFragmentManager().findFragmentById(R.id.record_list)).setActivateOnItemClick(true);
		}

		ListView list = (ListView) findViewById(android.R.id.list);
		//this.registerForContextMenu(list);
		//list.setOnCreateContextMenuListener(this);
		registerForContextMenu(list);
		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_record_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert: {
			if (mTwoPane) {
				// In two-pane mode, show the detail view in this activity by
				// adding or replacing the detail fragment using a
				// fragment transaction.
				Bundle arguments = new Bundle();
				arguments = null;
				// arguments.putString(RecordDetailFragment.ARG_ITEM_ID, id);
				// Uri recordUri =
				// Uri.parse(DailyContentProvider.RECORDS_CONTENT_URI + "/");
				// arguments.putParcelable(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE,
				// recordUri);

				recordDetailFragment = new RecordDetailFragment();

				recordDetailFragment.setArguments(arguments);
				getSupportFragmentManager().beginTransaction().replace(R.id.record_detail_container, recordDetailFragment).commit();

			} else {
				// In single-pane mode, simply start the detail activity
				// for the selected item ID.
				Intent detailIntent = new Intent(this, RecordDetailActivity.class);
				startActivity(detailIntent);
			}

			return true;
		}
		case R.id.grid: {
			startActivity(new Intent(this, Grid.class));
			return true;
		}
		case R.id.graphs: {
			startActivity(new Intent(this, GraphsActivity.class));
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link RecordListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(long id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			// arguments.putString(RecordDetailFragment.ARG_ITEM_ID, id);
			Uri recordUri = Uri.parse(DailyContentProvider.RECORDS_CONTENT_URI + "/" + id);
			arguments.putParcelable(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE, recordUri);

			recordDetailFragment = new RecordDetailFragment();

			recordDetailFragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.record_detail_container, recordDetailFragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, RecordDetailActivity.class);
			Uri recordUri = Uri.parse(DailyContentProvider.RECORDS_CONTENT_URI + "/" + id);
			detailIntent.putExtra(DailyContentProvider.RECORDS_CONTENT_ITEM_TYPE, recordUri);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		Log.d("onCreateContextMenu", "onCreateContextMenu called");
		menu.add(0, DELETE_ID, 0, "Delete");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		Log.d("onContextItemSelected", "onContextItemSelected called");
		Log.d("id", "" + item.getItemId());
		switch (item.getItemId()) {
		case DELETE_ID:
			
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		    int index = (int) info.id;
		    
			Uri uri = Uri.parse(DailyContentProvider.RECORDS_CONTENT_URI + "/" + index);
			getContentResolver().delete(uri, null, null);
			Log.d("", "Delete selected");
			//TODO: Only detach if different
			if(recordDetailFragment != null) {
				// Only if fragment is attached
				getSupportFragmentManager().beginTransaction().detach(recordDetailFragment).commit();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d("onContextItemSelected", "onContextItemSelected called");
		// TODO Auto-generated method stub
		return false;
	}
}
