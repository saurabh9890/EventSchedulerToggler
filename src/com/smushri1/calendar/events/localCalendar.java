package com.smushri1.calendar.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

public class localCalendar {

	static final String TAG = "localCalendar";
	static Cursor eventCursor, cursor;
	public static final String EVENT_ID = "_id";
	public static final String EVENT_DTSTART = "dtstart";
	public static final String EVENT_DTEND = "dtend";
	public static final String EVENT_DURATION = "duration";
	public static final String EVENT_RRULE = "rrule";
	public static final String EVENT_RDATE = "rdate";
	public static final String EVENT_TIMEZONE = "eventTimezone";
	public static final String EVENT_TITLE = "title";
	public static final String EVENT_ALL_DAY = "allDay";
	
	public static final String I_ID = "i_id";
	public static final String I_BEGIN = "begin";
	public static final String I_END = "end";
	public static final String I_END_DAY = "endDay";
	public static final String I_END_MINUTE = "endMinute";
	public static final String I_START_DAY = "startDay";
	public static final String I_START_MINUTE = "startMinute";
	
	public static ArrayList<CharSequence> mSelectedItems;
	static String title, repeat, until;
	
	@SuppressWarnings("unused")
	public static Cursor readCalendar(Context context) 
	{
		ContentResolver contentResolver = context.getContentResolver();

			// Fetch a list of all calendars synced with the device, their display names and whether the
			// user has them selected for display.
			
			 cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"),
					(new String[] { "_id", "name", "account_name", "account_type" }), null, null, null);

			
			if (cursor.moveToFirst()) {
			   do {
				   final String _id = cursor.getString(0);
				   final String displayName = cursor.getString(1);
				   final String acc_name = cursor.getString(2);
				   final String acc_type = cursor.getString(3);
				
				   Log.d(TAG, "Id: " + _id + " Display Name: " + displayName);
				   
			   }while (cursor.moveToNext());
			}
			
			return cursor;
		}
		
		
		
		
		@SuppressWarnings("unused")
		@SuppressLint("SimpleDateFormat")
		public static Cursor readEvents(Context context, int id){

			mSelectedItems = new ArrayList<CharSequence>();
			// For each calendar, display all the events from the previous week to the end of next week.		
			
			String calID = Integer.toString(id);
				ContentResolver contentResolver = context.getContentResolver();
				//Uri.Builder builder = Uri.parse("content://com.android.calendar/events").buildUpon();
				Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
				long now = new Date().getTime();
				ContentUris.appendId(builder, now);
				ContentUris.appendId(builder, now + DateUtils.YEAR_IN_MILLIS);
				
				
				eventCursor = contentResolver.query(builder.build(),
						new String[] { EVENT_ID, EVENT_TITLE, I_BEGIN, I_END, EVENT_ALL_DAY, EVENT_RRULE}, "calendar_id=" + calID,
						null, "startDay ASC, startMinute ASC"); 
				
				Log.d(TAG, "Event ID: " + calID);
				
				
				if (eventCursor != null && eventCursor.moveToFirst()) {
				   do {
					   final String eve_id = eventCursor.getString(0);
					   final String title = eventCursor.getString(1);
					   final Date begin = new Date(eventCursor.getLong(2));
					   final Date end = new Date(eventCursor.getLong(3));
					   final Boolean allDay = !eventCursor.getString(4).equals("0");
					   final String repeat = eventCursor.getString(5);
					
						 	
				   }while(eventCursor.moveToNext());
				   
				   return eventCursor;
				}
				else{
					Log.d(TAG, "EventCursor is empty");
					return eventCursor;
				}
		
		}		
}
