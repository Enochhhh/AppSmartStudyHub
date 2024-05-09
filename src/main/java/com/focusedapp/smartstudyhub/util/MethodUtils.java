package com.focusedapp.smartstudyhub.util;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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

	public static Date addDaysForDate(Date date, Integer numberDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, numberDays);
        date = calendar.getTime();
		return date;
	}
	
	public static Date addWeeksForDate(Date date, Integer numberWeeks) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, numberWeeks);
        date = calendar.getTime();
		return date;
	}
	
	public static Date addMonthsForDate(Date date, Integer numberMonths) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, numberMonths);
        date = calendar.getTime();
		return date;
	}
	
	public static Date addYearsForDate(Date date, Integer numberYears) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, numberYears);
        date = calendar.getTime();
		return date;
	}
	
	public static Date setTimeOfDateToMidnight(Long dateMili) {
		Date date = new Date(dateMili + 7 * 60 * 60 * 1000);
		// Get date at time 00:00:00
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dateMili = calendar.getTime().getTime() - 7 * 60 * 60 * 1000;
		return new Date(dateMili);
	}
	
	public static Long distanceDaysBetweenTwoDateNotLocalDateTime(Date dateFirst, Date dateSecond, ZoneId zoneId) {
		LocalDateTime dateFirstTimeZone = convertoToLocalDateTime(dateFirst);
		LocalDateTime dateSecondTimeZone = convertoToLocalDateTime(dateSecond);
		ZonedDateTime zonedDateTime1 = dateFirstTimeZone.atZone(zoneId);
        ZonedDateTime zonedDateTime2 = dateSecondTimeZone.atZone(zoneId);

	    return ChronoUnit.DAYS.between(zonedDateTime1.toLocalDate(), zonedDateTime2.toLocalDate());
	}
}
