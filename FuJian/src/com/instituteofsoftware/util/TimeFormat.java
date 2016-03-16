package com.instituteofsoftware.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeFormat {
	public static Date parse(String str){
		DateFormat df = null;
		if(str.contains("-")&&str.contains("."))
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
		else if(str.contains("-"))
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else if(str.contains("/"))
			df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		else if(str.length()==26||str.length()==25)
			df = new SimpleDateFormat("MMM dd yyyy hh:mm:ss:SSSa",Locale.US);
		else if(str.length()==17)
			df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		else if(str.length()==12)
			df = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			return df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Long format12(Long t){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		return Long.valueOf(df.format(new Date(t)));
	}
	public static String format19(Long t){
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(new Date(t));
	}
	public static String format23(Long t){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
		return df.format(new Date(t));
	}
	public static void main(String[] args) {
		System.out.println(TimeFormat.parse("2016/02/24 00:00:00").getTime());
		System.out.println(TimeFormat.parse("2016/02/23 24:00:00").getTime());
	}
}
