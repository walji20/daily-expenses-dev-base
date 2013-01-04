package com.daily.expenses;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.util.Clockwork;
import com.daily.expenses.util.GraphsHelper;
import com.daily.expenses.util.Maps;
import com.daily.expenses.util.RecordFilter;
import com.daily.expenses.util.StringValuePair;
import com.daily.expenses.util.ValuePair;

public class GraphCategories extends SherlockFragmentActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_graph_month);
        

        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            Fragment newFragment = GraphCategoriesFragment.newInstance(1);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.simple_fragment, newFragment).commit();
        } else {
            //mStackLevel = savedInstanceState.getInt("level");
        }
    }

	public static class GraphCategoriesFragment extends SherlockFragment {
	int mNum;

    /**
	* Create a new instance of CountingFragment, providing "num"
	* as an argument.
	*/
        static GraphCategoriesFragment newInstance(int num) {
        	GraphCategoriesFragment f = new GraphCategoriesFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
		* When creating, retrieve this instance's number from its arguments.
		*/
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
		* The Fragment's UI is just a simple text view showing its
		* instance number.
		*/
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //View v = inflater.inflate(R.layout.hello_world, container, false);
        	LinearLayout layout = new LinearLayout(getActivity());
        	GraphicalView v;
        	double income = 0;
        	/* used to get colors */
        	int colorIndex = 0;
        	String select;
        	String[] selectArgs;
        	
        	String[] recordProjection = { DailyTables.TABLE_RECORDS_COLUMN_ID, DailyTables.TABLE_RECORDS_COLUMN_TITLE, DailyTables.TABLE_RECORDS_COLUMN_AMOUNT };
        	String[] categoryProjection = { DailyTables.TABLE_CATEGORIES_COLUMN_ID, DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
        	
        	CategorySeries series = new CategorySeries("Chart");
        	DefaultRenderer render = new DefaultRenderer();
        	ArrayList<StringValuePair> expensesValues = new ArrayList<StringValuePair>();
        	
        	// get categories
        	Cursor categoryCursor = getSherlockActivity().getContentResolver().query(DailyContentProvider.CATEGORIES_CONTENT_URI, categoryProjection, null, null, null);
        	for (boolean hasItem = categoryCursor.moveToFirst(); hasItem; hasItem = categoryCursor.moveToNext()) {
        		double expenses = 0;
        		colorIndex++;
        		String categoryTitle = categoryCursor.getString(categoryCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE));
        	    long categoryId = categoryCursor.getLong(categoryCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_ID));
        	   
        		// manage expense Records
            	Map<String, String> selectionMap = Maps.newHashMap();
            	RecordFilter filter = new RecordFilter();
            	filter.reset();
            	selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE + "=?", "" + 1);
            	selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE + "=?", "" + categoryId);
            	filter.set(selectionMap);
            	
            	select = filter.getSelection();
            	selectArgs = filter.getSelectionArgs();
            	
            	
            	Cursor expensesRecords = getActivity().getContentResolver().query(DailyContentProvider.RECORDS_CONTENT_URI, recordProjection , select, selectArgs, null);
            	
            	for (boolean hasItem2 = expensesRecords.moveToFirst(); hasItem2; hasItem2 = expensesRecords.moveToNext()) {
            		String recordTitle = expensesRecords.getString(expensesRecords.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_TITLE));
            		double recordAmount = expensesRecords.getDouble(expensesRecords.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_AMOUNT));
            		expensesValues.add(new StringValuePair( recordTitle, recordAmount ));
            		expenses += recordAmount;
            	}
            	SimpleSeriesRenderer incomeSeriesRenderer = new SimpleSeriesRenderer();
            	 incomeSeriesRenderer.setColor( GraphsHelper.getColorCode( colorIndex ) );
            	 render.addSeriesRenderer(incomeSeriesRenderer);
            	series.add(categoryTitle + ": " + expenses, expenses);
			}
        	
        	render.setInScroll(true);
          	render.setPanEnabled(true);
          	render.setClickEnabled(false);
          	render.setChartTitleTextSize(26);
            render.setShowLabels(true);
    		render.setShowLegend(true);
    		render.setShowGrid(true);
    		render.setBackgroundColor(Color.BLACK);
    		render.setLabelsTextSize(26);
    		render.setLabelsColor(Color.BLACK);
    		render.setLegendTextSize(26);
        	v = ChartFactory.getPieChartView(getActivity(), series, render);
        	//layout.removeAllViews();
        	layout.addView(v);
        	
            return layout;
        }
    }
}
