package sk.peterjurkovic.dril.fragments;

import sk.peterjurkovic.dril.R;
import sk.peterjurkovic.dril.db.BookDBAdapter;
import sk.peterjurkovic.dril.db.StatisticDbAdapter;
import sk.peterjurkovic.dril.utils.GoogleAnalyticsUtils;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GeneralStatisticsFragment extends Fragment{
	
	private Context context;

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_general_statistics_fragment, container, false);
		((TextView)view.findViewById(R.id.statisticGeneralWordsHeader)).setText(getString(R.string.header_stats_general));
		return view;
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context  = activity;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loeadData();
	}
	
	private void loeadData(){
		StatisticDbAdapter statisticDbAdapter = null;
		try{
			statisticDbAdapter = new StatisticDbAdapter(context);
			Cursor cursor = statisticDbAdapter.getGeneralStatistics();
			prepareUI(cursor);
		}catch(Exception e){
			 GoogleAnalyticsUtils.logException(e, context);
			 Log.e(this.getClass().getName(), e.getMessage());
		}finally{
			if(statisticDbAdapter != null){
				statisticDbAdapter.close();
			}
		}
	}

	
	private void prepareUI(Cursor cursor) {
		if(cursor == null){ 
			return;
		}
		if(cursor.moveToFirst()){
		
			final int bookCountIndex = cursor.getColumnIndex( BookDBAdapter.BOOK_COUNT );
			final int lectureCountIndex = cursor.getColumnIndex( BookDBAdapter.LECTURES_COUNT );
			final int wordCountIndex = cursor.getColumnIndex( BookDBAdapter.WORD_COUNT );
			final int activatedWordcountIndex = cursor.getColumnIndex( BookDBAdapter.ACTIVE_WORD_COUNT );
			final int avgRateIndex = cursor.getColumnIndex( BookDBAdapter.AVG_RATE );
			final int finishedIndex = cursor.getColumnIndex( StatisticDbAdapter.LEARNED_CARDS );
			
			View view = getView();
			((TextView)view.findViewById(R.id.tableCountOfBooks))
									.setText(cursor.getString(bookCountIndex));	
			((TextView)view.findViewById(R.id.tableCountOfLectures))
									.setText(cursor.getString(lectureCountIndex));	
			((TextView)view.findViewById(R.id.tableCountOfWords))
									.setText(cursor.getString(wordCountIndex));	
			((TextView)view.findViewById(R.id.tableCountOfActiveWords))
									.setText(cursor.getString(activatedWordcountIndex));
			((TextView)view.findViewById(R.id.tableRate))
									.setText(String.format( "%.2f", cursor.getDouble(avgRateIndex) )  );
			((TextView)view.findViewById(R.id.tableFinished))
									.setText(cursor.getString(finishedIndex));
			}
		
		cursor.close();
	}
	
	
}
