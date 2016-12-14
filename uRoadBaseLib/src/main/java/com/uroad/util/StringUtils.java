package com.uroad.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dateFormater3 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	private final static SimpleDateFormat dateFormater4 = new SimpleDateFormat(
			"yyyy年MM月dd日  HH小时mm分");
	private final static SimpleDateFormat dateFormater5 = new SimpleDateFormat(
			"MM月dd日");
	private final static Pattern password1 = Pattern.compile("^[a-zA-Z]+");
	private final static Pattern password2 = Pattern.compile("^[0-9]+");
	private final static Pattern password3 = Pattern.compile("^[A-Za-z0-9]+");
	private final static Pattern carnoMach = Pattern
			.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$");

	/**
	 * 判断当前日期是星期几
	 *
	 * @param pTime
	 *            设置的需要判断的时间 //格式如2012-09-08
	 *
	 *
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */

	// String pTime = "2012-03-12";
	public static String getWeek(String pTime) {

		String Week = "星期";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			c.setTime(format.parse(pTime));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

		return Week;
	}

	/**
	 * 获取年龄
	 *
	 * @param sdate
	 * @return
	 */
	public static int getAge(Date birthDay) {
		Calendar cal = Calendar.getInstance();

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}

		return age;
	}

	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 昨天，今天，上午，下午
	 *
	 * @param sdate
	 * @return
	 */
	public static String iphoneTime(String sdate) {
		Date time = toDate(sdate);
		Calendar today = new GregorianCalendar();
		Calendar calendar = new GregorianCalendar();
		today.setTime(new Date());
		calendar.setTime(time);

		try {
			String ret = "";
			// 对比的时间
			long stime = calendar.getTime().getTime();

			// 今天的时间开始边界
			long todaybegin = dateFormater2.parse(
					dateFormater2.format(today.getTime())).getTime();
			// 昨天的时间开始边界
			long yesterdaybegin = todaybegin - 24 * 60 * 60 * 1000;
			// 昨天正午的时间开始边界
			long yestermoonbegin = todaybegin - 12 * 60 * 60 * 1000;
			// 今天正午的时间开始边界
			long todaymoonbegin = todaybegin + 12 * 60 * 60 * 1000;

			//

			if (stime > yesterdaybegin && stime < yestermoonbegin) {
				ret += "昨天  上午  ";
			} else if (stime > yestermoonbegin && stime < todaybegin) {
				ret += "昨天  下午  ";
			} else if (stime > todaybegin && stime < todaymoonbegin) {
				ret += "今天  上午  ";
			} else if (stime > todaymoonbegin) {
				ret += "今天  下午  ";
			} else {
				ret += dateFormater2.format(calendar.getTime()) + " ";
			}

			if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
				if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
					ret += "0" + calendar.get(Calendar.HOUR_OF_DAY);
				} else {
					ret += calendar.get(Calendar.HOUR_OF_DAY);
				}
			} else {
				int day = calendar.get(Calendar.HOUR_OF_DAY) - 12;
				if (day < 10) {
					ret += "0" + day;
				} else {
					ret += day;
				}
			}

			if (calendar.get(Calendar.MINUTE) < 10) {
				ret += ":0" + calendar.get(Calendar.MINUTE);
			} else {
				ret += ":" + calendar.get(Calendar.MINUTE);
			}
			return ret;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return sdate;
		}

	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 *
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.format(today);
			String timeDate = dateFormater2.format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 *
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 密码匹配：纯数字或纯字母或数字字母混合都算对
	 * **/
	public static boolean isPassword(String password) {
		if (password == null || password.trim().length() == 0) {
			return false;
		}
		return password1.matcher(password).matches()
				|| password2.matcher(password).matches()
				|| password3.matcher(password).matches();

	}

	/**
	 * 字符串转整数
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 年－月－日
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static String toShortDate(String sdate) {
		try {
			return dateFormater2.format(dateFormater.parse(sdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return sdate;
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * 年－月－日-时－分
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static String toShortfenDate(String sdate) {
		try {
			return dateFormater3.format(dateFormater.parse(sdate));
		} catch (ParseException e) {
			return "";
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * 年－月－日-时－分
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static String toLongDate(String sdate) {
		try {
			return dateFormater4.format(dateFormater3.parse(sdate));
		} catch (ParseException e) {
			return "";
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * 年－月－日
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static String toShortDate(Date date) {
		try {
			return dateFormater2.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return date.toString();
		}
	}

	/**
	 * 年－月－日
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static Date getShortDate(String date) {
		try {
			return dateFormater2.parse(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * 月－日
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static String toShortDate2(Date date) {
		try {
			return dateFormater5.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return date.toString();
		}
	}

	/**
	 * 对象转整数
	 *
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 *
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 *
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param sdate
	 * @return
	 */
	public static String friendly_time_2(String sdate) {

		Date time = null;
		try {
			time = dateFormater.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (time == null) {
			return "Unknown";
		}
		Calendar cal = Calendar.getInstance();

		long diff = 0;
		Date dnow = cal.getTime();
		String str = "";
		diff = dnow.getTime() - time.getTime();

		if (diff > 604800000) {// 7 * 24 * 60 * 60 * 1000=604800000 毫秒
			str = dateFormater2.format(time);
		} else if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
			// System.out.println("X天前");
			str = (int) Math.floor(diff / 86400000f) + "天前";
		} else if (diff > 18000000) {// 5 * 60 * 60 * 1000=18000000 毫秒
			// System.out.println("X小时前");
			str = (int) Math.floor(diff / 18000000f) + "小时前";
		} else if (diff > 60000) {// 1 * 60 * 1000=60000 毫秒
			// System.out.println("X分钟前");
			str = (int) Math.floor(diff / 60000) + "分钟前";
		} else if (diff > 10000) {
			str = (int) Math.floor(diff / 1000) + "秒前";
		} else {
			str = "刚刚";
		}
		return str;

	}

	/**
	 * 手机号码判断的正则表达式
	 */
	public static Boolean isMobile(String mobile) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 车牌号判断的正则表达式(首位是省市汉字)
	 */
	public static Boolean isCarNo(String carno) {
		Matcher m = carnoMach.matcher(carno);
		return m.matches();
	}

	// 得到转码后的数据
	public static String encoder(String url)
			throws UnsupportedEncodingException {
		// 转中文
		String url1 = url.substring(0, url.lastIndexOf('/') + 1);
		String envalue = url.substring(url.lastIndexOf('/') + 1);
		String str = url1 + URLEncoder.encode(envalue, "UTF-8");
		return str;
	}

}