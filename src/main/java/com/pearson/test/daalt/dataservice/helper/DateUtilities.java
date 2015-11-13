package com.pearson.test.daalt.dataservice.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jackson.annotate.JsonIgnore;

public class DateUtilities {
	
	@JsonIgnore
	private static String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private DateUtilities() {
		throw new AssertionError();
	}
	
	public static long getLongUTCDate() {
		Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return nowCal.getTime().getTime();
	}
	
	@JsonIgnore
	public static String getFormattedDateString(long millis) {
		String formattedString = null;
		Date date = new Date(millis);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    formattedString = sdf.format(date);
		return formattedString;
	}
}
