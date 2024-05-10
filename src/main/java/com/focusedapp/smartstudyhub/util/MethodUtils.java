package com.focusedapp.smartstudyhub.util;


import java.time.DayOfWeek;
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
	
	public static Date getAnyDayOfWeekFromDateSpecified(Date date, Integer day, ZoneId zoneId) {
		LocalDateTime dateLocal = convertoToLocalDateTime(date);
		switch(day) {
			case 2:
				return Date.from(dateLocal.with(DayOfWeek.MONDAY).atZone(ZoneId.systemDefault()).toInstant());
			case 3:
				return Date.from(dateLocal.with(DayOfWeek.TUESDAY).atZone(ZoneId.systemDefault()).toInstant());
			case 4:
				return Date.from(dateLocal.with(DayOfWeek.WEDNESDAY).atZone(ZoneId.systemDefault()).toInstant());
			case 5:
				return Date.from(dateLocal.with(DayOfWeek.THURSDAY).atZone(ZoneId.systemDefault()).toInstant());
			case 6:
				return Date.from(dateLocal.with(DayOfWeek.FRIDAY).atZone(ZoneId.systemDefault()).toInstant());
			case 7:
				return Date.from(dateLocal.with(DayOfWeek.SATURDAY).atZone(ZoneId.systemDefault()).toInstant());
			case 8:
				return Date.from(dateLocal.with(DayOfWeek.SUNDAY).atZone(ZoneId.systemDefault()).toInstant());
			default:
				return null;
		}
	}
}
