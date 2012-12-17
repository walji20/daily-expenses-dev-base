package com.daily.expenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.daily.expenses.contentprovider.RecordsContentProvider;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecordListActivity}
 * in two-pane mode (on tablets) or a {@link RecordDetailActivity}
 * on handsets.
 */
public class RecordInsertFragment extends SherlockFragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecordInsertFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RecordsContentProvider.CONTENT_ITEM_TYPE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_insert, container, false);

        // Show the dummy content as text in a TextView.
        /*if (mItem != null) {
        	if(mItem.text != null) {
        		((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.text);
        	} else {
        		((TextView) rootView.findViewById(R.id.item_detail)).setText("edit content by long press an item on the left.");
			}
        }
        */
        return rootView;
    }
}
