package com.daily.expenses;


import java.util.ArrayList;
import java.util.Map;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.util.Maps;
import com.daily.expenses.util.RecordFilter;
import com.daily.expenses.util.ValuePair;

public class GraphOverview extends SherlockFragmentActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_graph_month);
        

        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            Fragment newFragment = GraphOverviewFragment.newInstance(1);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.simple_fragment, newFragment).commit();
        } else {
            //mStackLevel = savedInstanceState.getInt("level");
        }
    }

	public static class GraphOverviewFragment extends SherlockFragment {
	int mNum;

    /**
	* Create a new instance of CountingFragment, providing "num"
	* as an argument.
	*/
        static GraphOverviewFragment newInstance(int num) {
            GraphOverviewFragment f = new GraphOverviewFragment();

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
        	double expenses = 0;
        	double total = 0;
        	String select;
        	String[] selectArgs;
        	
        	String[] projection = { DailyTables.TABLE_RECORDS_COLUMN_ID, DailyTables.TABLE_RECORDS_COLUMN_TITLE, DailyTables.TABLE_RECORDS_COLUMN_AMOUNT };
			
        	RecordFilter filter = new RecordFilter();
        	filter.reset();
        	// manage Income Records
        	Map<String, String> selectionMap = Maps.newHashMap();
			selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE + "=?", "" + 0);
			//selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + "=?", ""+  0);
			filter.set(selectionMap);
			
    		select = filter.getSelection();
    		selectArgs = filter.getSelectionArgs();
        	
    		ArrayList<ValuePair> incomeValues = new ArrayList<ValuePair>();
    		
        	Cursor incomeRecords = getActivity().getContentResolver().query(DailyContentProvider.RECORDS_CONTENT_URI, projection , select, selectArgs, null);
        	
        	for (boolean hasItem = incomeRecords.moveToFirst(); hasItem; hasItem = incomeRecords.moveToNext()) {
        		String title = incomeRecords.getString(incomeRecords.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_TITLE));
        	    double amount = incomeRecords.getDouble(incomeRecords.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_AMOUNT));
        		incomeValues.add(new ValuePair( title, amount ));
        		income += amount;
			}
        	
        	// manage expense Records
        	filter.reset();
        	selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE + "=?", "" + 1);
        	filter.set(selectionMap);
        	
        	select = filter.getSelection();
        	selectArgs = filter.getSelectionArgs();
        	
        	ArrayList<ValuePair> expensesValues = new ArrayList<ValuePair>();
        	
        	Cursor expensesRecords = getActivity().getContentResolver().query(DailyContentProvider.RECORDS_CONTENT_URI, projection , select, selectArgs, null);
        	
        	for (boolean hasItem = expensesRecords.moveToFirst(); hasItem; hasItem = expensesRecords.moveToNext()) {
        		String title = expensesRecords.getString(expensesRecords.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_TITLE));
        		double amount = expensesRecords.getDouble(expensesRecords.getColumnIndexOrThrow(DailyTables.TABLE_RECORDS_COLUMN_AMOUNT));
        		expensesValues.add(new ValuePair( title, amount ));
        		expenses += amount;
        	}
        	
        	total = income - expenses;
            if(total <= -1) {
            	total = 0;
            }
            CategorySeries series = new CategorySeries("Chart");
//			series.add(getActivity().getString(R.string.income), income);
//			series.add(getActivity().getString(R.string.costs), costs);
//    		series.add(getActivity().getString(R.string.total), income - costs);
    		
    		
    		 DefaultRenderer render = new DefaultRenderer();
    		 
//             for ( ValuePair entry : incomeValues) {
//            		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
//                 	//r.setColor(color);
//                 	render.addSeriesRenderer(r);
//                 	series.add(getActivity().getString(R.string.income), entry.getValue2());
//             }
    		 
		    SimpleSeriesRenderer incomeSeriesRenderer = new SimpleSeriesRenderer();
		    incomeSeriesRenderer.setColor(Color.BLACK);
          	render.addSeriesRenderer(incomeSeriesRenderer);
          	series.add(getActivity().getString(R.string.income) +  " : " + income, income);
          	
          	SimpleSeriesRenderer expensesSeriesRenderer = new SimpleSeriesRenderer();
          	expensesSeriesRenderer.setColor(Color.RED);
          	render.addSeriesRenderer(expensesSeriesRenderer);
          	series.add(getActivity().getString(R.string.expenses) + " : " + expenses, expenses);
          	
          	SimpleSeriesRenderer totalSeriesRenderer = new SimpleSeriesRenderer();
          	totalSeriesRenderer.setColor(Color.BLUE);
          	render.addSeriesRenderer(totalSeriesRenderer);
          	series.add(getActivity().getString(R.string.total) + " : " + total, total);
             
//            CategorySeries series = new CategorySeries("Pie Graph");
//            int k = 0;
//            for(int value : values) {
//            	series.add("Section " + ++k, value);
//            }
//            
           
//            for( int color : colors) {
//            	SimpleSeriesRenderer r = new SimpleSeriesRenderer();
//            	r.setColor(color);
//            	render.addSeriesRenderer(r);
//            }
        	render.setInScroll(true);
          	render.setPanEnabled(true);
          	render.setClickEnabled(false);
          	
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
