package com.focusedapp.smartstudyhub.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.focusedapp.smartstudyhub.exception.ParseException;


public class MethodUtils {

	/**
	 * Calculate the distance of two dates
	 * 
	 * @param dateFirst
	 * @param dateSecond
	 * @param timeUnit
	 * @return
	 */
	public static Long distanceBetweenTwoDate(Date dateFirst, Date dateSecond, TimeUnit timeUnit) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dateFirst = dateFormat.parse(dateFormat.format(dateFirst));
			dateSecond = dateFormat.parse(dateFormat.format(dateSecond));
		} catch (Exception e) {
			throw new ParseException("Parse String to Java Util Date Failure!", "MethodUtils -> distanceBetweenTwoDate");		
		}
		
		Long diffInMilies = dateSecond.getTime() - dateFirst.getTime();
		return timeUnit.convert(diffInMilies, TimeUnit.MILLISECONDS);
	}
}
