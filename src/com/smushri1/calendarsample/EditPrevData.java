package com.smushri1.calendarsample;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditPrevData {

	static final String DEBUG_TAG = "EditPrevData";
	static final String TAG = "Edit_TimelineEventActivity";
	
	static Button bfrom_date, bto_date, bstart_time, bend_time, bday_picker, b_next, b_cancel;
	static EditText eventName;
	
	static DataBean bdata;
	static StatusData statusData;
	static Cursor cursor;
	static String id;
	
	@SuppressWarnings("static-access")
	public static DataBean getPreviousData(Context context, View rootView, int pos){
		
		Log.d(DEBUG_TAG, "getPreviousData");
		
		bdata = new DataBean();
		statusData = new StatusData(context);
		statusData.open();
		cursor = statusData.query();
		cursor.moveToPosition(pos);
		
		Log.d(TAG, " Getting position = cursor data:  " + cursor.getString(cursor.getColumnIndex(statusData.C_ID)));
		Log.d(TAG, " Getting position = cursor data:  " + cursor.getString(cursor.getColumnIndex(statusData.C_EVENT_NAME)));
		
		id = cursor.getString(cursor.getColumnIndex(statusData.C_ID));
		
		eventName = (EditText)rootView.findViewById(R.id.enter_event_name);
		bstart_time = (Button)rootView.findViewById(R.id.button_start_time);
		bend_time = (Button) rootView.findViewById(R.id.button_end_time);
		bfrom_date = (Button) rootView.findViewById(R.id.button_from_date);
		bto_date = (Button) rootView.findViewById(R.id.button_to_date);
		bday_picker = (Button) rootView.findViewById(R.id.button_repeat_event);
		
		
		String evName = cursor.getString(cursor.getColumnIndex(statusData.C_EVENT_NAME));
		eventName.setText("" + evName);
		
		String startTime = cursor.getString(cursor.getColumnIndex(statusData.C_START_TIME));
		bstart_time.setText("" + startTime);
		
		String endTime = cursor.getString(cursor.getColumnIndex(statusData.C_END_TIME));
		bend_time.setText("" + endTime);
		
		String fromDate = cursor.getString(cursor.getColumnIndex(statusData.C_FROM_DATE));
		bfrom_date.setText("" + fromDate);
		
		String toDate = cursor.getString(cursor.getColumnIndex(statusData.C_TO_DATE));
		bto_date.setText("" + toDate);
		
		String multiDay = cursor.getString(cursor.getColumnIndex(statusData.C_REPEAT_EVENT));
		bday_picker.setText("" + multiDay);
		
		String ringer = cursor.getString(cursor.getColumnIndex(statusData.C_RINGER_MODE));
		int media_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_MEDIA_VOL));
		int alarm_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_ALARM_VOL));
		int wifi = cursor.getInt(cursor.getColumnIndex(statusData.C_WIFI_MODE));
		int blue = cursor.getInt(cursor.getColumnIndex(statusData.C_BLUETOOTH_MODE));
		int data = cursor.getInt(cursor.getColumnIndex(statusData.C_DATA_MODE));
		
		bdata.setEventName(evName);
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
		
		statusData.close();
		return bdata;
	}	
}
