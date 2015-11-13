package com.pearson.test.daalt.dataservice.request.message.version01;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class MessagePayload {
	protected String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	//required : default to "Create", TestAction may override
	public String Transaction_Type_Code = "Create";
	
	//required : generate new every time, value should be now
	public String Transaction_Datetime;
	
	public MessagePayload() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Transaction_Datetime = dateFormat.format(new Date());
	}
	
	public String getPastOrFutureTimeFormatted(int daysToAdd) {
		Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		nowCal.add(Calendar.DAY_OF_YEAR, daysToAdd);
		return getFormattedDateString(nowCal.getTime());
	}
	
	public String getCurrentTimeFormatted() {
		Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return getFormattedDateString(nowCal.getTime());
	}
	
	public String getFormattedDateString(Date date) {
		return new SimpleDateFormat(dateFormatString).format(date);
	}
}
