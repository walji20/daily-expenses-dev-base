package com.daily.expenses;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.util.LogUtils;

public class Grid extends Activity {
	private static final String TAG = LogUtils.makeLogTag(DailyContentProvider.class);
	GridView gridview;
	//The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
	private float mLastTouchX;
	private float mLastTouchY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		
		gridview = (GridView) findViewById(R.id.gridview);
		
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	    	@Override
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(Grid.this, "pos: " + position + "id: " + id, Toast.LENGTH_SHORT).show();
	        }
	    });
	    gridview.setOnTouchListener( new OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent me) {
		          
	       	 final int action = MotionEventCompat.getActionMasked(me); 
	       	 int pos;
	       	 
	       	 switch (action) { 
	       	 case MotionEvent.ACTION_DOWN: {
	       	     final int pointerIndex = MotionEventCompat.getActionIndex(me); 
	       	     final float x = MotionEventCompat.getX(me, pointerIndex); 
	       	     final float y = MotionEventCompat.getY(me, pointerIndex); 
	       	         
	       	     // Remember where we started (for dragging)
	       	     mLastTouchX = x;
	       	     mLastTouchY = y;
	       	     // Save the ID of this pointer (for dragging)
	       	     mActivePointerId = MotionEventCompat.getPointerId(me, 0);
	       	    
	       	     pos = gridview.pointToPosition((int) x, (int) y);
	       	     Log.d(TAG, "from: " + pos);
	       	     break;
	       	 }
	       	         
	       	 case MotionEvent.ACTION_MOVE: {
	       	     // Find the index of the active pointer and fetch its position
	       	     final int pointerIndex = 
	       	             MotionEventCompat.findPointerIndex(me, mActivePointerId);  
	       	         
	       	     final float x = MotionEventCompat.getX(me, pointerIndex);
	       	     final float y = MotionEventCompat.getY(me, pointerIndex);
	       	         
	       	     // Remember this touch position for the next move event
	       	     mLastTouchX = x;
	       	     mLastTouchY = y;
	       	
	       	     break;
	       	 }
	       	         
	       	 case MotionEvent.ACTION_UP: {
	       		 pos = gridview.pointToPosition((int) mLastTouchX, (int) mLastTouchY);
	       	     Log.d(TAG, "to: " + pos);
	       	     break;
	       	 }
	       	         
	       	 case MotionEvent.ACTION_CANCEL: {
	       	     mActivePointerId = MotionEvent.INVALID_POINTER_ID;
	       	     break;
	       	 }
	       	     
	       	 case MotionEvent.ACTION_POINTER_UP: {
	       	         
	       	 }
	       	 }       
	       	 return true;
	       	}
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_grid, menu);
		return true;
		
	}
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }
	    @Override
	    public Object getItem(int position) {
	        return mThumbIds[position];
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            //imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    }

	    // references to our images
	    private Integer[] mThumbIds = {
	            R.drawable.device_access_accounts, R.drawable.device_access_battery,
	            R.drawable.device_access_accounts, R.drawable.device_access_battery,
	            R.drawable.device_access_accounts, R.drawable.device_access_battery,
	            R.drawable.device_access_accounts, R.drawable.device_access_battery,
	            R.drawable.device_access_accounts, R.drawable.device_access_battery,
	            R.drawable.device_access_accounts, R.drawable.device_access_battery,
	            R.drawable.device_access_accounts, R.drawable.device_access_battery,
	    };
	}
}
