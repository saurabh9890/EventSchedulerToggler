package com.smushri1.calendar.events;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.smushri1.calendarsample.R;

public class AdapterCalEvent extends CursorAdapter{

	static final String TAG = "AdapterCalEvent";
	
	TextView event_name, begin_time, end_time, repeat_event, start_date;
	static String title, repeat;
	static Date begin, end;
	static boolean allDay;
	
	private LayoutInflater mLayoutInflater;
 	@SuppressWarnings("unused")
	private Context mContext;
  
	
	public AdapterCalEvent(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);

		mContext = context;
		mLayoutInflater = LayoutInflater.from(context); 
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		 title = cursor.getString(1);
		 begin = new Date(cursor.getLong(2));
		 end = new Date(cursor.getLong(3));
		 allDay = !cursor.getString(4).equals("0");
		 repeat = cursor.getString(5);
					 
		 
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy zzz");
		String eve_begin = formatter.format(begin);
		String eve_end = formatter.format(end);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		String stTime = sdf.format(begin);
		String endTime = sdf.format(end);
		 	
		
		event_name = (TextView)view.findViewById(R.id.textView_event_name);
		event_name.setText(title);
		
		begin_time = (TextView)view.findViewById(R.id.textView_event_begin);
		begin_time.setText(stTime.toString());
		
		end_time = (TextView)view.findViewById(R.id.textView_event_end);
		end_time.setText("- " + endTime.toString());
		
		start_date = (TextView)view.findViewById(R.id.textView_event_allDay);
		start_date.setText(eve_begin);
		
		
		repeat_event = (TextView)view.findViewById(R.id.textView_event_rrule);
		repeat_event.setText(repeat);
	}

	
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View v = mLayoutInflater.inflate(R.layout.row_event, parent, false);
		bindView(v, context, cursor);
        return v;
	}

}
