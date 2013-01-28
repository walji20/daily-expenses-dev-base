package com.daily.expenses;

import static com.daily.expenses.util.LogUtils.makeLogTag;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class PreferencesActivity extends SherlockPreferenceActivity {
	private static final String TAG = makeLogTag(PreferencesActivity.class);
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		final Preference syncPref = (Preference) findPreference(getString(R.string.pref_category_sync_key));
		syncPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				//TODO: open browser or intent here
				StringBuilder builder = new StringBuilder();
				Log.w(TAG, "" + syncPref.getTitle() + " was clicked");
				return false;
			}
		});
	}
}
