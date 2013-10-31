package com.smushri1.calendarsample;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class RingerBroadcastReceiver extends BroadcastReceiver{

	final static String TAG = "RingerBroadcastReceiver";
	final public static String ONE_TIME = "onetime";
	final public static String RINGER_MODE_ITEM = "ringer_mode_item"; 
	public static final String MEDIA_VOL = "media_vol";
	public static final String ALARM_VOL = "alarm_vol";
	public static final String INTENT_TYPE = "intent_type";
	public static final String ROW_ID = "row_id";
	public static final String WIFI_STATE = "wifi_state";
	public static final String BLUETOOTH_STATE = "bluetooth_state";
	public static final String DATA_STATE = "data_state";
	
	static int modeID, mediavol, alarmvol, wifiState, bluetoothState, dataState;
	static String intent_type;
	static long row_ID;
	static SharedPreferences pref;
	
	
	
	@SuppressWarnings("static-access")
	@SuppressLint({ "Wakelock", "SimpleDateFormat", "CommitPrefEdits" })
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d(TAG, "Welcome OnReceive !!!");
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PowerManager Acquires lock");
		wl.acquire();
		Log.d(TAG, "lock acquired");
		
		
		AudioManager mAudioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
		WifiManager wifi = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
		BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		pref = context.getSharedPreferences("EVENT", context.MODE_PRIVATE);				
		SharedPreferences.Editor edit = pref.edit();
		
	    Bundle extras = intent.getExtras();
	    modeID = extras.getInt(RINGER_MODE_ITEM);
	    mediavol = extras.getInt(MEDIA_VOL);
	    alarmvol = extras.getInt(ALARM_VOL);
	    wifiState = extras.getInt(WIFI_STATE);
	    bluetoothState = extras.getInt(BLUETOOTH_STATE);
	    dataState = extras.getInt(DATA_STATE);
	    intent_type = extras.getString(INTENT_TYPE);
	    row_ID = extras.getLong(ROW_ID);
	   
		Log.d(TAG, "modeID: " + modeID);
		Log.d(TAG, "mediavol: " + mediavol);
		Log.d(TAG, "alarmvol: " + alarmvol);
		Log.d(TAG, "wifi mode: " + wifiState);
		Log.d(TAG, "bluetoothState: " + bluetoothState);
		Log.d(TAG, "dataState: " + dataState);
		Log.d(TAG, "Intent_Type: " + intent_type);
		Log.d(TAG, "row_ID: " + row_ID);
		
		String rowid = String.valueOf(row_ID);
		
		if(intent_type.equals("start"))
		{
			
			 StringBuilder str = new StringBuilder();
			    str.append(mAudioManager.getRingerMode())
			    .append(":").append(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
			    .append(":").append(mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM))
			    .append(":").append(wifi.getWifiState())
			    .append(":").append(blue.getState())
			    .append(":").append(telephonyManager.getDataState());
			    
			 edit.putString(rowid, str.toString());
			 edit.commit();
			 Log.d(TAG, "StringBuilder str: " + str.toString());
				  
			 
			// Mode set Starts here
			
			RingerMode(context, modeID);
			
			 mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediavol, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE); 
        	 Log.d(TAG,"--> STREAM_MUSIC volume for START_Intent set successfully !!! <--");
        	 
        	 mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarmvol, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        	 Log.d(TAG,"--> STREAM_ALARM volume for START_Intent set successfully !!! <--");
        	 
        	 
        	 // wifi
        	 if(wifiState == wifi.WIFI_STATE_DISABLED)
        	 {	 wifi.setWifiEnabled(true);
        	 	Log.d(TAG,"--> WIFI_MODE ON for START_Intent!!! <--");
        	 }else{
            	 wifi.setWifiEnabled(false); 
            	 Log.d(TAG,"--> WIFI_MODE OFF for START_Intent!!! <--");
        	 }
        	 
      
        	 // Bluetooth
        	 if(bluetoothState == blue.STATE_OFF)
        	 {
        		 blue.enable();
        		 Log.d(TAG,"--> BLUETOOTH_MODE ON for START_Intent!!! <--");
        	 }else{
        		 blue.disable();
        		 Log.d(TAG,"--> BLUETOOTH_MODE OFF for START_Intent!!! <--");
        	 }
        	 
        	 
        	 // Data Mode
        	 	DataMode(context,dataState);
        	 	Log.d(TAG, "--> DATA_MODE for START_Intent!!! <--");
   		
		}
		else if(intent_type.equals("end"))
		{
			String savedString = pref.getString(rowid, "");
			StringTokenizer st = new StringTokenizer(savedString, ":");
			int[] savedList = new int[5];
			for (int i = 0; i < 5; i++) {
			    savedList[i] = Integer.parseInt(st.nextToken());
			    Log.d(TAG, "savedList: "+ i + ": " + savedList[i]);
			}
			
			
			// Mode set Starts here
			
			RingerMode(context, savedList[0]);
			
			 mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedList[1], AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE); 
        	 Log.d(TAG,"--> STREAM_MUSIC volume for END_Intent set successfully !!! <--");
        	 
        	 mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, savedList[2], AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        	 Log.d(TAG,"--> STREAM_ALARM volume for END_Intent set successfully !!! <--");
        	
        	 
        	 
        	 // wifi
        	 if(savedList[3] == wifi.WIFI_STATE_DISABLED)
        	 {	 wifi.setWifiEnabled(false);
        	 	Log.d(TAG,"--> WIFI_MODE ON for END_Intent!!! <--");
        	 }else{
            	 wifi.setWifiEnabled(true); 
            	 Log.d(TAG,"--> WIFI_MODE OFF for END_Intent!!! <--");
        	 }
        	 
        	 
        	// Bluetooth
        	 if(savedList[4] == blue.STATE_OFF)
        	 {
        		 blue.disable();
        		 Log.d(TAG,"--> BLUETOOTH_MODE OFF for END_Intent!!! <--");
        	 }else{
        		 blue.enable();
        		 Log.d(TAG,"--> BLUETOOTH_MODE ON for END_Intent!!! <--");
        	 }
        	 
        	 
        	 // Data Mode
     	 	DataMode(context,savedList[4]);
     	 	Log.d(TAG, "--> DATA_MODE for END_Intent!!! <--");
		}
        	 
		Log.d(TAG, "lock released");
		wl.release();		
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void DataMode(Context context, int dState) {		
	boolean isEnabled;
	
		 if(dState == TelephonyManager.DATA_CONNECTED)
    	 {
    		 isEnabled = false;
    	  }else{
    	        isEnabled = true;  
    	  }   
    	
		 
		 try {
			    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				    final Class conmanClass = Class.forName(conman.getClass().getName());
				    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
				    iConnectivityManagerField.setAccessible(true);
				    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
				    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
				    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
				    setMobileDataEnabledMethod.setAccessible(true);

				    setMobileDataEnabledMethod.invoke(iConnectivityManager, isEnabled);
				
	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	
	
	@SuppressWarnings("static-access")
	public void RingerMode(Context context, int mode){
		
		AudioManager mAudioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);	        		
		
		 if(mode == mAudioManager.RINGER_MODE_NORMAL)
			{
				//change to normal mode 
				Log.d(TAG, " Normal Mode");
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				Toast.makeText(context, "Normal Mode Activated",
	                    Toast.LENGTH_LONG).show();
			}
			else if(mode == mAudioManager.RINGER_MODE_SILENT)
			{
				//change to silent mode   
				Log.d(TAG, " Silent Mode");
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				Toast.makeText(context, "Silent Mode Activated",
	                    Toast.LENGTH_LONG).show();
			}
			else if(mode == mAudioManager.RINGER_MODE_VIBRATE)
			{
				Log.d(TAG, " Vibration Mode");
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	    		Toast.makeText(context, "Vibration Mode Activated", 
	           		 Toast.LENGTH_LONG).show();
			}     		
	}
	
}
