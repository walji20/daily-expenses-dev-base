package com.daily.expenses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.daily.expenses.contentprovider.DailyContentProvider;
import com.daily.expenses.database.DailyTables;
import com.daily.expenses.dialogs.SelectDateDialogFragment;
import com.daily.expenses.dialogs.SelectDateDialogFragment.SelectDateDialog;
import com.daily.expenses.util.Maps;
import com.daily.expenses.util.RecordFilter;
import com.daily.expenses.util.StringValuePair;
import com.daily.expenses.util.ValuePair;

public class GraphFlow extends SherlockFragmentActivity {
	GraphFlowFragment mCategoriesFragment;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_graph_flow);
        

        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            mCategoriesFragment = GraphFlowFragment.newInstance(1);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_graph_flow, mCategoriesFragment).commit();
        } else {
            //mStackLevel = savedInstanceState.getInt("level");
        }
    }
	
	
	
	public static class GraphFlowFragment extends SherlockFragment implements SelectDateDialogFragment.SelectDateDialog {
	int mNum;
	Button FilterButton;
	private ValuePair mDateValuesFilter = null;

    /**
	* Create a new instance of CountingFragment, providing "num"
	* as an argument.
	*/
        static GraphFlowFragment newInstance(int num) {
        	GraphFlowFragment f = new GraphFlowFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }
        @Override
        public void onResume() {
        	super.onResume();
			drawChart();
        };
        
	
        /**
		* When creating, retrieve this instance's number from its arguments.
		*/
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
        	if (savedInstanceState != null) {
    			
    			SelectDateDialogFragment sd = (SelectDateDialogFragment) getSherlockActivity().getSupportFragmentManager().findFragmentByTag("SelectDateDialog"); // "tag" is the string set as the tag for the dialog when you show it
    			if (sd != null) {
    				// the dialog exists so update its listener
    				sd.setListener(this);
    			}
    		}
        	
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
		* The Fragment's UI is just a simple text view showing its
		* instance number.
		*/
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	View view = inflater.inflate(R.layout.fragment_graph_flow, container, false);
        	FilterButton = (Button) view.findViewById(R.id.button_filter);
        	FilterButton.setOnClickListener( new View.OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			 SelectDateDialogFragment dialog = SelectDateDialogFragment.newInstance();
    				 dialog.setListener((SelectDateDialog) GraphFlow.GraphFlowFragment.this);
    				 dialog.show(getSherlockActivity().getSupportFragmentManager(), "SelectDateDialog");
				}
			});
			
    	   return view;
        }
        
		public void onFilterClicked(View v) {
			
			 SelectDateDialogFragment dialog = SelectDateDialogFragment.newInstance();
			 dialog.setListener((SelectDateDialog) GraphFlow.GraphFlowFragment.this);
			 dialog.show(getSherlockActivity().getSupportFragmentManager(), "SelectDateDialog");
		}
		
		
		private void drawChart() {
			
		  	String[] titles = new String[] { "Sales growth January 1995 to December 2000" };
		    List<Date[]> dates = new ArrayList<Date[]>();
		    List<double[]> values = new ArrayList<double[]>();
		    Date[] dateValues = new Date[] { new Date(95, 0, 1), new Date(95, 3, 1), new Date(95, 6, 1),
		        new Date(95, 9, 1), new Date(96, 0, 1), new Date(96, 3, 1), new Date(96, 6, 1),
		        new Date(96, 9, 1), new Date(97, 0, 1), new Date(97, 3, 1), new Date(97, 6, 1),
		        new Date(97, 9, 1), new Date(98, 0, 1), new Date(98, 3, 1), new Date(98, 6, 1),
		        new Date(98, 9, 1), new Date(99, 0, 1), new Date(99, 3, 1), new Date(99, 6, 1),
		        new Date(99, 9, 1), new Date(100, 0, 1), new Date(100, 3, 1), new Date(100, 6, 1),
		        new Date(100, 9, 1), new Date(100, 11, 1) };
		    dates.add(dateValues);

		    values.add(new double[] { 4.9, 5.3, 3.2, 4.5, 6.5, 4.7, 5.8, 4.3, 4, 2.3, -0.5, -2.9, 3.2, 5.5,
		        4.6, 9.4, 4.3, 1.2, 0, 0.4, 4.5, 3.4, 4.5, 4.3, 4 });
		    int[] colors = new int[] { Color.BLUE };
		    PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
		    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    int length = colors.length;
		    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(colors[i]);
		      r.setPointStyle(styles[i]);
		      renderer.addSeriesRenderer(r);
		    }
		    
		    
		    renderer.setXTitle("Time");
		    renderer.setYTitle("Value");
		    renderer.setXAxisMin(dateValues[0].getTime());
		    renderer.setXAxisMax(dateValues[dateValues.length - 1].getTime());
		    renderer.setYAxisMin(-4);
		    renderer.setYAxisMax(11);
		    renderer.setAxesColor(Color.GRAY);
		    renderer.setLabelsColor(Color.LTGRAY);
		    renderer.setYLabels(10);
		
		    GraphicalView chartView;
		    
		    chartView = ChartFactory.getTimeChartView(getActivity() , buildDateDataset(titles, dates, values), renderer, "MMM yyyy");

			LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graph_flow_layout);	
			layout.removeAllViews();
			layout.addView(chartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
		}
		
	 /**
	   * Builds an XY multiple time dataset using the provided values.
	   * 
	   * @param titles the series titles
	   * @param xValues the values for the X axis
	   * @param yValues the values for the Y axis
	   * @return the XY multiple time dataset
	   */
	  protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues, List<double[]> yValues) {
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    int length = titles.length;
	    for (int i = 0; i < length; i++) {
	      TimeSeries series = new TimeSeries(titles[i]);
	      Date[] xV = xValues.get(i);
	      double[] yV = yValues.get(i);
	      int seriesLength = xV.length;
	      for (int k = 0; k < seriesLength; k++) {
	        series.add(xV[k], yV[k]);
	      }
	      dataset.addSeries(series);
	    }
	    return dataset;
	  }
	  
		public CategorySeries getDataSet() {
        	String select;
        	String[] selectArgs;
        	
        	String[] recordProjection = { DailyTables.TABLE_RECORDS_COLUMN_ID, DailyTables.TABLE_RECORDS_COLUMN_TITLE, DailyTables.TABLE_RECORDS_COLUMN_AMOUNT };
        	String[] categoryProjection = { DailyTables.TABLE_CATEGORIES_COLUMN_ID, DailyTables.TABLE_CATEGORIES_COLUMN_TITLE };
        	
        	CategorySeries series = new CategorySeries("Chart");
        	
        	ArrayList<StringValuePair> expensesValues = new ArrayList<StringValuePair>();
        	
        	// get categories
        	Cursor categoryCursor = getSherlockActivity().getContentResolver().query(DailyContentProvider.CATEGORIES_CONTENT_URI, categoryProjection, null, null, null);
        	for (boolean hasItem = categoryCursor.moveToFirst(); hasItem; hasItem = categoryCursor.moveToNext()) {
        		double expenses = 0;
        		String categoryTitle = categoryCursor.getString(categoryCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_TITLE));
        	    long categoryId = categoryCursor.getLong(categoryCursor.getColumnIndexOrThrow(DailyTables.TABLE_CATEGORIES_COLUMN_ID));
        	   
        		// manage expense Records
            	Map<String, String> selectionMap = Maps.newHashMap();
            	RecordFilter filter = new RecordFilter();
            	filter.reset();
            	selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_BOOKING_TYPE + "=?", "" + 1);
            	selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_CATEGORY_TYPE + "=?", "" + categoryId);
            	// if there is a filter, attach the arguments
            	if(mDateValuesFilter != null) {
	            	selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + ">=?", "" + mDateValuesFilter.getValue1());
	    			selectionMap.put(DailyTables.TABLE_RECORDS_COLUMN_UNIX_DATE + "<=?", ""+  mDateValuesFilter.getValue2());
            	}
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
                 expensesRecords.close();
            	 series.add(categoryTitle + ": " + expenses, expenses);
			}
			return series;
        }

		@Override
		public void onSelectDateDialogPositiveClick(ValuePair dates) {
			// set filter
			mDateValuesFilter = dates;
			drawChart();
		}

		@Override
		public void onSelectDateDialogDialogNeutralClick() {
			// invalid filter
			mDateValuesFilter = null;
			drawChart();
		}

		@Override
		public void onSelectDateDialogNegativeClick() {
			// TODO Auto-generated method stub
			
		}
    }
}

