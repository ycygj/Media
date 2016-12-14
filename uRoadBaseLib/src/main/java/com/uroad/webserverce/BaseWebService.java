package com.uroad.webserverce;

import org.json.JSONObject;

import com.uroad.common.BaseConstants;
import com.uroad.net.RequestParams;
import com.uroad.net.SyncHttpClient;

/**
 * @author Administrator
 *
 */
public class BaseWebService {

	public RequestParams getBaseParams() {
		RequestParams params = new RequestParams();
		return params;
	}

	/* 错误日志 */
	public static JSONObject postError(String appname, String version,
									   String vername, String mobileinfo, String errormsg) {
		String url = "http://106.187.97.123/AndroidAPI/index.php?/log/error";
		try {
			SyncHttpClient client = new SyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("appname", appname);
			params.put("version", version);
			params.put("vername", vername);
			params.put("mobileinfo", mobileinfo);
			params.put("errormsg", errormsg);
			return client.postToJson(url, params);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * google 经纬度转百度经纬度
	 *
	 * @param
	 * **/
	public String getBaiduPoint(String x, String y) {
		String url = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4&x="
				+ x + "&y=" + y;
		try {
			SyncHttpClient client=new SyncHttpClient();
			return client.get(url);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * google 经纬度转百度经纬度批量
	 *
	 * @param x
	 *            ->23.13,23.14
	 * @param y
	 *            ->113.44,113.22
	 * @return [{"error":0,"x":"MjIuNTk1MTc2MDQ5NjM0","y":"MTEzLjQwMjAwMDUzMTAy"},{"error":0,"x":"MjMuMDA2ODM2Mjc3MDE2","y":"MjIzLjAwNTk4MjcyODg2"}]
	 * **/
	public String getBatchBaiduPoint(String x, String y) {
		String url = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4&mode=1&x="
				+ x + "&y=" + y;
		try {
			SyncHttpClient client = new SyncHttpClient();
			return client.get(url);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取天气
	 *
	 * @param
	 * **/
	public String getWeather(String cityCode) {
		String url = BaseConstants.weatherUrl + cityCode + ".html";
		try {
			SyncHttpClient client = new SyncHttpClient();
			return client.get(url);
		} catch (Exception e) {
			return "";
		}
	}

}
