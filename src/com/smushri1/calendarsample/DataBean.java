package com.smushri1.calendarsample;

public class DataBean {
		
	public String e_id, eventName;
	public String startTime, endTime;
	public String fromDate, toDate;
	public String repeatEvent, ringerMode;
	public int mediaVol, alarmVol, wifi, bluetooth, data;
	

	public DataBean(){
		super();
	}


	public String getE_id() {
		return e_id;
	}

	public void setE_id(String e_id) {
		StatusData.C_ID = e_id;
	}

	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}


	public String getRepeatEvent() {
		return repeatEvent;
	}


	public void setRepeatEvent(String repeatEvent) {
		this.repeatEvent = repeatEvent;
	}

	public String getRingerMode() {
		return ringerMode;
	}

	public void setRingerMode(String ringerMode) {
		this.ringerMode = ringerMode;
	}

	public int getMediaVol() {
		return mediaVol;
	}

	public void setMediaVol(int mediaVol) {
		this.mediaVol = mediaVol;
	}

	public int getAlarmVol() {
		return alarmVol;
	}

	public void setAlarmVol(int alarmVol) {
		this.alarmVol = alarmVol;
	}

	public int getWifi() {
		return wifi;
	}

	public void setWifi(int wifi) {
		this.wifi = wifi;
	}

	public int getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(int bluetooth) {
		this.bluetooth = bluetooth;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataBean [eventName=");
		builder.append(eventName);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", repeatEvent=");
		builder.append(repeatEvent);
		builder.append(", ringerMode=");
		builder.append(ringerMode);
		builder.append("]");
		return builder.toString();
	}


	

}
