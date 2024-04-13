package com.focusedapp.smartstudyhub.util;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class MethodUtils {

	/**
	 * Calculate the distance of two dates
	 * 
	 * @param dateFirst
	 * @param dateSecond
	 * @param timeUnit
	 * @return
	 */
	public static Long distanceDaysBetweenTwoDate(LocalDateTime dateFirst, LocalDateTime dateSecond, ZoneId zoneId) {
        ZonedDateTime zonedDateTime1 = dateFirst.atZone(zoneId);
        ZonedDateTime zonedDateTime2 = dateSecond.atZone(zoneId);

	    return ChronoUnit.DAYS.between(zonedDateTime1.toLocalDate(), zonedDateTime2.toLocalDate());
	}
	
	public static LocalDateTime convertoToLocalDateTime(Date date) {
		ZoneId targetZone = ZoneId.of("Asia/Ho_Chi_Minh");
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, targetZone);		
		
		return localDateTime;
	}
}
