package com.daily.expenses;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
* This demonstrates how you can implement switching between the tabs of a
* TabHost through fragments. It uses a trick (see the code below) to allow
* the tabs to switch between fragments instead of simple views.
*/
public class GraphsActivity extends SherlockFragmentActivity {
    TabHost mTabHost;
    TabManager mTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_graphs);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabManager.addTab(mTabHost.newTabSpec("graph_overview").setIndicator(getResources().getString(R.string.graph_tab_overview)), GraphOverview.GraphOverviewFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("graph_month").setIndicator(getResources().getString(R.string.graph_tab_month)), GraphMonth.GraphMonthFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("graph_categories").setIndicator(getResources().getString(R.string.graph_tab_categories)), GraphCategories.GraphCategoriesFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("graph_flow").setIndicator(getResources().getString(R.string.graph_tab_flow)), GraphFlow.GraphFlowFragment.class, null);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    switch (item.getItemId()) {
		
	    	case android.R.id.home: {
	        // This ID represents the Home or Up button. In the case of this
	        // activity, the Up button is shown. Use NavUtils to allow users
	        // to navigate up one level in the application structure. For
	        // more details, see the Navigation pattern on Android Design:
	        //
	        // http://developer.android.com/design/patterns/navigation.html#up-vs-back
	        //
	        NavUtils.navigateUpTo(this, new Intent(this, RecordListActivity.class));
	        return true;
	    	}  
		}
		 return super.onOptionsItemSelected(item);
    }
    /**
* This is a helper class that implements a generic mechanism for
* associating fragments with the tabs in a tab host. It relies on a
* trick. Normally a tab host has a simple API for supplying a View or
* Intent that each tab will show. This is not sufficient for switching
* between fragments. So instead we make the content part of the tab host
* 0dp high (it is not shown) and the TabManager supplies its own dummy
* view to show as the tab content. It listens to changes in tabs, and takes
* care of switch to the correct fragment shown in a separate content area
* whenever the selected tab changes.
*/
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state. If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }
}
