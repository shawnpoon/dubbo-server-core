package com.shawn.server.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author ShawnPoon
 * 
 */
public class DateUtil {
	public static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat STRING_2_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

	public static String defaultFormat(long time) {
		Date date = new Date(time);
		return SIMPLE_DATE_FORMAT.format(date);
	}

	public static String defaultFormat(Date date) {
		return SIMPLE_DATE_FORMAT.format(date);
	}

	public static String yyyyMMddHHmmss(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	public static String yyyyMMdd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	public static Date defaultFormat(String date) throws ParseException {
		date = date.replace(" ", "");
		return STRING_2_DATE_FORMAT.parse(date);
	}

	public static Date yyyyMMddHHmm(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.parse(date);
	}

	public static Date yyyyMMdd(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.parse(date);
	}

	public static Date yyyyMMddHHmmss(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.parse(date);
	}

}
