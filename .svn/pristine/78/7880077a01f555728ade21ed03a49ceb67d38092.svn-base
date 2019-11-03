package com.xryb.zhtc.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 * 
 * @author hshzh
 * 
 */
public class DateTimeUtil {
	/** 默认日期格式 */
	private static final String DEF_DATE_FORMAT = "yyyy-MM-dd";
	/** 默认时间格式 */
	private static final String DEF_TIME_FORMAT = "HH:mm:ss";
	/** 默认日期时间格式 */
	private static final String DEF_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 精确到分钟时间格式 */
	private static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

	/** 默认日期匹配格式 */
	private static final String DEF_DATE_PATTERN = "\\d{4}\\-\\d{2}-\\d{2}";
	/** 默认日期时间匹配格式 */
	private static final String DEF_DATETIME_PATTERN = "\\d{4}\\-\\d{2}-\\d{2}\\p{javaWhitespace}\\d{2}:\\d{2}:\\d{2}";
	
	/**
	 * 格式化时间
	 * @param date
	 * @param strFormat
	 * @return
	 */
	public static String formatDateTime(Date date, String strFormat) {
		if (date == null || strFormat == null || "".equals(strFormat)) {
			return "";
		}
		String result = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
			result = dateFormat.format(date);
		} catch (Exception ex) {
			result = "";
		}
		return result;
	}

	/**
	 * 获取纳秒
	 * 
	 * @return
	 */
	public static long getLeonardSeconds() {
		return System.nanoTime();
	}

	/**
	 * 取得系统当前时间戳
	 * 
	 * @return 系统当前时间戳对象
	 */
	public static Timestamp getSysTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 判断时间格式是否正确
	 * 
	 * @param strDateTime
	 *            时间字符串
	 * @param strFormat
	 *            时间格式字符串
	 * @return
	 */
	public static boolean isValidDate(String strDateTime, String strFormat) {
		if (strDateTime == null || "".equals(strDateTime) || strFormat == null || "".equals(strFormat)) {
			return false;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(strDateTime);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	/**
	 * 根据指定的格式将传入字符串转化为日期对象
	 * @param date 传入字符串
	 * @param format 指定格式
	 * @return 格式化后日期对象
	 */
	public static Date parseDate(String date, String format) {
		Date d;
		try {
			d = new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			d = null;
		}
		return d;
	}
	/**
	 * 日期运算
	 * 
	 * @param date
	 *            Date类型对象
	 * @param d
	 *            天，正数为加天，负数为减天
	 * @param h
	 *            小时，正数为加小时，负数为减小时
	 * @param m
	 *            分，同上
	 * @param s
	 *            秒，同上
	 * @return Date
	 */
	public static Date dateOperation(Date date, int d, int h, int m, int s) {
		if (date == null) {
			return null;
		}
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.set(Calendar.DATE, cld.get(Calendar.DATE) + d);
		cld.set(Calendar.HOUR, cld.get(Calendar.HOUR) + h);
		cld.set(Calendar.MINUTE, cld.get(Calendar.MINUTE) + m);
		cld.set(Calendar.SECOND, cld.get(Calendar.SECOND) + s);
		return cld.getTime();
	}

	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param early
	 * @param late
	 * @return
	 */
	public static final int daysBetween(Date early, Date late) {
		java.util.Calendar calst = java.util.Calendar.getInstance();
		java.util.Calendar caled = java.util.Calendar.getInstance();
		calst.setTime(early);
		caled.setTime(late);
		// 设置时间为0时
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;

		return days;
	}

	/**
	 * 计算两个日期间的天
	 * @param date1
	 *            <String>
	 * @param date2
	 *            <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getDateSpace(String date1, String date2) throws ParseException {		
		int result = 0;
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		calst.setTime(df.parse(date1));
		caled.setTime(df.parse(date2));
		// 设置时间为0时
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime()/1000) - (int)(calst.getTime().getTime()/1000))/3600/24;
		return days;
	}
	/**
	 * 将yyyy-MM-dd格式的字符串转换为日期对象
	 * @param date yyyy-MM-dd格式字符串
	 * @return 转换后的日期对象，无法转换时返回null
	 */
	public static Date getDate(String date) {
		if (!matchesPattern(date, DEF_DATE_PATTERN)) return null;
		return parseDate(date, DEF_DATE_FORMAT);
	}
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为日期对象
	 * @param datetime yyyy-MM-dd HH:mm:ss格式字符串
	 * @return 转换后的日期对象，无法转换时返回null
	 */
	public static Date getDateTime(String datetime) {
		if (!matchesPattern(datetime, DEF_DATETIME_PATTERN)) return null;
		return parseDate(datetime, DEF_DATETIME_FORMAT);
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为时间戳期对象
	 * @param datetime yyyy-MM-dd HH:mm:ss格式字符串
	 * @return 转换后的时间戳对象，无法转换时返回null
	 */
	public static Timestamp getDateTimeStamp(String datetime) {
		if (!matchesPattern(datetime, DEF_DATETIME_PATTERN)) return null;
		return new Timestamp(parseDate(datetime, DEF_DATETIME_FORMAT).getTime());		
	}
	
	/**
	 * 将指定格式的字符串对象转换为日期对象
	 * @param date 字符串
	 * @param pattern 指定的格式
	 * @return 转换后的日期，无法转换时返回null
	 */
	public static Date getDate(String date, String pattern) {
		return getDate(date, pattern, null);
	}
	
	/**
	 * 将指定格式的字符串对象转换为日期对象
	 * @param date 字符串
	 * @param pattern 指定的格式
	 * @param defVal 默认返回值
	 * @return 转换后的日期，无法转换时返回defVal指定值
	 */
	public static Date getDate(String date, String pattern, Date defVal) {
		if (date == null || pattern == null) return null;
		Date ret = parseDate(date, pattern);
		return (ret == null) ? defVal : ret;
	}
	
	/**
	 * 检测输入字符串是否与指定格式匹配
	 * @param input 待检测字符串
	 * @param pattern 检测格式
	 * @return
	 * <li>true：匹配</li>
	 * <li>false：不匹配</li>
	 */
	private static boolean matchesPattern(String input, String pattern) {
		return (input != null) && (input.matches(pattern));
	}
	
	/**
	 * 将日期对象格式化成yyyy-mm-dd类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatDate(Date date) {
		return formatDateToString(date, DEF_DATE_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成HH:mm:ss类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatTime(Date date) {
		return formatDateToString(date, DEF_TIME_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成yyyy-MM-dd HH:mm:ss类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatDateTime(Date date) {
		return formatDateToString(date, DEF_DATETIME_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成yyyy-MM-dd HH:mm类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formateMinuteDate(Date date) {
		return formatDateToString(date, MINUTE_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成指定的格式字符串
	 * @param date 日期对象
	 * @param format 格式
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	private static String formatDateToString(Date date, String format) {
		if (date == null) return null;
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 将日期对象格式化成指定格式的字符串
	 * @param date 日期对象
	 * @param format 格式
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatDate(Date date, String format) {
		if (date == null || format == null) return null;
		String ret;		
		try {
			ret = new SimpleDateFormat(format).format(date);
		} catch (RuntimeException e) {
			ret = null;
		}
		return ret;
	}
	
	/**
	 * 取得指定日期所在月的最后一天日期对象
	 * @param d 指定日期
	 * @return 指定日期当月的最后一天日期对象，如指定日期为null时，返回当前月的最后一天日期对象
	 */
	public static Date getLastDayObjectInMonth(Date d) {
		Calendar cal = Calendar.getInstance();
		if (d != null) cal.setTime(d);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	
	/**
	 * 取得指定日期所在月的最后一天日期值
	 * @param d 指定日期
	 * @return 当月的最后一天日期值，如指定日期为null时，返回当前月的最后一天日期值
	 * @see #getLastDayObjectInMonth(Date)
	 */
	public static int getLastDayInMonth(Date d) {
		Date date = getLastDayObjectInMonth(d);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	
	/**
	 * 返回给定的两个时间之间的差值
	 * @param bgDate
	 * @param edDate
	 * @return 以分为单位返回
	 */
	public static long getTimeInterval(long bgDate,long edDate) {
		return (edDate - bgDate)/1000/60;
	}
	
	/**
	 * 返回指定时间段日期的毫秒表示形式
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @return
	 */
	public static long getSpecifyDate(int hourOfDay,int minute,int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 
				hourOfDay, 
				minute, 
				second);
		return cal.getTimeInMillis();
	}
	public static void main(String[] args) {
		//System.out.println(DateTimeUtil.formatDateTime(new Date(), "HH:mm"));
		//System.out.println(DateTimeUtil.formatDateTime(DateTimeUtil.dateOperation(new Date(), -1, -1, 0, 0), "yyyy-MM-dd HH:mm:ss"));
		//计算时间差
		System.out.println(DateTimeUtil.getTimeInterval(0, (new Date()).getTime()));
		Date date = new Date();
	    DateTimeUtil.parseDate("2017-03-05 16:10","yyyy-MM-dd HH:mm");
		DateTimeUtil.getTimeInterval(DateTimeUtil.parseDate("2016-09-05 16:10","yyyy-MM-dd HH:mm").getTime(), (new Date()).getTime());
		// System.out.println(DateTimeUtil.isValidDate("2014-04-24 18:17:52", "yyyy-MM-dd HH:mm:ss"));
	}


}
