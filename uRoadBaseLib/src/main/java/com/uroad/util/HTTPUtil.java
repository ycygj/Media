package com.uroad.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import com.UREncryption.Function.UREncryption;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

public class HTTPUtil {
	/**
	 * 发送http请求
	 *
	 * @param url
	 * @param para
	 * @return
	 */
	public static String post(String url, HashMap<String, String> para) {

		boolean result = true;
		String resp = "";
		try {
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(true);
			// con.setRequestProperty("Content-Type", "application/xml");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setRequestProperty("Charset", "UTF-8");
			con.connect();

			if (para != null) {
				String content = "";
				DataOutputStream output = new DataOutputStream(
						con.getOutputStream());

				for (Entry<String, String> e : para.entrySet()) {

					content += "&" + e.getKey() + "="
							+ URLEncoder.encode(e.getValue(), "UTF-8");
				}

				content = content.substring(1);
				// log.debug("post参数:" + content);
				output.writeBytes(content);
				output.flush();
				output.close();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			String line = "";

			while ((line = reader.readLine()) != null) {
				resp += line;
			}

			reader.close();

			// if (JsonUtil.getStatus(resp)) {
			// result = true;
			// }else {
			// result = false;
			// }

		} catch (Exception e) {
			Log.e("http请求失败！", e.toString());
			// result = resp;

		}

		return resp;
	}

	/**
	 * 用加密库进行加密传输
	 * **/
	public static String postByUroad(String url, String content) {

		boolean result = true;
		String resp = "";
		try {
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(true);
			// con.setRequestProperty("Content-Type", "application/xml");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setRequestProperty("Charset", "UTF-8");
			con.connect();

			if (!TextUtils.isEmpty(content)) {
				DataOutputStream output = new DataOutputStream(
						con.getOutputStream());

				content = UREncryption.UREncrypt(content);
				output.writeBytes(content);
				output.flush();
				output.close();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			String line = "";

			while ((line = reader.readLine()) != null) {
				resp += line;
			}

			reader.close();

		} catch (Exception e) {
			Log.e("http请求失败！", e.toString());
			// result = resp;

		}

		return resp;
	}

	/**
	 * 对图片下载链接出现中文的处理
	 * **/
	public static String getUrlEncode(String url) {
		String enUft;
		try {
			String url1 = url.substring(0, url.lastIndexOf('/') + 1);
			String envalue = url.substring(url.lastIndexOf('/') + 1);
			enUft = URLEncoder.encode(envalue, "utf-8")
					.replaceAll("\\+", "%20");
			return url1 + enUft;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 判断网络连接类型
	 * **/
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 判断mobile网络是否可用
	 * **/
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI是否可用
	 * **/
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断是否有网络连接
	 * **/
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断是否开启GPS
	 * **/
	public static boolean isOpenGPS(Context mContext) {
		LocationManager alm = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}

	/**
	 * 强制帮用户打开GPS
	 *
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 检查网络是否可用
	 *
	 * @param paramContext
	 * @return
	 */
	public static boolean checkEnable(Context paramContext) {
		boolean i = false;
		NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
				.getSystemService("connectivity")).getActiveNetworkInfo();
		if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
			return true;
		return false;
	}

	/**
	 * 将ip的整数形式转换成ip形式
	 *
	 * @param ipInt
	 * @return
	 */
	public static String int2ip(int ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	/**
	 * 获取当前ip地址
	 *
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		try {

			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int i = wifiInfo.getIpAddress();
			return int2ip(i);
		} catch (Exception ex) {
			return ex.getMessage();
		}
		// return null;
	}

}
