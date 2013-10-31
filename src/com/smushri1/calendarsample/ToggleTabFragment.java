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
	
	static AlarmManagerSender alarm;
	static EditPrevData edited;
	static View rootView;
	static Context context;
	
	private static AudioManager mAudioManager;
	static String startEvent, endEvent;
	static AlertDialog alert_save;
	static StatusData statusData;
	static DataBean bdata;
	
	private static ToggleButton toggle_alarm, toggle_media, toggle_wifi, toggle_bluetooth, toggle_data;
	static Button badd_toggle, bset_alarm, bset_ringer, normal, silent, b_save, b_cancel;
	static int mode, currentVolume, alarmVol, wifiState, bluetoothState, dataState;
	static long rowID, hours, minutes, seconds, Startmiliseconds, endmiliseconds, start_end_eventmili;
	private static ArrayList<CharSequence> mItems;
	final static String[] ringer_items={"Normal","Silent","Vibration"};
	static Date d1 = null;
    static Date d2 = null;
    static Date d5 = null;
	
	
	public ToggleTabFragment() {
		Log.d(TAG, " ToggleTabFragment Constructor");
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
		alarm = new AlarmManagerSender();
		edited = new EditPrevData();
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

	
	@SuppressWarnings({ "unused" })
	@SuppressLint("SimpleDateFormat")
	private static long initPreviousGeneralData(){
		
		
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
		Startmiliseconds = initPreviousGeneralData();
		
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
			         
				}
			}else 
			  {		// one time event, no repeatation.
				if(System.currentTimeMillis() <= Startmiliseconds && Startmiliseconds < endmiliseconds)
				{
				  alarm.setOnetimeRinger(context, alarmVol, items, currentVolume, wifi, bluetooth, data, Startmiliseconds, start_end_eventmili);
					Toast.makeText(context, "OneTime Event is Set Successfully !!", Toast.LENGTH_SHORT).show();
				 
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
			         
			 	  }
			   }	
			
		  }else{
		      Toast.makeText(context, "Ringer mode is null", Toast.LENGTH_SHORT).show();
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
			
			alert_save = alert.create();
			alert_save.show();
		}
		else 
		{	
		  Log.d("showNextView <-->", " 1 ");
		  Startmiliseconds = initPreviousGeneralData();
			
		  if(System.currentTimeMillis() <= Startmiliseconds && Startmiliseconds < endmiliseconds)
		  {
			  statusData = new StatusData(getActivity()); 
			  Log.d("showNextView <-->", " Inner loop 1 ");
			  if(GeneralTabFragment.prevData == 0)
			  {
				  Log.d("showNextView <-->", " Inner loop 1.1 ");
				  try {
					   statusData.open();
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
				  Log.d("showNextView <-->", " Inner loop 1.2 ");
				  try {
				   		statusData.open();
				   		statusData.update(EditPrevData.id,GeneralTabFragment.bdata);
				   		Log.d("Data Updated into DB", " Hurrey....!!!! ");
			   			} catch (Exception e) {
			   				e.printStackTrace();
			   				}
			   			finally{
			   				statusData.close();
			   				}
		      	}
		
		   
			  startRinger(alarmVol, mode, currentVolume, wifiState, bluetoothState, dataState);  // setting alarms to do toggling action.	
			  getActivity().finish();
			  Log.d("OnNext Button", " MainActivity view showing registered Event List");
	      }
		  else{
			  Log.d("showNextView <-->", " 2  --> Please Enter Correct Date & Time.");
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
		  }
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
					
					switch (which) {
					case 0 : // Alarm
						toggle_alarm.setVisibility(ToggleButton.VISIBLE);
						toggle_alarm.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								AudioManager audio = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
								
								if(toggle_alarm.isChecked())
								{	
									alarmVol = audio.getStreamVolume(AudioManager.STREAM_ALARM);
									GeneralTabFragment.bdata.setAlarmVol(alarmVol);
								  
								}
							}
						});
						GeneralTabFragment.bdata.setAlarmVol(alarmVol);
						break;
		        	 	
					case 1 : // Ringer
						AlertDialog.Builder ringer = new AlertDialog.Builder(getActivity());
						ringer.setTitle(" Ringer Mode");
						ringer.setSingleChoiceItems(ringer_items, -1, new DialogInterface.OnClickListener() {
							
							@SuppressWarnings("static-access")
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								GeneralTabFragment.bdata.setRingerMode(ringer_items[which]);
								if(ringer_items[which] == "Normal")
								{
									mode = mAudioManager.RINGER_MODE_NORMAL;
								}
								if(ringer_items[which] == "Silent")
								{ 
									mode = mAudioManager.RINGER_MODE_SILENT;
								}
								if(ringer_items[which] == "Vibration")
								{ 
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
						toggle_media.setVisibility(ToggleButton.VISIBLE);
						toggle_media.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								AudioManager audio = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
								
								if(toggle_media.isChecked())
								{	
									currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
									GeneralTabFragment.bdata.setMediaVol(currentVolume);
								}
							}
						});
						GeneralTabFragment.bdata.setMediaVol(currentVolume);
						break;

					case 3 : // wi-fi
						toggle_wifi.setVisibility(ToggleButton.VISIBLE);
						toggle_wifi.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
								
								if(toggle_wifi.isChecked())
								{	
									//wifi.setWifiEnabled(false);
									wifiState = wifi.getWifiState();
									GeneralTabFragment.bdata.setWifi(wifiState);
								}
							}
						});
						GeneralTabFragment.bdata.setWifi(wifiState);
						break;
						
					case 4 : // bluetooth
						toggle_bluetooth.setVisibility(ToggleButton.VISIBLE);
						toggle_bluetooth.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
						
								if(toggle_bluetooth.isChecked())
								{	
									bluetoothState = blue.getState();
									GeneralTabFragment.bdata.setBluetooth(bluetoothState);
									
								}
							}
						});
						
						GeneralTabFragment.bdata.setBluetooth(bluetoothState);
						break;
						
					case 5 : // Data
						toggle_data.setVisibility(ToggleButton.VISIBLE);
						toggle_data.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
								
								if(toggle_data.isChecked())
								{	
									dataState = telephonyManager.getDataState();
									GeneralTabFragment.bdata.setData(dataState);
									
								}
							 }
						});
						GeneralTabFragment.bdata.setData(dataState);
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
