package com.smushri1.calendarsample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;

public class BootReceiver extends BroadcastReceiver {

	static final String TAG = "OnBootReceiver";
	
	static DataBean bdata;
	static StatusData statusData;
	static Cursor curs;
	static int mode;
	static String id;
	static AlarmManagerSender alarm;
	private static ArrayList<CharSequence> mItems;
	static Date d1 = null;
    static Date d2 = null;
    static Date d5 = null;
    long Startmiliseconds, endmiliseconds, start_end_eventmili;
    int media_vol,alarm_vol,wifi, blue, data;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
				
		alarm = new AlarmManagerSender();
		mItems = new ArrayList<CharSequence>();
	
		statusData = new StatusData(context);
		statusData.open();
		curs = statusData.query();
		
		 // looping through all rows and adding to list
	    if (curs.moveToFirst()) {
	        do {
	        	/* DataBEan class object */
	    		bdata = new DataBean();	
	    		
	    		getDbData(context, curs, bdata);

	    		// Schedular object : AlarmManagerBroadcastReceiver Object
	    		alarm.setRingerMode(context, alarm_vol, mode, media_vol, wifi, blue, data, endmiliseconds, mItems, d1, d2, d5);
	    		alarm.setOnetimeRinger(context, alarm_vol, mode, media_vol, wifi, blue, data, Startmiliseconds, start_end_eventmili);
	           
	        } while (curs.moveToNext());
	    }
	    
	    statusData.close();
   	}

	
	@SuppressWarnings({ "static-access", "unused" })
	private void getDbData(Context context, Cursor cursor, DataBean bdata) {

		id = cursor.getString(cursor.getColumnIndex(statusData.C_ID));
		String evName = cursor.getString(cursor.getColumnIndex(statusData.C_EVENT_NAME));		
		String startTime = cursor.getString(cursor.getColumnIndex(statusData.C_START_TIME));
		String endTime = cursor.getString(cursor.getColumnIndex(statusData.C_END_TIME));
		String fromDate = cursor.getString(cursor.getColumnIndex(statusData.C_FROM_DATE));
		String toDate = cursor.getString(cursor.getColumnIndex(statusData.C_TO_DATE));
		String multiDay = cursor.getString(cursor.getColumnIndex(statusData.C_REPEAT_EVENT));
		
		String ringer = cursor.getString(cursor.getColumnIndex(statusData.C_RINGER_MODE));
		 media_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_MEDIA_VOL));
		 alarm_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_ALARM_VOL));
		 wifi = cursor.getInt(cursor.getColumnIndex(statusData.C_WIFI_MODE));
		 blue = cursor.getInt(cursor.getColumnIndex(statusData.C_BLUETOOTH_MODE));
		 data = cursor.getInt(cursor.getColumnIndex(statusData.C_DATA_MODE));
		
		
		// Setting DataBean POJO object
		bdata.setStartTime(startTime);
		bdata.setEndTime(endTime);
		bdata.setFromDate(fromDate);
		bdata.setToDate(toDate);
		bdata.setRepeatEvent(multiDay);
		
		bdata.setRingerMode(ringer);
		bdata.setMediaVol(media_vol);
		bdata.setAlarmVol(alarm_vol);
		bdata.setWifi(wifi);
		bdata.setBluetooth(blue);
		bdata.setData(data);
		
		
		mItems.clear();
		if(multiDay != null)
		{
			StringTokenizer token = new StringTokenizer(multiDay, " ");
			while(token.hasMoreElements()){
				mItems.add(token.nextToken());
				}
		}
		
		String startEvent = new String(new StringBuilder().append(fromDate).append(" ").append(startTime));		
		String endEvent = new String(new StringBuilder().append(toDate).append(" ").append(endTime));
		String start_end_event = new String(new StringBuilder().append(fromDate).append(" ").append(endTime));
		 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault());
     	try {
			d1 = dateFormat.parse(startEvent);
			d5 = dateFormat.parse(start_end_event);
			d2 = dateFormat.parse(endEvent);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		 Startmiliseconds = d1.getTime();
         endmiliseconds = d2.getTime();
         start_end_eventmili = d5.getTime();
        
        AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
        if(ringer.equals("Normal"))
		{ 
			mode = mAudioManager.RINGER_MODE_NORMAL;
		}
		if(ringer.equals("Silent"))
		{ 
			mode = mAudioManager.RINGER_MODE_SILENT;
		}
		if(ringer.equals("Vibration"))
		{ 
			mode = mAudioManager.RINGER_MODE_VIBRATE;
		}
 		
	}
}
