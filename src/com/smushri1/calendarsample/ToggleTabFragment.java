package com.smushri1.calendarsample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ToggleTabFragment extends Fragment{

	static final String DEBUG_TAG = "TogglesMenuDialogFragment";
	static final String TAG = "ToggleTabFragment";
	
	SectionFragmentPagerAdapter mtSectionsFragPagerAdapter;
	static AlarmManagerBroadcastReceiver alarm;
	static View rootView;
	static Context context;
	
	private static AudioManager mAudioManager;
	static String startEvent, endEvent;
	
	static Button badd_toggle, bset_alarm, bset_ringer, normal, silent, b_save, b_cancel;
	static StatusData statusData;
	static DataBean bdata;
	static long rowID;
	static long hours, minutes, seconds, Startmiliseconds, endmiliseconds, start_end_eventmili;
	private static ArrayList<CharSequence> mItems;
	final static String[] ringer_items={"Normal","Silent","Vibration"};
	static int mode;
	static int currentVolume, alarmVol, wifiState, bluetoothState, dataState;
	static String stMins, stHours, Date_start, Time_start;
	static String endMins, endHours, Date_end, Time_end;
	static int lh, lm, lh_end, lm_end;
	static int date_st, date_end; 
	static Date d1 = null;
    static Date d2 = null;
    static Date d5 = null;
	private static ToggleButton toggle_alarm, toggle_media, toggle_wifi, toggle_bluetooth, toggle_data;
	
	
	public ToggleTabFragment() {
		Log.d("DEBUG_TAG", " ToggleTabFragment Constructor");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d(DEBUG_TAG, "onCreateView()");
		
		rootView = inflater.inflate(R.layout.toggle_add_event, container,false);
		return rootView;
	}		
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		init();
      }


	public void init(){
		alarm = new AlarmManagerBroadcastReceiver();
		context = this.getActivity();
	
		mItems = new ArrayList<CharSequence>();
		
		toggle_alarm = (ToggleButton)rootView.findViewById(R.id.toggleButton_alarm);
		toggle_alarm.setVisibility(ToggleButton.INVISIBLE);
		
		toggle_media = (ToggleButton)rootView.findViewById(R.id.toggleButton_media);
		toggle_media.setVisibility(ToggleButton.INVISIBLE);
		
		
		toggle_wifi = (ToggleButton)rootView.findViewById(R.id.toggleButton_wifi);
		toggle_wifi.setVisibility(ToggleButton.INVISIBLE);
		
		toggle_bluetooth = (ToggleButton)rootView.findViewById(R.id.toggleButton_bluetooth);
		toggle_bluetooth.setVisibility(ToggleButton.INVISIBLE);
		
		toggle_data = (ToggleButton)rootView.findViewById(R.id.toggleButton_data);
		toggle_data.setVisibility(ToggleButton.INVISIBLE);
		
		
		badd_toggle = (Button)rootView.findViewById(R.id.button_add_toggle);
		badd_toggle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new TogglesMenuDialog().show(getActivity().getFragmentManager(), DEBUG_TAG);
			}
		});
		
		
		b_save = (Button)rootView.findViewById(R.id.button_save);
		b_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showNextView(v);
			}
		});
		
		
		b_cancel = (Button)rootView.findViewById(R.id.button_cancel);
		b_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCancelView(v);
			}
		});
		
	}

	
	@SuppressWarnings("unused")
	@SuppressLint("SimpleDateFormat")
	private static long prevGeneralData(){
		
		String startTime = GeneralTabFragment.bstart_time.getText().toString();
		String endTime = GeneralTabFragment.bend_time.getText().toString();
		String fromDate = GeneralTabFragment.bfrom_date.getText().toString();
		String toDate = GeneralTabFragment.bto_date.getText().toString();
		String repeatEvent = GeneralTabFragment.bday_picker.getText().toString();
		
		String ringer = GeneralTabFragment.bdata.getRingerMode();
		int media_vol = GeneralTabFragment.bdata.getMediaVol();
		int alarm_vol = GeneralTabFragment.bdata.getAlarmVol();
		int wifi = GeneralTabFragment.bdata.getWifi();
		int blue = GeneralTabFragment.bdata.getBluetooth();
		int data = GeneralTabFragment.bdata.getData();
		
		GeneralTabFragment.bdata.setStartTime(startTime);
		GeneralTabFragment.bdata.setEndTime(endTime);
		GeneralTabFragment.bdata.setFromDate(fromDate);
		GeneralTabFragment.bdata.setToDate(toDate);
		GeneralTabFragment.bdata.setRepeatEvent(repeatEvent);	
		
		mItems.clear();
		
		if(repeatEvent != null)
		{
			StringTokenizer token = new StringTokenizer(repeatEvent, " ");
			while(token.hasMoreElements()){
				mItems.add(token.nextToken());
				}
			Log.d("prevGeneralData", "Repeat day from mItems ArrayList: " + mItems);
		}
		
		startEvent = new String(new StringBuilder().append(fromDate).append(" ").append(startTime));		
        endEvent = new String(new StringBuilder().append(toDate).append(" ").append(endTime));
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
        
		return Startmiliseconds;
	}
	
	

	public static void startRinger(int alarmVol ,int items, int currentVolume, int wifi, int bluetooth, int data) {			
		Log.d(TAG, "OnstartRinger");
		Startmiliseconds = prevGeneralData();
		
		if(alarm != null)
		{
			if(GeneralTabFragment.isRepeatEvent == true)
			{
				// If repeat event
				Log.d("Saurabh", "if(multiDay != null) --> loop Entry");
				if(System.currentTimeMillis() <= Startmiliseconds && Startmiliseconds < endmiliseconds)
				{
					alarm.setRingerMode(context, alarmVol, items, currentVolume, wifi, bluetooth, data, endmiliseconds, mItems, d1,d2,d5); 
					Toast.makeText(context, "Repeat Event is Set Successfully !!", Toast.LENGTH_SHORT).show();
					
			//		return true;
				}else{
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("ERROR !!")
					.setMessage("Please Enter Correct Date & Time.")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CreateEventActivity.mViewPager.setCurrentItem(0);
							}
					});
					
					AlertDialog alert11 = alert.create();
			         alert11.show();
			         
			   //      return false;
				}
				
				Log.d("Saurabh", "if(multiDay != null) --> loop Exit");
				
			}else 
			  {		// one time event, no repeatation.
				
				if(System.currentTimeMillis() <= Startmiliseconds && Startmiliseconds < endmiliseconds)
				{
				  alarm.setOnetimeRinger(context, alarmVol, items, currentVolume, wifi, bluetooth, data, Startmiliseconds, start_end_eventmili);
					Toast.makeText(context, "OneTime Event is Set Successfully !!", Toast.LENGTH_SHORT).show();
				 
				//	return true;
				 }else{
					  AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("ERROR !!")
					.setMessage("Please Enter Correct Date & Time.")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CreateEventActivity.mViewPager.setCurrentItem(0);
							}
					});
					
					AlertDialog alert11 = alert.create();
			         alert11.show();
			         
			   //      return false;
				  }
			   }	
			
		  }else{
		      Toast.makeText(context, "Ringer mode is null", Toast.LENGTH_SHORT).show();
		   //   return false;
		     }		
	}
		
	
	public void showNextView(View v) 
	{	
		if(GeneralTabFragment.bdata.getRingerMode() == null)
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("ERROR !!")
			.setMessage("Please Select Ringer Toggling mode")
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					}
			});
			
			AlertDialog alert11 = alert.create();
	         alert11.show();
		}
	  else
	  {				
		   statusData = new StatusData(getActivity()); 
		
		   if(GeneralTabFragment.prevData == 0)
		   {
			  try {
				  	statusData.open();
					Log.d("before insert", " " + GeneralTabFragment.bdata);
					rowID = statusData.insert(GeneralTabFragment.bdata);
					Log.d("Data Inserted into DB", " Hurrey....!!!! Row ID: " + rowID);
				   } catch (Exception e) {
					e.printStackTrace();
				    }
			  		finally{
			  			statusData.close();
			  		}
		     }	
				
		   if(GeneralTabFragment.prevData == 1)
		    {
			   try {
				   		statusData.open();
				   		Log.d("Before Update", " " + GeneralTabFragment.bdata);
				   		statusData.update(GeneralTabFragment.id,GeneralTabFragment.bdata);
				   		Log.d("Data Updated into DB", " Hurrey....!!!! ");
			   		} catch (Exception e) {
			   			e.printStackTrace();
			   			}
			   		finally{
			   			statusData.close();
			   			}
		      }
		
		   
		startRinger(alarmVol, mode, currentVolume, wifiState, bluetoothState, dataState);  // setting Broadcast Receiver to do toggling action.	
		
		   Log.d("OnNext Button", " Next/Save clicked ");
		   getActivity().finish();
		   Log.d("OnNext Button", " MainActivity view showing registered Event List");
	  }
   }
	

	
	public void showCancelView(View v) 
	{		
		CreateEventActivity.mViewPager.setCurrentItem(0);
	}
	

	
	public static class TogglesMenuDialog extends DialogFragment 
	implements DialogInterface{
		

		@Override
		public Dialog onCreateDialog(Bundle state) {
				
	         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	         builder.setTitle("Toggle Item");				
	         builder.setItems(R.array.toggles_menu_dialog_array, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, final int which) {
					Log.d(DEBUG_TAG, " you selected " + which + " from list");
					switch (which) {
					case 0 : // Alarm
						toggle_alarm.setVisibility(ToggleButton.VISIBLE);
						toggle_alarm.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Log.d(TAG, "toggle_alarm OnClickListener");
								AudioManager audio = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
								
								if(toggle_alarm.isChecked())
								{	
									alarmVol = audio.getStreamVolume(AudioManager.STREAM_ALARM);
									Log.d(TAG, "The toggle_alarm state is changed to ON --> volume:  " + alarmVol);
									GeneralTabFragment.bdata.setAlarmVol(alarmVol);
									Log.d("Toggle_alarm vol Mode", " alarm vol mode return from POJO: " + GeneralTabFragment.bdata.getAlarmVol());
						        
								}
							}
						});
						GeneralTabFragment.bdata.setAlarmVol(alarmVol);
						break;
		        	 	
					case 1 : // Ringer
						Log.d("button_ringer", " Going in Ringer...Silent/vibrator/normal !!!");						
						AlertDialog.Builder ringer = new AlertDialog.Builder(getActivity());
						ringer.setTitle(" Ringer Mode");
						ringer.setSingleChoiceItems(ringer_items, -1, new DialogInterface.OnClickListener() {
							
							@SuppressWarnings("static-access")
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//checkIfPhoneIsSilent();
								GeneralTabFragment.bdata.setRingerMode(ringer_items[which]);
								Log.d("Radio-button_ringer Mode", " Ringer Mode return from POJO: " + GeneralTabFragment.bdata.getRingerMode());
					        	
								if(ringer_items[which] == "Normal")
								{ Log.d("mode", " First button: mode = 0 :Normal");
									mode = mAudioManager.RINGER_MODE_NORMAL;
								}
								if(ringer_items[which] == "Silent")
								{ Log.d("mode", " 2nd button: mode = 1 :Silent");
									mode = mAudioManager.RINGER_MODE_SILENT;
								}
								if(ringer_items[which] == "Vibration")
								{ Log.d("mode", " 3rd button: mode = 2 :Vibration ");
									mode = mAudioManager.RINGER_MODE_VIBRATE;
								}
				        	}
						})
						.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						
						ringer.create().show();
		        	 	break;
		        	 	
					case 2: // Media
						Log.d("ToggleButton_Media", " ToggleButton_Media !!!");
						toggle_media.setVisibility(ToggleButton.VISIBLE);
						toggle_media.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Log.d(TAG, "toggle_media OnClickListener");
								AudioManager audio = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
								
								if(toggle_media.isChecked())
								{	
									currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
									Log.d(TAG, "The toggle_media state is changed to ON --> volume:  " + currentVolume);
									GeneralTabFragment.bdata.setMediaVol(currentVolume);
									Log.d("Toggle_media vol Mode", " media vol mode return from POJO: " + GeneralTabFragment.bdata.getMediaVol());
								}
							}
						});
						GeneralTabFragment.bdata.setMediaVol(currentVolume);
						Log.d("Toggle_media vol Mode", " media vol mode return from POJO: " + GeneralTabFragment.bdata.getMediaVol());
						break;

					case 3 : // wi-fi
						Log.d("ToggleButton_wifi", " ToggleButton_wifi !!!");
						toggle_wifi.setVisibility(ToggleButton.VISIBLE);
						toggle_wifi.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Log.d(TAG, "toggle_wifi OnClickListener");
								WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
								
								if(toggle_wifi.isChecked())
								{	
									//wifi.setWifiEnabled(false);
									wifiState = wifi.getWifiState();
									Log.d(TAG, "The toggle_wifi state is changed");
									GeneralTabFragment.bdata.setWifi(wifiState);
									Log.d("Toggle_wifi Mode", " wifi Mode return from POJO: " + GeneralTabFragment.bdata.getWifi());
								}
							}
						});
						GeneralTabFragment.bdata.setWifi(wifiState);
						Log.d("Toggle_wifi vol Mode", " wifi Mode return from POJO: " + GeneralTabFragment.bdata.getWifi());
						
						break;
						
					case 4 : // bluetooth
						Log.d("ToggleButton_bluetooth", " ToggleButton_bluetooth !!!");
						toggle_bluetooth.setVisibility(ToggleButton.VISIBLE);
						toggle_bluetooth.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Log.d(TAG, "toggle_bluetooth OnClickListener");
								BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
						
								if(toggle_bluetooth.isChecked())
								{	
									bluetoothState = blue.getState();
									Log.d(TAG, "The ToggleButton_bluetooth state is changed");
									GeneralTabFragment.bdata.setBluetooth(bluetoothState);
									Log.d("ToggleButton_bluetooth Mode", " blueTooth mode return from POJO: " + GeneralTabFragment.bdata.getBluetooth());
						        	
								}
							}
						});
						
						GeneralTabFragment.bdata.setBluetooth(bluetoothState);
						Log.d("ToggleButton_bluetooth vol Mode", " media vol mode return from POJO: " + GeneralTabFragment.bdata.getBluetooth());
						break;
						
					case 5 : // Data
						Log.d("ToggleButton_data", " ToggleButton_data !!!");
						toggle_data.setVisibility(ToggleButton.VISIBLE);
						toggle_data.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Log.d(TAG, "ToggleButton_data OnClickListener");
								 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
								
								if(toggle_data.isChecked())
								{	
									dataState = telephonyManager.getDataState();
									Log.d(TAG, "The ToggleButton_data state is changed --> dataState:  " + dataState);
									GeneralTabFragment.bdata.setData(dataState);
									Log.d("ToggleButton_data vol Mode", " dataState return from POJO: " + GeneralTabFragment.bdata.getData());
						        	
								}
							 }
						});
						GeneralTabFragment.bdata.setData(dataState);
						Log.d("ToggleButton_data vol Mode", " Data mode return from POJO: " + GeneralTabFragment.bdata.getData());						
						break;
					}					
				}
			})	         
	         .setNegativeButton(R.string.cancel,  new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();				
					}
				});
	         	         
	      	return builder.create();
		}
		@Override
		public void cancel() {
			// TODO Auto-generated method stub
			
		}
	}		
}
