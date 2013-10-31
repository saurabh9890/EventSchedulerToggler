package com.smushri1.calendarsample;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;


public class AlarmManagerSender {

	static final String TAG = "AlarmManagerSender";
	
	public static final String ONE_TIME = "onetime";
	public static final String RINGER_MODE_ITEM = "ringer_mode_item";
	public static final String MEDIA_VOL = "media_vol";
	public static final String ALARM_VOL = "alarm_vol";
	public static final String INTENT_TYPE = "intent_type";
	public static final String ROW_ID = "row_id";
	public static final String WIFI_STATE = "wifi_state";
	public static final String BLUETOOTH_STATE = "bluetooth_state";
	public static final String DATA_STATE = "data_state";
	
	static long stSeconds, stMinutes, stHours;
	static long endSeconds, endMinutes, endHours;
	


	   public void setRingerMode(Context context,int alarmVol, int items, int volume, int wifiState, int bluetooth, int dataState, long endmili, ArrayList<CharSequence> mItems, Date d1, Date d2, Date d5) 	    
	    {	
	    	AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	    	AudioManager audi = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	    	
	    	// Monday
	    	if(mItems.contains("Monday"))
	    	{
	    	 Log.d(TAG, " Ringer setting for Monday");
	         	         
	         Intent inM = new Intent(context, RingerBroadcastReceiver.class);
	         inM.setData(Uri.parse("monday/" + ToggleTabFragment.rowID + "/start"));
	         inM.putExtra(ONE_TIME, Boolean.FALSE);
	         inM.putExtra(RINGER_MODE_ITEM, items);
	         inM.putExtra(MEDIA_VOL, volume);
	         inM.putExtra(ALARM_VOL, alarmVol);
	         inM.putExtra(WIFI_STATE, wifiState);
	         inM.putExtra(BLUETOOTH_STATE, bluetooth);
	         inM.putExtra(DATA_STATE, dataState);
	         inM.putExtra(INTENT_TYPE, "start");
	         inM.putExtra(ROW_ID, ToggleTabFragment.rowID);
	         Log.d(TAG, "monday/" + ToggleTabFragment.rowID + "/start");
	         
	         PendingIntent piM = PendingIntent.getBroadcast(context, 0, inM, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d1, Calendar.MONDAY), AlarmManager.INTERVAL_DAY * 7 , piM); 
	         
	         
	         // cancelling repeatetive event at end time.	           
	         Intent inM_end = new Intent(context, RingerBroadcastReceiver.class);
	         inM_end.setData(Uri.parse("monday/" + ToggleTabFragment.rowID + "/end"));
	         Log.d(TAG, "monday/" + ToggleTabFragment.rowID + "/end");
	         inM_end.putExtra(INTENT_TYPE, "end");
	         inM_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
	             	        
	         PendingIntent piM_end = PendingIntent.getBroadcast(context, 0, inM_end, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d5, Calendar.MONDAY), AlarmManager.INTERVAL_DAY * 7 , piM_end); 
		     
	 		}
	    	
	 	
	    	// Tuesday
	    	if(mItems.contains("Tuesday"))
	    	{
	    	 Log.d(TAG, " Ringer setting for Tuesday");
	           	         
	         Intent inT = new Intent(context, RingerBroadcastReceiver.class);
	         inT.setData(Uri.parse("tuesday/" + ToggleTabFragment.rowID + "/start"));
	         inT.putExtra(ONE_TIME, Boolean.FALSE);
	         inT.putExtra(RINGER_MODE_ITEM, items);
	         inT.putExtra(MEDIA_VOL, volume);
	         inT.putExtra(ALARM_VOL, alarmVol);
	         inT.putExtra(WIFI_STATE, wifiState);
	         inT.putExtra(BLUETOOTH_STATE, bluetooth);
	         inT.putExtra(DATA_STATE, dataState);
	         inT.putExtra(INTENT_TYPE, "start");
	         inT.putExtra(ROW_ID, ToggleTabFragment.rowID);
	         
	         Log.d(TAG, "tuesday/" + ToggleTabFragment.rowID + "/start");
	         
	         PendingIntent piT = PendingIntent.getBroadcast(context, 0, inT, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d1, Calendar.TUESDAY), AlarmManager.INTERVAL_DAY * 7 , piT); 
		     
	         
	         // cancelling repeatetive event at end time.    
	         Intent inT_end = new Intent(context, RingerBroadcastReceiver.class);
	         inT_end.setData(Uri.parse("tuesday/" + ToggleTabFragment.rowID + "/end"));
	         Log.d(TAG, "tuesday/" + ToggleTabFragment.rowID + "/end");
	         inT_end.putExtra(INTENT_TYPE, "end");
	         inT_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
	         
		        
	         PendingIntent piT_end = PendingIntent.getBroadcast(context, 0, inT_end, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d5, Calendar.TUESDAY), AlarmManager.INTERVAL_DAY * 7 , piT_end); 
		       
	 		}
	    	 
	    	// Wednesday
	    	if(mItems.contains("Wednesday"))
	    	{
	    	 Log.d(TAG, " Ringer setting for Wednesday");
	         Intent inW = new Intent(context, RingerBroadcastReceiver.class);
	         inW.setData(Uri.parse("wednesday/" + ToggleTabFragment.rowID + "/start"));
	         inW.putExtra(ONE_TIME, Boolean.FALSE);
	         inW.putExtra(RINGER_MODE_ITEM, items);
	         inW.putExtra(MEDIA_VOL, volume);
	         inW.putExtra(ALARM_VOL, alarmVol);
	         inW.putExtra(WIFI_STATE, wifiState);
	         inW.putExtra(BLUETOOTH_STATE, bluetooth);
	         inW.putExtra(DATA_STATE, dataState);
	         inW.putExtra(INTENT_TYPE, "start");
	         inW.putExtra(ROW_ID, ToggleTabFragment.rowID);
	        
	         Log.d(TAG, "wednesday/" + ToggleTabFragment.rowID + "/start");
	         
	         PendingIntent piW = PendingIntent.getBroadcast(context, 0, inW, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d1, Calendar.WEDNESDAY), AlarmManager.INTERVAL_DAY * 7 , piW); 
		     
	         
	         // cancelling repeatetive event at end time.      
	         Intent inW_end = new Intent(context, RingerBroadcastReceiver.class);
	         inW_end.setData(Uri.parse("wednesday/" + ToggleTabFragment.rowID + "/end"));
	         Log.d(TAG, "wednesday/" + ToggleTabFragment.rowID + "/end");
	         inW_end.putExtra(INTENT_TYPE, "end");
	         inW_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
		        
	         PendingIntent piW_end = PendingIntent.getBroadcast(context, 0, inW_end, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d5, Calendar.WEDNESDAY), AlarmManager.INTERVAL_DAY * 7 , piW_end); 
			}
	    	
	    	
	    	// Thursday
	    	if(mItems.contains("Thursday"))
	    	{
	    	 Log.d(TAG, " Ringer setting for Thursday");
	         Intent inTh = new Intent(context, RingerBroadcastReceiver.class);
	         inTh.setData(Uri.parse("thursday/" + ToggleTabFragment.rowID + "/start"));
	         inTh.putExtra(ONE_TIME, Boolean.FALSE);
	         inTh.putExtra(RINGER_MODE_ITEM, items);
	         inTh.putExtra(MEDIA_VOL, volume);
	         inTh.putExtra(ALARM_VOL, alarmVol);
	         inTh.putExtra(WIFI_STATE, wifiState);
	         inTh.putExtra(BLUETOOTH_STATE, bluetooth);
	         inTh.putExtra(DATA_STATE, dataState);
	         inTh.putExtra(INTENT_TYPE, "start");
	         inTh.putExtra(ROW_ID, ToggleTabFragment.rowID);
	        
	         PendingIntent piTh = PendingIntent.getBroadcast(context, 0, inTh, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d1, Calendar.THURSDAY), AlarmManager.INTERVAL_DAY * 7 , piTh); 	         
	         
	         // cancelling repeatetive event at end time.
	         Intent inTh_end = new Intent(context, RingerBroadcastReceiver.class);
	         inTh_end.setData(Uri.parse("thursday/" + ToggleTabFragment.rowID + "/end"));
	         inTh_end.putExtra(INTENT_TYPE, "end");
	         inTh_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
	         
	       PendingIntent piTh_end = PendingIntent.getBroadcast(context, 0, inTh_end, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d5, Calendar.THURSDAY), AlarmManager.INTERVAL_DAY * 7 , piTh_end); 
			}
	    	
	    	
	    	// Friday
	    	if(mItems.contains("Friday"))
	    	{
	    	 Log.d(TAG, " Ringer setting for Friday");
	         Intent inF = new Intent(context, RingerBroadcastReceiver.class);
	         inF.setData(Uri.parse("friday/" + ToggleTabFragment.rowID + "/start"));	         
	         inF.putExtra(ONE_TIME, Boolean.FALSE);
	         inF.putExtra(RINGER_MODE_ITEM, items);
	         inF.putExtra(MEDIA_VOL, volume);
	         inF.putExtra(ALARM_VOL, alarmVol);
	         inF.putExtra(WIFI_STATE, wifiState);
	         inF.putExtra(BLUETOOTH_STATE, bluetooth);
	         inF.putExtra(DATA_STATE, dataState);
	         inF.putExtra(INTENT_TYPE, "start");
	         inF.putExtra(ROW_ID, ToggleTabFragment.rowID);
	         
	         PendingIntent piF = PendingIntent.getBroadcast(context, 0, inF, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d1, Calendar.FRIDAY), AlarmManager.INTERVAL_DAY * 7 , piF); 
		     
	         
	         // cancelling repeatetive event at end time.
	         Intent inF_end = new Intent(context, RingerBroadcastReceiver.class);
	         inF_end.setData(Uri.parse("friday/" + ToggleTabFragment.rowID + "/end"));
	         inF_end.putExtra(INTENT_TYPE, "end");
	         inF_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
	 		    
	         PendingIntent piF_end = PendingIntent.getBroadcast(context, 0, inF_end, PendingIntent.FLAG_UPDATE_CURRENT);
	        am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d5, Calendar.FRIDAY), AlarmManager.INTERVAL_DAY * 7 , piF_end); 
			}
	    	
	    	
	    	// Saturday
	    	if(mItems.contains("Saturday"))
	    	{
	    	 Log.d(TAG, " Ringer setting for Saturday");
	         Intent inS = new Intent(context, RingerBroadcastReceiver.class);
	         inS.setData(Uri.parse("saturday/" + ToggleTabFragment.rowID + "/start"));
	         inS.putExtra(ONE_TIME, Boolean.FALSE);
	         inS.putExtra(RINGER_MODE_ITEM, items);
	         inS.putExtra(MEDIA_VOL, volume);
	         inS.putExtra(ALARM_VOL, alarmVol);
	         inS.putExtra(WIFI_STATE, wifiState);
	         inS.putExtra(BLUETOOTH_STATE, bluetooth);
	         inS.putExtra(DATA_STATE, dataState);
	         inS.putExtra(INTENT_TYPE, "start");
	         inS.putExtra(ROW_ID, ToggleTabFragment.rowID);
	         
	         PendingIntent piS = PendingIntent.getBroadcast(context, 0, inS, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d1, Calendar.SATURDAY), AlarmManager.INTERVAL_DAY * 7 , piS); 
		     
	         
	         // cancelling repeatetive event at end time.
	         Intent inS_end = new Intent(context, RingerBroadcastReceiver.class);
	         inS_end.setData(Uri.parse("saturday/" + ToggleTabFragment.rowID + "/end"));
	         inS_end.putExtra(INTENT_TYPE, "end");
	         inS_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
	          
			    
	         PendingIntent piS_end = PendingIntent.getBroadcast(context, 0, inS_end, PendingIntent.FLAG_UPDATE_CURRENT);
	          am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d5, Calendar.SATURDAY), AlarmManager.INTERVAL_DAY * 7 , piS_end); 
			}
	    	
	    	
	    	// Sunday
	    	if(mItems.contains("Sunday"))
	    	{
	    	 Log.d(TAG, " Ringer setting for Sunday");
	         Intent inSu = new Intent(context, RingerBroadcastReceiver.class);
	         inSu.setData(Uri.parse("sunday/" + ToggleTabFragment.rowID + "/start"));
	         inSu.putExtra(ONE_TIME, Boolean.FALSE);
	         inSu.putExtra(RINGER_MODE_ITEM, items);
	         inSu.putExtra(MEDIA_VOL, volume);
	         inSu.putExtra(ALARM_VOL, alarmVol);
	         inSu.putExtra(WIFI_STATE, wifiState);
	         inSu.putExtra(BLUETOOTH_STATE, bluetooth);
	         inSu.putExtra(DATA_STATE, dataState);
	         inSu.putExtra(INTENT_TYPE, "start");
	         inSu.putExtra(ROW_ID, ToggleTabFragment.rowID);
	        
	         PendingIntent piSu = PendingIntent.getBroadcast(context, 0, inSu, PendingIntent.FLAG_UPDATE_CURRENT);
	         am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d1, Calendar.SUNDAY), AlarmManager.INTERVAL_DAY * 7 , piSu); 
		
	         
	         // cancelling repeatetive event at end time.
	         Intent inSu_end = new Intent(context, RingerBroadcastReceiver.class);
	         inSu_end.setData(Uri.parse("sunday/" + ToggleTabFragment.rowID + "/end"));
	         inSu_end.putExtra(INTENT_TYPE, "end");
	         inSu_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
	            
	         PendingIntent piSu_end = PendingIntent.getBroadcast(context, 0, inSu_end, PendingIntent.FLAG_UPDATE_CURRENT);
	        am.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(d5, Calendar.SUNDAY), AlarmManager.INTERVAL_DAY * 7 , piSu_end); 
			}
	    	
	    	// Event End Pending Intent set for End Date & End Time
	    	 Intent intent_event_end = new Intent(context, UnScheduleBroadcastReceiver.class);
	    	 intent_event_end.setData(Uri.parse("event/" + ToggleTabFragment.rowID + "/end"));
	    	 intent_event_end.putExtra(RINGER_MODE_ITEM, audi.getRingerMode());
	    	 intent_event_end.putExtra(INTENT_TYPE, "end");
	    	 intent_event_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
	               
		     PendingIntent pi_event_end = PendingIntent.getBroadcast(context, 0, intent_event_end, PendingIntent.FLAG_UPDATE_CURRENT);
		     am.set(AlarmManager.RTC_WAKEUP, endmili, pi_event_end);	
		      Log.d(TAG, "Event End One Time pending intent is set");	    	
	    		    	
		}  // startRinger() big method end here
	   
	   
	   
	   
	    
	   public long getTriggerTime(Date dt, int day) 
	   {		   
		   Calendar cal = Calendar.getInstance();
		   cal.setTime(dt);

		   Log.d("getTriggerTime():", "User Date in millis: " + dt.getTime());
		   if (cal.get(Calendar.DAY_OF_WEEK) == day) 
		   {
			   return dt.getTime();
		   } 

		   	while(true) 
		   	{
		   		cal.add(Calendar.DAY_OF_WEEK, 1);		  
		   		if (cal.get(Calendar.DAY_OF_WEEK) == day)
		   			break;
		   	}
		   	Log.d("getTriggerTime():", " getTrigger Time in milis: "+ cal.getTimeInMillis());
		   	return cal.getTimeInMillis();
		}


	 
	   
	    
	    public void setOnetimeRinger(Context context,int alarmVol, int items, int volume, int wifiState, int bluetooth, int dataState, long d1_mili, long d5_mili){
	    	 Log.d(TAG, " One Time Ringer setting");
		     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		     
		        Intent intent = new Intent(context, RingerBroadcastReceiver.class);
		        intent.setData(Uri.parse("onetime/" + ToggleTabFragment.rowID + "/start"));
		        Log.d("OneTimer", "onetime/" + ToggleTabFragment.rowID + "/start");
		        intent.putExtra(ONE_TIME, Boolean.TRUE);
		        intent.putExtra(RINGER_MODE_ITEM, items);
		        intent.putExtra(MEDIA_VOL, volume);
		        intent.putExtra(ALARM_VOL, alarmVol);
		        intent.putExtra(WIFI_STATE, wifiState);
		        intent.putExtra(BLUETOOTH_STATE, bluetooth);
		        intent.putExtra(DATA_STATE, dataState);
		        intent.putExtra(INTENT_TYPE, "start");
		        intent.putExtra(ROW_ID, ToggleTabFragment.rowID);
		       
		        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        am.set(AlarmManager.RTC_WAKEUP, d1_mili, pi);
		        Log.d(TAG, "One Time Event is set");
		        
		       // onetime ringer end set  
		        Intent intent_end = new Intent(context, RingerBroadcastReceiver.class);
		        intent_end.setData(Uri.parse("onetime/" + ToggleTabFragment.rowID + "/end"));
		        intent_end.putExtra(INTENT_TYPE, "end");
		        intent_end.putExtra(ROW_ID, ToggleTabFragment.rowID);
		           
		        PendingIntent pi_end = PendingIntent.getBroadcast(context, 0, intent_end, PendingIntent.FLAG_UPDATE_CURRENT);
		        am.set(AlarmManager.RTC_WAKEUP, d5_mili, pi_end);	
		        Log.d(TAG, "One Time Event is set to be end");		        
		    }	
	    
}
