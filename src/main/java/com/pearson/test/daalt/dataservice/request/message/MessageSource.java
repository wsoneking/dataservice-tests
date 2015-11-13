package com.pearson.test.daalt.dataservice.request.message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class MessageSource {

	@JsonIgnore
	protected String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	//required : generate new every time, value should be now
	public String Transaction_Datetime;
	
	public MessageSource() {
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
	
	@JsonIgnore
	public String getFormattedDateString(Date date) {
		return new SimpleDateFormat(dateFormatString).format(date);
	}
	
}
