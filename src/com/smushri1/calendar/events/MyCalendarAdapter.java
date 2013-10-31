package com.smushri1.calendar.events;

import java.util.Date;

import com.smushri1.calendarsample.R;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class MyCalendarAdapter extends CursorAdapter{

	static final String TAG = "MyCalendarAdapter";
	
	ImageView alarmView, bluetoothView, wifiView, mediaView, dataView, ringerView;
	TextView event_name, start_time, end_time, repeat_event;
	
	private LayoutInflater mLayoutInflater;
 	private Context mContext;
  
	
	public MyCalendarAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);

		mContext = context;
		mLayoutInflater = LayoutInflater.from(context); 
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		/*if (cursor.moveToFirst()) {
			   do {
				   String title = cursor.getString(0);
					 Date begin = new Date(cursor.getLong(1));
					 Date end = new Date(cursor.getLong(2));
					 Boolean allDay = !cursor.getString(3).equals("0");
					 
				   Log.d(TAG, "Title: " + title + " Begin: " + begin + " End: " + end +
						" All Day: " + allDay);
			}while(cursor.moveToNext());
		  }	  */ 
		
		String title = cursor.getString(0);
		 Date begin = new Date(cursor.getLong(1));
		 Date end = new Date(cursor.getLong(2));
		 Boolean allDay = !cursor.getString(3).equals("0");
		 
	   Log.d(TAG, "Title: " + title + " Begin: " + begin + " End: " + end +
			" All Day: " + allDay);
		
		event_name = (TextView)view.findViewById(R.id.text_event_name);
		event_name.setText(title);
		
		start_time = (TextView)view.findViewById(R.id.text_start_time);
		start_time.setText((CharSequence) begin);
		
		end_time = (TextView)view.findViewById(R.id.text_end_time);
		end_time.setText((CharSequence) end);
		
		/*repeat_event = (TextView)view.findViewById(R.id.text_repeat_event);
		repeat_event.setText(allDay);
		*/
		
			Log.d("Sau", "ImageView Set <--> Nw Adapter initialising");		
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View v = mLayoutInflater.inflate(R.layout.row, parent, false);
		bindView(v, context, cursor);
        return v;
	}

}
