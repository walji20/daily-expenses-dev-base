package com.daily.expenses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.daily.expenses.contentprovider.RecordsContentProvider;

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
		this.registerForContextMenu(list);
		list.setOnCreateContextMenuListener(this);
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
		case R.id.insert:
			if (mTwoPane) {
				// In two-pane mode, show the detail view in this activity by
				// adding or replacing the detail fragment using a
				// fragment transaction.
				Bundle arguments = new Bundle();
				arguments = null;
				// arguments.putString(RecordDetailFragment.ARG_ITEM_ID, id);
				//Uri recordUri = Uri.parse(RecordsContentProvider.CONTENT_URI + "/");
				//arguments.putParcelable(RecordsContentProvider.CONTENT_ITEM_TYPE, recordUri);

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
			Uri recordUri = Uri.parse(RecordsContentProvider.CONTENT_URI + "/" + id);
			arguments.putParcelable(RecordsContentProvider.CONTENT_ITEM_TYPE, recordUri);

			recordDetailFragment = new RecordDetailFragment();

			recordDetailFragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.record_detail_container, recordDetailFragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, RecordDetailActivity.class);
			Uri recordUri = Uri.parse(RecordsContentProvider.CONTENT_URI + "/" + id);
			detailIntent.putExtra(RecordsContentProvider.CONTENT_ITEM_TYPE, recordUri);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add(0, DELETE_ID, 0, "Delete");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d("id", "" + item.getItemId());
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			Uri uri = Uri.parse(RecordsContentProvider.CONTENT_URI + "/" + info.id);
			getContentResolver().delete(uri, null, null);
			Log.d("", "Delete selected");
			return true;
		}
		return false;
	}
}
