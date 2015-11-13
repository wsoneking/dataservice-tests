package com.pearson.test.daalt.dataservice.utility;

import java.util.Calendar;

public class DateTimestampTranslater {
	
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.add(Calendar.DATE, -30);		// 30 days ago
		java.util.Date now = calendar.getTime();

		System.out.println("Calendar: " + calendar.getTime() + "\ntimestamp: "+now.getTime());
		
	}

}
