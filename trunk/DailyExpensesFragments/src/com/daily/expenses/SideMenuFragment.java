package com.daily.expenses;

import com.actionbarsherlock.app.SherlockListFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SideMenuFragment extends SherlockListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		adapter.add(new SampleItem(getActivity().getString(R.string.graphs), R.drawable.collections_sort_by_size, false));
		adapter.add(new SampleItem(getActivity().getString(R.string.preferences), R.drawable.action_settings, true));
		adapter.add(new SampleItem(getActivity().getString(R.string.about), R.drawable.action_about, false));
		
		setListAdapter(adapter);
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if( sdk < android.os.Build.VERSION_CODES.JELLY_BEAN ) {
			view.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.fabric_pattern_light));
		} else {
			view.setBackground(getActivity().getResources().getDrawable(R.drawable.fabric_pattern_light));
		}
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		switch (position) {
			case 0:
			startActivity(new Intent(getActivity(), GraphsActivity.class));
			break;
		}
		
	}
	
	private class SampleItem {
		public String tag;
		public int iconRes;
		public boolean prime;

		public SampleItem(String tag, int iconRes, boolean prime) {
			this.tag = tag;
			this.iconRes = iconRes;
			this.prime = prime;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			// if no prime feature remove image
			if(!getItem(position).prime) {
				ImageView primeIdentifier = (ImageView) convertView.findViewById(R.id.prime);
				primeIdentifier.setVisibility(View.GONE);
			}
			return convertView;
		}

	}
}