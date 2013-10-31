package com.smushri1.calendarsample;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class MyAdapter extends CursorAdapter{

	static final String TAG = "MyAdapter";
	
	ImageView alarmView, bluetoothView, wifiView, mediaView, dataView, ringerView;
	TextView event_name, start_time, end_time, repeat_event;
	
	private LayoutInflater mLayoutInflater;
 	private Context mContext;
    static StatusData statusData;
	
	
	public MyAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);

		mContext = context;
		mLayoutInflater = LayoutInflater.from(context); 
	}

	@SuppressWarnings("static-access")
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		statusData = new StatusData(context);
		

	//	if(isData == true)
		{	
			
			String eveName = cursor.getString(cursor.getColumnIndex(statusData.C_EVENT_NAME));
			String stTime = cursor.getString(cursor.getColumnIndex(statusData.C_START_TIME));
			String endTime = cursor.getString(cursor.getColumnIndex(statusData.C_END_TIME));
			String multiDay = cursor.getString(cursor.getColumnIndex(statusData.C_REPEAT_EVENT));
			
			String ringer = cursor.getString(cursor.getColumnIndex(statusData.C_RINGER_MODE));
			int media_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_MEDIA_VOL));
			int alarm_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_ALARM_VOL));
			int wifi = cursor.getInt(cursor.getColumnIndex(statusData.C_WIFI_MODE));
			int blue = cursor.getInt(cursor.getColumnIndex(statusData.C_BLUETOOTH_MODE));
			int data = cursor.getInt(cursor.getColumnIndex(statusData.C_DATA_MODE));
			
			
			
			event_name = (TextView)view.findViewById(R.id.text_event_name);
			event_name.setText(eveName);
			
			start_time = (TextView)view.findViewById(R.id.text_start_time);
			start_time.setText(stTime);
			
			end_time = (TextView)view.findViewById(R.id.text_end_time);
			end_time.setText(endTime);
			
			repeat_event = (TextView)view.findViewById(R.id.text_repeat_event);
			repeat_event.setText(multiDay);
			
			
			Log.d("Sau", "Initialising ImageView --> ");
			ringerView = (ImageView) view.findViewById(R.id.imageView_ringer);
			alarmView = (ImageView) view.findViewById(R.id.imageView_alarm);
			bluetoothView = (ImageView) view.findViewById(R.id.imageView_bluetooth);
			wifiView = (ImageView) view.findViewById(R.id.imageView_wifi);
			dataView = (ImageView) view.findViewById(R.id.imageView_data);
			mediaView = (ImageView) view.findViewById(R.id.imageView_media);
			
		
			Log.d("Sau", "alarm_vol --> " + alarm_vol);
			Log.d("Sau", "media_vol --> " + media_vol);
			Log.d("Sau", "wifi --> " + wifi);
			Log.d("Sau", "blue --> " + blue);
			Log.d("Sau", "data --> " + data);
			Log.d("Sau", "Ringer --> " + ringer);
		
			
			// Data Usage setImage
			if(data == 2) // data connected = 2
			{	
				dataView.setImageResource(R.drawable.ic_action_network_cell);
			}else{
				dataView.setImageResource(R.drawable.ic_action_network_cell_dark);
			}
			
			
			// Bluetooth setImage
			if(blue == 0) // bluetooth disabled = 0
			{
				bluetoothView.setImageResource(R.drawable.ic_action_bluetooth_dark);
			}else{
				bluetoothView.setImageResource(R.drawable.ic_action_bluetooth);
			}
			
			
			// Wifi setImage
			if(wifi == 1)  // wifi disabled = 1
			{
				wifiView.setImageResource(R.drawable.ic_action_network_wifi_dark);
			}else{
				wifiView.setImageResource(R.drawable.ic_action_network_wifi);
			}
			
			
			// Media setImage
			if(media_vol == 0) // media_Vol = 0
			{
				mediaView.setImageResource(R.drawable.ic_action_play_over_video_dark);
			}else{
				mediaView.setImageResource(R.drawable.ic_action_play_over_video);
			}
			
			
			// Alarm setImage
			if(alarm_vol == 0)  // alarm_Vol = 0
			{
				alarmView.setImageResource(R.drawable.ic_action_alarms_dark);
			}else{
				alarmView.setImageResource(R.drawable.ic_action_alarms);
			}		
			
			
			// Ringer setImage
			if(ringer.equals("Normal"))
				{
					//change to normal mode 
					Log.d(TAG, " Normal Mode");
					ringerView.setImageResource(R.drawable.phone_iphone);
				}
				else if(ringer.equals("Silent"))
				{
					//change to silent mode   
					Log.d(TAG, " Silent Mode");
					ringerView.setImageResource(android.R.drawable.ic_lock_silent_mode);
				}
				else if(ringer.equals("Vibration"))
				{
					Log.d(TAG, " Vibration Mode");
					ringerView.setImageResource(R.drawable.phone_vibration);
				}
	      		
			Log.d("Sau", "ImageView Set <--> Nw Adapter initialising");		
		}		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View v = mLayoutInflater.inflate(R.layout.row, parent, false);
		bindView(v, context, cursor);
        return v;
	}

}
