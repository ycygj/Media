/**
 * @Title: WeatherFraments.java
 * @Package com.uroad.framents
 * @Description: TODO(鐢ㄤ竴鍙ヨ瘽鎻忚堪璇ユ枃浠跺仛浠?箞)
 * @author oupy
 * @date 2013-8-22 涓嬪崍5:02:50
 * @version V1.0
 */
package com.uroad.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.uroad.common.BaseConstants;
import com.uroad.net.AsyncHttpClient;
import com.uroad.net.JsonHttpResponseHandler;
import com.uroad.uroad_ctllib.R;
import com.uroad.util.ImageUtil;
import com.uroad.util.JsonUtil;
import com.uroad.util.TimeUtil;
import com.uroad.widget.image.UroadImageView;

/**
 * @author Administrator
 * 天气控件
 */

public class WeatherCtl extends RelativeLayout {

	UroadImageView uiv_weather_icon;
	TextView tvCity, tvWeather, tvTemplate;
	String url = "", cityCode = "101280101";
	Context mContext;

	/**
	 * @param context
	 */
	public WeatherCtl(Context context) {
		super(context);
		mContext = context;
		init();
	}

	/**
	 * @param context
	 */
	public WeatherCtl(Context context, String city) {
		super(context);
		mContext = context;
		cityCode = city;
		init();
		// TODO Auto-generated constructor stub
	}

	public WeatherCtl(Context context, AttributeSet attrs, String city) {
		super(context, attrs);
		mContext = context;
		cityCode = city;
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		LayoutInflater.from(mContext).inflate(R.layout.base_weather_layout,
				this, true);
		uiv_weather_icon = (UroadImageView) findViewById(R.id.uiv_weather_icon);
		uiv_weather_icon.setBaseScaleType(ScaleType.FIT_XY);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvTemplate = (TextView) findViewById(R.id.tvTemplate);
		tvWeather = (TextView) findViewById(R.id.tvWeather);
		url = BaseConstants.weatherUrl + cityCode + ".html";
		// LoadWeather task = new LoadWeather();
		// task.execute("");

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, checkHandler);
	}

	private JsonHttpResponseHandler checkHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject result) {
			// TODO Auto-generated method stub

			try {
				if (result != null) {
					JSONObject data = result.getJSONObject("weatherinfo");
					tvCity.setText(JsonUtil.GetString(data, "city"));
					tvWeather.setText(JsonUtil.GetString(data, "weather"));
					tvTemplate.setText(JsonUtil.GetString(data, "temp2") + "~"
							+ JsonUtil.GetString(data, "temp1"));
					String baseImgUrl = "http://www.weather.com.cn/m/i/icon_weather/42x30/", imgUrl = "";
					if (TimeUtil.isDay()) {
						imgUrl = JsonUtil.GetString(data, "img1");
					} else {
						imgUrl = JsonUtil.GetString(data, "img2");
					}
					if (imgUrl != null
							&& imgUrl.substring(0, imgUrl.indexOf("."))
							.length() <= 2) {
						imgUrl = imgUrl.substring(0, imgUrl.indexOf(".") - 1)
								+ "0"
								+ imgUrl.substring(imgUrl.indexOf(".") - 1);
					}

					String imgIconFlag = imgUrl.substring(0,
							imgUrl.indexOf("."));
					Bitmap bitmap = ImageUtil.GetBitmapByFileName(mContext,
							imgIconFlag.toLowerCase());
					uiv_weather_icon.getImageView().setImageBitmap(bitmap);
					// uiv_weather_icon.setImageUrlNotCache(baseImgUrl +
					// imgUrl);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onSuccess(result);
		}

		@Override
		public void onFinish() {
			super.onFinish();
		}

		@Override
		public void onFailure(Throwable error, String content) {
		}

		@Override
		public void onFailure(Throwable error) {
		}

	};

}
