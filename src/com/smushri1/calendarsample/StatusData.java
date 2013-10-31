package com.smushri1.calendarsample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatusData {
	private static final String TAG = " Calender StatusData";
	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "calendar.db";
	public static final String TABLE = "event";
	
	public static String C_ID = "_id";
	public static final String C_EVENT_NAME = "_name";
	public static final String C_START_TIME = "started_at";
	public static final String C_END_TIME = "ended_at";
	public static final String C_FROM_DATE = "from_date";
	public static final String C_TO_DATE = "to_date";
	public static final String C_REPEAT_EVENT = "repeat_event";
	public static final String C_RINGER_MODE = "ringer_mode";
	public static final String C_MEDIA_VOL = "media_vol";
	public static final String C_ALARM_VOL = "alarm_vol";
	
	public static final String C_WIFI_MODE = "wifi_mode";
	public static final String C_BLUETOOTH_MODE = "bluetooth_mode";
	public static final String C_DATA_MODE = "data_mode";
	public static final String C_USER = "user_name";
	
	Context context;
	DbHelper dbHelper;
	SQLiteDatabase db;
	Cursor cursor;
	DataBean bean;
	
	
	public StatusData(Context context){
		 this.context = context;
		  dbHelper = new DbHelper();
	}

	public long insert(DataBean bean){		
		ContentValues values = new ContentValues();
		values.put(C_EVENT_NAME, bean.getEventName());
		values.put(C_START_TIME, bean.getStartTime());
		values.put(C_END_TIME, bean.getEndTime());
		values.put(C_FROM_DATE, bean.getFromDate());
		values.put(C_TO_DATE, bean.getToDate());
		values.put(C_REPEAT_EVENT, bean.getRepeatEvent());
		values.put(C_RINGER_MODE, bean.getRingerMode());
		values.put(C_MEDIA_VOL, bean.getMediaVol());
		values.put(C_ALARM_VOL, bean.getAlarmVol());
		values.put(C_WIFI_MODE, bean.getWifi());
		values.put(C_BLUETOOTH_MODE, bean.getBluetooth());
		values.put(C_DATA_MODE, bean.getData());
		
		db = dbHelper.getWritableDatabase();
		long rowID = db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		
		return rowID;
	}
	
	
	 public Cursor query(){		  
		  db = dbHelper.getReadableDatabase();		  
		  cursor = db.query(TABLE, null, null, null, null, null, C_START_TIME + " ASC");
	
		  return cursor;
	  }
	
	 
	public void update(String id, DataBean bean){		
		Log.d(TAG, "Query Updation....!!!");
		
		ContentValues values = new ContentValues();
		values.put(C_EVENT_NAME, bean.getEventName());
		values.put(C_START_TIME, bean.getStartTime());
		values.put(C_END_TIME, bean.getEndTime());
		values.put(C_FROM_DATE, bean.getFromDate());
		values.put(C_TO_DATE, bean.getToDate());
		values.put(C_REPEAT_EVENT, bean.getRepeatEvent());
		values.put(C_RINGER_MODE, bean.getRingerMode());
		values.put(C_MEDIA_VOL, bean.getMediaVol());
		values.put(C_ALARM_VOL, bean.getAlarmVol());
		values.put(C_WIFI_MODE, bean.getWifi());
		values.put(C_BLUETOOTH_MODE, bean.getBluetooth());
		values.put(C_DATA_MODE, bean.getData());
		
		db = dbHelper.getWritableDatabase();
		db.updateWithOnConflict(TABLE, values, C_ID + "=" + id, null, SQLiteDatabase.CONFLICT_IGNORE);
		
		Log.d(TAG, "Query Updated!!!");
	}
	
	
	
	
	// long id
	public void delete(long id){
		Log.d(TAG, "Query Deleting row id : " + id);
		
		db = dbHelper.getWritableDatabase();
		db.delete(TABLE, C_ID + "=" + id , null);
	
		Log.d(TAG, "Query Deleted!!!");
	}
	
	
	
	public void open() throws SQLException {
	    db = dbHelper.getWritableDatabase();
	  }

	
	  public void close() {
	    dbHelper.close();
	  }

	
	class DbHelper extends SQLiteOpenHelper{

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = String.format("create table %s" + 
					"(%s INTEGER primary key AUTOINCREMENT, %s text, %s text, %s text, %s text, %s text, %s text, %s text, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER)", 
					TABLE, C_ID , C_EVENT_NAME, C_START_TIME, C_END_TIME, C_FROM_DATE, C_TO_DATE, C_REPEAT_EVENT, C_RINGER_MODE, C_MEDIA_VOL, C_ALARM_VOL, C_WIFI_MODE, C_BLUETOOTH_MODE, C_DATA_MODE);
			
		
			Log.d(TAG, "OnCreate with SQL: "+ sql);			
			db.execSQL(sql);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// ALTER TABLE statement
			db.execSQL("drop table "+ TABLE);
			onCreate(db);			
		}	
	}
}
