package com.smushri1.calendar.events;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class CalDataBase {

	private static final String TAG = " Cal_DataBase";
	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "calendar.db";
	public static final String TABLE_CALENDARS = "table_calendars";
	public static final String TABLE_EVENTS = "table_events";
	public static final String TABLE_INSTANCES = "table_instances";
	
	public static final String CAL_ID = "_id";
	public static final String CAL_NAME = "name";
	public static final String CAL_CALENDAR_DISPLAY_NAME = "calendar_displayName";
	public static final String CAL_VISIBLE = "visible";
	public static final String CAL_SYNC_EVENTS = "sync_events";
	
	public static final String EVENT_ID = "event_id";
	public static final String EVENT_DTSTART = "dtstart";
	public static final String EVENT_DTEND = "dtend";
	public static final String EVENT_DURATION = "duration";
	public static final String EVENT_RRULE = "rrule";
	public static final String EVENT_RDATE = "rdate";
	public static final String EVENT_TIMEZONE = "eventTimezone";
	public static final String EVENT_TITLE = "title";
	public static final String EVENT_DESCRIPTION = "description";
	public static final String EVENT_ALL_DAY = "allDay";

	public static final String I_ID = "i_id";
	public static final String I_BEGIN = "begin";
	public static final String I_END = "end";
	public static final String I_END_DAY = "endDay";
	public static final String I_END_MINUTE = "endMinute";
	public static final String I_START_DAY = "startDay";
	public static final String I_START_MINUTE = "startMinute";
	
	
	Context context;
	CalDbHelper caldbHelper;
	SQLiteDatabase db;
	Cursor cursor;
	//DataBean bean;
	
	
	public CalDataBase(Context context){
		 this.context = context;
		  caldbHelper = new CalDbHelper();
	}
	
	
	
	
	class CalDbHelper extends SQLiteOpenHelper{

		public CalDbHelper() {
			super(context, DB_NAME, null, DB_VERSION);
			
		//	 DB_PATH = context.getDatabasePath(DB_NAME).getPath();
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			String calendar_sql = String.format("create table %s" + 
					"(%s long primary key AUTOINCREMENT, %s text, %s text, %s int, %s int)", 
					TABLE_CALENDARS, CAL_ID, CAL_NAME, CAL_CALENDAR_DISPLAY_NAME, CAL_VISIBLE, CAL_SYNC_EVENTS);
			Log.d(TAG, "OnCreate with CALENDAR_SQL: "+ calendar_sql);
			
			
			String events_sql = String.format("create table %s" + 
					"(%s long primary key AUTOINCREMENT, %s long foreign key, %s long, %s long, %s text, %s text, %s text, %s text, %s text, %s text, %s int)", 
					TABLE_EVENTS, EVENT_ID , CAL_ID, EVENT_DTSTART, EVENT_DTEND, EVENT_DURATION, EVENT_RRULE, EVENT_RDATE, EVENT_TIMEZONE, EVENT_TITLE, EVENT_DESCRIPTION, EVENT_ALL_DAY);
			Log.d(TAG, "OnCreate with EVENTS_SQL: "+ events_sql);
		
			
			String instances_sql = String.format("create table %s" + 
					"(%s long primary key AUTOINCREMENT, %s long foreign key, %s long, %s long, %s int, %s int, %s int, %s int)", 
					TABLE_INSTANCES, I_ID, EVENT_ID , I_BEGIN, I_END, I_END_DAY, I_END_MINUTE, I_START_DAY, I_START_MINUTE);
			Log.d(TAG, "OnCreate with INSTANCES_SQL: "+ instances_sql);	
			
			
			db.execSQL(calendar_sql);
			db.execSQL(events_sql);
			db.execSQL(instances_sql);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// ALTER TABLE statement
			db.execSQL("drop table "+ TABLE_CALENDARS);
			db.execSQL("drop table "+ TABLE_EVENTS);
			db.execSQL("drop table "+ TABLE_INSTANCES);
			onCreate(db);			
		}	
	}
	
}
