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
import com.daily.expenses.util.GraphsHelper;
import com.daily.expenses.util.Maps;
import com.daily.expenses.util.RecordFilter;
import com.daily.expenses.util.StringValuePair;
import com.daily.expenses.util.ValuePair;

public class GraphCategories extends SherlockFragmentActivity {
	GraphCategoriesFragment mCategoriesFragment;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_graph_categories);
        

        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            mCategoriesFragment = GraphCategoriesFragment.newInstance(1);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_graph_categories, mCategoriesFragment).commit();
        } else {
            //mStackLevel = savedInstanceState.getInt("level");
        }
    }
	
	
	
	public static class GraphCategoriesFragment extends SherlockFragment implements SelectDateDialogFragment.SelectDateDialog {
	int mNum;
	Button FilterButton;
	private ValuePair mDateValuesFilter = null;

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
        	View view = inflater.inflate(R.layout.fragment_graph_categories, container, false);
        	FilterButton = (Button) view.findViewById(R.id.button_filter);
        	FilterButton.setOnClickListener( new View.OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			 SelectDateDialogFragment dialog = SelectDateDialogFragment.newInstance();
    				 dialog.setListener((SelectDateDialog) GraphCategories.GraphCategoriesFragment.this);
    				 dialog.show(getSherlockActivity().getSupportFragmentManager(), "SelectDateDialog");
				}
			});
			
    	   return view;
        }
        
		public void onFilterClicked(View v) {
			
			 SelectDateDialogFragment dialog = SelectDateDialogFragment.newInstance();
			 dialog.setListener((SelectDateDialog) GraphCategories.GraphCategoriesFragment.this);
			 dialog.show(getSherlockActivity().getSupportFragmentManager(), "SelectDateDialog");
		}
		
		
		private void drawChart() {
			DefaultRenderer render = new DefaultRenderer();
        	
        	CategorySeries incomeSeries = getDataSet();
        	
        	for (int i = 0; i < incomeSeries.getItemCount(); i++) {
        		SimpleSeriesRenderer incomeSeriesRenderer = new SimpleSeriesRenderer();
        		incomeSeriesRenderer.setColor( GraphsHelper.getColorCode( i ) );
        		render.addSeriesRenderer(incomeSeriesRenderer);
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
    		render.setZoomButtonsVisible(true);
    		
		   GraphicalView chartView;
    		  
		   chartView = ChartFactory.getPieChartView(getActivity(), getDataSet(), render);

			LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.dashboard_chart_layout);	
			layout.removeAllViews();
			layout.addView(chartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
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
