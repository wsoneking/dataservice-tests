package com.pearson.test.daalt.dataservice.request.message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class TransformerMessagePayload {
	protected String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	
	public TransformerMessagePayload() {
	}
	
	public String getPastOrFutureTimeFormatted(int daysToAdd) {
		Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		nowCal.add(Calendar.DAY_OF_YEAR, daysToAdd);
		return getFormattedDateString(nowCal.getTime());
	}
	
	public String getFormattedDateString(Date date) {
		return new SimpleDateFormat(dateFormatString).format(date);
	}
}
