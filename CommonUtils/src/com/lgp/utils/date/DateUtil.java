package com.lgp.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期格式转换
 * */
public class DateUtil{

	/**
	 * 日期类型转换为字符串
	 * @param date 日期
	 * @param dateFormat 日期字符串格式
	 * @return dateString 字符串日期
	 * */
	public static String date2String(java.util.Date date , String dateFormat) {
	
		//默认转换格式 yyyy-MM-dd HH:mm:ss
		if(dateFormat==null){
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		
		String dateString = format.format(date);
		
	    return dateString;
	}
	
	/**
	 * 数据库日期类型转换为字符串
	 * @param date 数据库日期
	 * @param dateFormat 字符串格式
	 * @return dateString 字符串日期
	 * */
	public static String date2String(java.sql.Date date , String dateFormat) {
		
		java.util.Date dDate = date;
		
	   return  date2String(dDate, dateFormat);
	}
	
	/**
	 * 字符串转换日期类型
	 * @param dateFormat 转换格式
	 * @param strDate    日期的字符串类型
	 * @return date      日期类型
	 * */
	public static java.util.Date string2date(String dateFormat,String strDate){
		
		if(dateFormat==null){
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		 
		java.util.Date date = null;
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
}
