package com.smushri1.calendar.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.smushri1.calendarsample.R;

public class AdapterCalEvent extends CursorAdapter{

	static final String TAG = "AdapterCalEvent";
	
	static TextView event_name, begin_time, end_time, repeat_event, start_date, end_date;
	static String title, repeat, until, endTime, stTime;
	static Date begin, end;
	static boolean allDay;
	private static ArrayList<CharSequence> mSelectedItems;
	
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
		 repeat = cursor.getString(cursor.getColumnIndex(Events.RRULE));
		 
		Date endDate = null;
		String day = null;
		
		if(repeat != null) 
	    {
			String prefix = "UNTIL";
	    
			if((repeat.indexOf(prefix)) != -1)
			{ 
				int left = repeat.indexOf(prefix);
				String sub = repeat.substring(left+6, left+22);
				
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");         
				try {
					endDate = format.parse(sub);
				} catch (ParseException e) {
					Log.d(TAG, "Until Date is Not in correct form");
		    		e.printStackTrace();
				}
			}
			
			if((repeat.indexOf("FREQ")) != -1)
			{
				int l = repeat.indexOf("FREQ");
				String freq = repeat.substring(l+5, l+10);
				Log.d(TAG, "freq: "+ freq);
				mSelectedItems = new ArrayList<CharSequence>();
				
				if(freq.equals("DAILY"))
				{
					int d = repeat.indexOf("BYDAY");
					 day = repeat.substring(d+6);
					
					mSelectedItems.clear();
					if(day != null)
					{
						StringTokenizer token = new StringTokenizer(day,",");
						while(token.hasMoreElements())
						{
						  mSelectedItems.add(token.nextToken());
						}
					}	
					
				 }
				
				if(freq.equals("WEEKL"))
				{
					int d = repeat.indexOf("BYDAY");
					 day = repeat.substring(d+6);
				
					mSelectedItems.clear();
					if(day != null)
					{
						StringTokenizer token = new StringTokenizer(day,",");
						while(token.hasMoreElements())
						{
						  mSelectedItems.add(token.nextToken());
						}
					}	
					
				 }
				
				
				if(freq.equals("MONTH"))
				{
					int d = repeat.indexOf("BYDAY");
					 day = repeat.substring(d+6);
					
					mSelectedItems.clear();
					if(day != null)
					{
						StringTokenizer token = new StringTokenizer(day,",");
						while(token.hasMoreElements())
						{
						  mSelectedItems.add(token.nextToken());
						}
					}	
					
				 }
				
			 }
			
	     }
		 
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy zzz");
		String eve_begin = formatter.format(begin);
		String eve_end = null;
		
		if(endDate != null)
			eve_end = formatter.format(endDate);
	
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		stTime = sdf.format(begin);
		endTime = sdf.format(end);
		 	
		
		event_name = (TextView)view.findViewById(R.id.textView_event_name);
		event_name.setText(title);
		
		begin_time = (TextView)view.findViewById(R.id.textView_event_begin);
		begin_time.setText(stTime.toString());
		
		end_time = (TextView)view.findViewById(R.id.textView_event_end);
		end_time.setText(" - " + endTime.toString());
		
		start_date = (TextView)view.findViewById(R.id.textView_event_allDay);
		start_date.setText(eve_begin);
		
		end_date = (TextView)view.findViewById(R.id.textView_event_end_date);
		if(eve_end != null)
			end_date.setText(" Until " + eve_end);
		else
			end_date.setText(eve_end);
		
		repeat_event = (TextView)view.findViewById(R.id.textView_event_rrule);
		repeat_event.setText(day);
	}

	
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View v = mLayoutInflater.inflate(R.layout.row_event, parent, false);
		bindView(v, context, cursor);
        return v;
	}

}
