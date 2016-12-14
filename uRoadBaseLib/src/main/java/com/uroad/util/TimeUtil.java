/**
 * @Title: TimeUtil.java
 * @Package com.uroad.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-8-22 下午6:42:17
 * @version V1.0
 */
package com.uroad.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class TimeUtil {

	/*
	 * 判断是白天还是黑夜
	 */
	public static boolean isDay() {
		boolean ret = false;
		if (Calendar.getInstance().HOUR_OF_DAY > 6
				&& Calendar.getInstance().HOUR_OF_DAY < 18) {
			ret = true;
		}
		return ret;
	}

	/*
	 * 获取当前时间特定时间的间隔
	 */
	public static String timeAgo(String timeStr) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			date = format.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}

		long timeStamp = date.getTime();

		Date currentTime = new Date();
		long currentTimeStamp = currentTime.getTime();
		long seconds = (currentTimeStamp - timeStamp) / 1000;

		long minutes = Math.abs(seconds / 60);
		long hours = Math.abs(minutes / 60);
		long days = Math.abs(hours / 24);

		if (seconds <= 15) {
			return "刚刚";
		} else if (seconds < 60) {
			return seconds + "秒前";
		} else if (seconds < 120) {
			return "1分钟前";
		} else if (minutes < 60) {
			return minutes + "分钟前";
		} else if (minutes < 120) {
			return "一小时前";
		} else if (hours < 24) {
			return hours + "小时前";
		} else if (hours < 24 * 2) {
			return "一天前";
		} else if (days < 30) {
			return days + "天前";
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
			String dateString = formatter.format(date);
			return dateString;
		}

	}

	/*
	 * 获取当前时间特定时间的间隔
	 */
	public static String timeAgo(String timeStr, String formatstr) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatstr);
			date = format.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}

		long timeStamp = date.getTime();

		Date currentTime = new Date();
		long currentTimeStamp = currentTime.getTime();
		long seconds = (currentTimeStamp - timeStamp) / 1000;

		long minutes = Math.abs(seconds / 60);
		long hours = Math.abs(minutes / 60);
		long days = Math.abs(hours / 24);

		if (seconds <= 15) {
			return "刚刚";
		} else if (seconds < 60) {
			return seconds + "秒前";
		} else if (seconds < 120) {
			return "1分钟前";
		} else if (minutes < 60) {
			return minutes + "分钟前";
		} else if (minutes < 120) {
			return "一小时前";
		} else if (hours < 24) {
			return hours + "小时前";
		} else if (hours < 24 * 2) {
			return "一天前";
		} else if (days < 30) {
			return days + "天前";
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
			String dateString = formatter.format(date);
			return dateString;
		}

	}

	/**
	 * 得到特定日期几天后的时间
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 计算date之前n天的日期
	 */
	public static Date getDateBefore(Date date, int n) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - n);
		return now.getTime();
	}


	//获取当前时间
	/**
	 * patt  时间格式  （yyyy-MM-dd）
	 * @param patt
	 * @return
	 */
	public static String getCurrTime(String patt) {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat(patt);
		return sf.format(date);
	}

}
