package com.uroad.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ObjectHelper {
	public static String Convert2String(Date date, String type) {
		if (date == null)
			return "";
		DateFormat format = new SimpleDateFormat(type);
		String strString = format.format(date);
		return strString;
	}

	/**
	 * @Name: Convert2MathCount
	 * @Author: 陈伟炎
	 * @Date: 2012-8-11
	 * @param count
	 * @param obj
	 * @return
	 * @Description: 返回小数点后几位数
	 */
	public static String Convert2MathCount(int count, Object obj) {
		if (obj == null)
			return "";
		try {
			return String.format("%." + count + "f", obj);
		} catch (Exception e) {
			// TODO: handle exception
			return obj.toString();
		}
	}

	public static String Convert2String(Object value) {
		try {
			return String.valueOf(value);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}

	public static JSONObject Convert2JsonObject(Object value) {
		try {
			return new JSONObject(value.toString());
		} catch (Exception e) {
			// TODO: handle exception
			return new JSONObject();
		}
	}

	public static long Convert2Long(Object object) {
		try {
			long l = Long.parseLong(object.toString());
			return Long.parseLong(object.toString());
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static double Convert2Double(Object object) {
		try {

			return Double.parseDouble(object.toString());
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static List<String> Convert2ListString(String str,
												  String regularExpression) {
		List<String> strings = new LinkedList<String>();
		if (str != null && !str.trim().equals("")
				&& !str.toLowerCase().equals("null")) {
			try {
				String[] arrayStrings = str.split(regularExpression);

				for (String s : arrayStrings) {
					strings.add(s);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return strings;
	}


	public static Date Convert2Date(String dateString) {
		return Convert2Date(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date Convert2Date(String dateString, String type) {
		if (dateString == null || dateString.trim().equals("")
				|| dateString.trim().equals("null"))
			return null;
		DateFormat df = new SimpleDateFormat(type);
		Date date = new Date();
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block\
			Log.e("StringToDate", dateString + "    " + e);
			e.printStackTrace();
		}
		return date;
	}

	public static int Convert2Int(Object obj) {
		try {
			return Integer.valueOf(obj.toString().trim());
		} catch (Exception e) {
			// TODO: handle exceptionet
			return 0;
		}
	}

	public static float Convert2Float(String val) {
		try {
			return Float.valueOf(val);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static String GetBundleString(Activity activity, String key) {
		try {
			Bundle bundle = activity.getIntent().getExtras();
			if (bundle == null)
				return "";
			String value = bundle.getString(key);
			if (value == null || value.equals(""))
				return "";
			return value;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}

	}

	public static int GetBundleInt(Activity activity, String key) {
		try {
			Bundle bundle = activity.getIntent().getExtras();
			if (bundle == null)
				return 0;
			return bundle.getInt(key, 0);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public static String GetBundleString(Intent intent, String key) {
		try {
			Bundle bundle = intent.getExtras();
			if (bundle == null)
				return "";
			String value = bundle.getString(key);
			if (value == null || value.equals(""))
				return "";
			return value;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}

	}

	public static int GetBundleInt(Intent intent, String key) {
		try {
			Bundle bundle = intent.getExtras();
			if (bundle == null)
				return 0;
			return bundle.getInt(key, 0);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	/**
	 * 计算时间差
	 *
	 * @return 相差毫秒数
	 * **/
	public static long timeDifference(String begin, String end) {
		SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date beginDate = sDate.parse(begin);
			Date endDate = sDate.parse(end);
			long beginTime = beginDate.getTime();
			long endTime = endDate.getTime();
			return (endTime - beginTime) > 0 ? (endTime - beginTime)
					: (beginTime - endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算与当前时间的时间差
	 *
	 * @return 时间差毫米数
	 * **/
	public static long timeDifferenceFromNow(long beginTime, String end) {
		SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date endDate = sDate.parse(end);
			long endTime = endDate.getTime();
			return (endTime - beginTime) > 0 ? (endTime - beginTime)
					: (beginTime - endTime);
		} catch (ParseException e) {
		}
		return 0;
	}

	/**
	 * 计算与当前时间的时间差
	 *
	 * @return 时间差毫米数
	 * **/
	public static long timeDifferenceFromNow(long beginTime) {
		long endTime = System.currentTimeMillis();
		return (endTime - beginTime) >= 0 ? (endTime - beginTime)
				: (beginTime - endTime);
	}

	/**
	 * 保留小数点后两位
	 * **/
	public static double keepTowDecimal(double value) {
		return (double) (Math.round(value * 100) / 100.00);
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.equals("0") || str.equalsIgnoreCase("null")
				|| str.trim().equals("")) {
			return true;
		}
		return false;
	}
}
