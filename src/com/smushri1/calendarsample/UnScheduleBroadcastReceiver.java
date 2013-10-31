package com.smushri1.calendarsample;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class UnScheduleBroadcastReceiver extends BroadcastReceiver{

	static String TAG = "UnScheduleBroadcastReceiver";
	final public static String ONE_TIME = "onetime";
	final public static String RINGER_MODE_ITEM = "ringer_mode_item"; 
	
	private static ArrayList<CharSequence> mItems;
	static int modeID;
	
	protected static final String ID_EXTRA = "com.smushri1.calendarsample.C_ID";
	public static final String DELETE_EVENT = "delete_event";
	//public static int deleteData = 0; // 0=NewData & 1=EditData
	
	static DataBean bdata;
	static StatusData statusData;
	static Bundle extra;
	static Intent intent_del;
	static Context context_del;
	static Cursor cursor;
	static int position;
	static String id;
	static int deleteData;
	static long rowID;
	
	
	@SuppressWarnings("static-access")
	@SuppressLint({ "Wakelock", "SimpleDateFormat" })
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d(TAG, "Welcome OnReceive of UnScheduleBroadcastReceiver!!!");
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PowerManager Acquires lock");
		wl.acquire();
		Log.d(TAG, "lock acquired");
			
		Bundle extras = intent.getExtras();
		modeID = extras.getInt(RINGER_MODE_ITEM);
		Log.d(TAG, "modeID: " + modeID);
		
		rowID = ToggleTabFragment.rowID;
		
		AudioManager mAudioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);	        		
		
		if(modeID == mAudioManager.RINGER_MODE_NORMAL)
		{
			//change to normal mode 
			Log.d(TAG, " Normal Mode");
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			Toast.makeText(context, "Normal Mode Activated",
					Toast.LENGTH_LONG).show();
		}
		else if(modeID == mAudioManager.RINGER_MODE_SILENT)
		{
			//change to silent mode   
			Log.d(TAG, " Silent Mode");
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			Toast.makeText(context, "Silent Mode Activated",
					Toast.LENGTH_LONG).show();
		}
		else if(modeID == mAudioManager.RINGER_MODE_VIBRATE)
		{
			Log.d(TAG, " Vibration Mode");
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			Toast.makeText(context, "Vibration Mode Activated", 
					Toast.LENGTH_LONG).show();
		}
		 
			String repeatEvent = GeneralTabFragment.bday_picker.getText().toString();
			GeneralTabFragment.bdata.setRepeatEvent(repeatEvent);
			Log.d("TAG", "Repeat day from POJO: " + GeneralTabFragment.bdata.getRepeatEvent());
				
			mItems = new ArrayList<CharSequence>();
			
			if(repeatEvent != null)
			{
				StringTokenizer token = new StringTokenizer(repeatEvent, " ");
				while(token.hasMoreElements()){
					mItems.add(token.nextToken());
				}
				Log.d("prevGeneralData", "Repeat day from mItems ArrayList: " + mItems);
			}
			
			setUnscheduleRinger(context,mItems);
			
			statusData = new StatusData(context);
			statusData.open();
			Log.d(TAG, "--> Deleting RowID: " + rowID);
		    statusData.delete(rowID);
		    updatelist();
	    	statusData.close();
	    	
			wl.release();
			Log.d(TAG, "lock released");
			
		
	}
	
	
	protected void updatelist() {
		cursor = statusData.query();
		MainActivity.adapter.changeCursor(cursor);
	}
	 	
	
	
	
	@SuppressWarnings("static-access")
	public static void setDeleteEvent(Context cont, int position)
	{
			statusData = new StatusData(cont);
      		statusData.open();
      		cursor = statusData.query();
    		cursor.moveToPosition(position);
    		Log.d(TAG, " Getting position = cursor data rowID:  " + cursor.getString(cursor.getColumnIndex(statusData.C_ID)));
    		Log.d(TAG, " Getting position = cursor data:  " + cursor.getString(cursor.getColumnIndex(statusData.C_EVENT_NAME)));
    		
    		id = cursor.getString(cursor.getColumnIndex(statusData.C_ID));
    		
    		String multiDay = cursor.getString(cursor.getColumnIndex(statusData.C_REPEAT_EVENT));
    		Log.d(TAG, " String multiDay from DB: " + multiDay);
    		
   		 	
    		rowID = Long.parseLong(id);
    		Log.d(TAG, "Long rowID: " + rowID);
   		 
    		
    		bdata = new DataBean();
    		bdata.setRepeatEvent(multiDay);
			Log.d("TAG", "Repeat day from POJO: " + bdata.getRepeatEvent());
				
			mItems = new ArrayList<CharSequence>();
			
			if(multiDay != null)
			{
				StringTokenizer token = new StringTokenizer(multiDay, " ");
				while(token.hasMoreElements()){
					mItems.add(token.nextToken());
				}
				Log.d("prevGeneralData", "Repeat day from mItems ArrayList: " + mItems);
			}
			
			setUnscheduleRinger(cont,mItems);
			statusData.close();
	}
	
	
	
	
	 public static void setUnscheduleRinger(Context context, ArrayList<CharSequence> mItems)
	 {
	    	AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
   
		// Monday
		if(mItems.contains("Monday"))
		{
			Log.d(TAG, " Ringer cancelling for Monday");
			Intent inM = new Intent(context, RingerBroadcastReceiver.class);
			inM.setData(Uri.parse("monday/" + rowID + "/start"));
			Log.d(TAG, "monday/" + rowID + "/start");
	     
			PendingIntent piM = PendingIntent.getBroadcast(context, 0, inM, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piM);
			Log.d(TAG, "cancelled repeatetive event 1 for monday..");
			
			
			// cancelling repeatetive event at end time.
			Intent inM_end = new Intent(context, RingerBroadcastReceiver.class);
			inM_end.setData(Uri.parse("monday/" + rowID + "/end"));
			Log.d(TAG, "monday/" + rowID + "/end");
	 
			PendingIntent piM_end = PendingIntent.getBroadcast(context, 0, inM_end, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piM_end);
			Log.d(TAG, "cancelled repeatetive event 2 for monday.");
		}
		
		
		// Tuesday
		if(mItems.contains("Tuesday"))
		{
			Intent inT = new Intent(context, RingerBroadcastReceiver.class);
			inT.setData(Uri.parse("tuesday/" + rowID + "/start"));
			Log.d(TAG, "tuesday/" + rowID + "/start");
	     
			PendingIntent piT = PendingIntent.getBroadcast(context, 0, inT, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piT);
			Log.d(TAG, "cancelled repeatetive event 1 for tuesday.");
			
			
			// cancelling repeatetive event at end time.
			Intent inT_end = new Intent(context, RingerBroadcastReceiver.class);
			inT_end.setData(Uri.parse("tuesday/" + rowID + "/end"));
			Log.d(TAG, "tuesday/" + rowID + "/end");
	 
			PendingIntent piT_end = PendingIntent.getBroadcast(context, 0, inT_end, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piT_end);
			Log.d(TAG, "cancelled repeatetive event 2 for tuesday.");
		}
		 
		// Wednesday
		if(mItems.contains("Wednesday"))
		{
			Intent inW = new Intent(context, RingerBroadcastReceiver.class);
			inW.setData(Uri.parse("wednesday/" + rowID + "/start"));
			Log.d(TAG, "wednesday/" + rowID + "/start");
	     
			PendingIntent piW = PendingIntent.getBroadcast(context, 0, inW, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piW);
			Log.d(TAG, "cancelled repeatetive event 1 for wednesday.");
	    
			
		// cancelling repeatetive event at end time.
			Intent inW_end = new Intent(context, RingerBroadcastReceiver.class);
			inW_end.setData(Uri.parse("wednesday/" + rowID + "/end"));
			Log.d(TAG, "wednesday/" + rowID + "/end");
	 
			PendingIntent piW_end = PendingIntent.getBroadcast(context, 0, inW_end, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piW_end);
			Log.d(TAG, "cancelled repeatetive event 2 for wednesday."); 
		}
		
		
		// Thursday
		if(mItems.contains("Thursday"))
		{
			Intent inTh = new Intent(context, RingerBroadcastReceiver.class);
			inTh.setData(Uri.parse("thursday/" + rowID + "/start"));
	    
			PendingIntent piTh = PendingIntent.getBroadcast(context, 0, inTh, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piTh);
			Log.d(TAG, "cancelled repeatetive event 1 for thrusday.");
	     
			
			// cancelling repeatetive event at end time.
			Intent inTh_end = new Intent(context, RingerBroadcastReceiver.class);
			inTh_end.setData(Uri.parse("thursday/" + rowID + "/end"));
	 
			PendingIntent piTh_end = PendingIntent.getBroadcast(context, 0, inTh_end, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piTh_end);
			Log.d(TAG, "cancelled repeatetive event 2 for thrusday.");         
		}
		
		
		// Friday
		if(mItems.contains("Friday"))
		{
			Intent inF = new Intent(context, RingerBroadcastReceiver.class);
			inF.setData(Uri.parse("friday/" + rowID + "/start"));	         
	     
			PendingIntent piF = PendingIntent.getBroadcast(context, 0, inF, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piF);
			Log.d(TAG, "cancelled repeatetive event 1 for Friday.");
	   
			
			// cancelling repeatetive event at end time.
			Intent inF_end = new Intent(context, RingerBroadcastReceiver.class);
			inF_end.setData(Uri.parse("friday/" + rowID + "/end"));
	  
			PendingIntent piF_end = PendingIntent.getBroadcast(context, 0, inF_end, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piF_end);
			Log.d(TAG, "cancelled repeatetive event 2 for Friday.");
	  }
		
		
		// Saturday
		if(mItems.contains("Saturday"))
		{		
			Intent inS = new Intent(context, RingerBroadcastReceiver.class);
			inS.setData(Uri.parse("saturday/" + rowID + "/start"));
	     
			PendingIntent piS = PendingIntent.getBroadcast(context, 0, inS, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piS);
			Log.d(TAG, "cancelled repeatetive event 1 for Saturday.");
	   
			
			// cancelling repeatetive event at end time.
			Intent inS_end = new Intent(context, RingerBroadcastReceiver.class);
			inS_end.setData(Uri.parse("saturday/" + rowID + "/end"));
	  
			PendingIntent piS_end = PendingIntent.getBroadcast(context, 0, inS_end, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piS_end);
			Log.d(TAG, "cancelled repeatetive event 2 for Saturday.");
		}
		
		
		// Sunday
		if(mItems.contains("Sunday"))
		{
			Intent inSu = new Intent(context, RingerBroadcastReceiver.class);
			inSu.setData(Uri.parse("sunday/" + rowID + "/start"));
	    
			PendingIntent piSu = PendingIntent.getBroadcast(context, 0, inSu, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piSu);
			Log.d(TAG, "cancelled repeatetive event 1 for Sunday."); 
	    
			
			// cancelling repeatetive event at end time.
			Intent inSu_end = new Intent(context, RingerBroadcastReceiver.class);
			inSu_end.setData(Uri.parse("sunday/" + rowID + "/end"));
	  
			PendingIntent piSu_end = PendingIntent.getBroadcast(context, 0, inSu_end, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(piSu_end);
			Log.d(TAG, "cancelled repeatetive event 2 for Sunday.");
		}

	 }	
	
}
