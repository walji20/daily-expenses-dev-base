package com.daily.expenses;

import static com.daily.expenses.util.LogUtils.makeLogTag;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class AboutActivity extends SherlockActivity {
	private static final String TAG = makeLogTag(RecordDetailFragment.class);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
	
		TextView versionText = (TextView) findViewById(R.id.help_about_version);
        versionText.setText(getString(R.string.activity_about_version) + " " + getVersion());
        
	}
	
	 /**
	* Get the current package version.
	*
	* @return The current version.
	*/
    private String getVersion() {
        String result = "";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);

            result = String.format("%s (%s)", info.versionName, info.versionCode);
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Unable to get application version: " + e.getMessage());
            result = "Unable to get application version.";
        }

        return result;
    }
}
