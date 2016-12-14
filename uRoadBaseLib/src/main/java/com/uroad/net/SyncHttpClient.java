package com.uroad.net;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.UREncryption.Function.UREncryption;
import com.uroad.util.HTTPUtil;

import android.content.Context;
import android.os.Message;

/**
 * 这是同步发送http请求的类
 *
 *
 */
public class SyncHttpClient extends AsyncHttpClient {

	private int responseCode;
	protected String result;
	protected AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

		@Override
		protected void sendResponseMessage(org.apache.http.HttpResponse response) {
			responseCode = response.getStatusLine().getStatusCode();
			super.sendResponseMessage(response);
		}

		@Override
		protected void sendMessage(Message msg) {
			handleMessage(msg);
		}

		@Override
		public void onSuccess(String content) {
			result = content;
		}

		@Override
		public void onFailure(Throwable error, String content) {
			result = onRequestFailed(error, content);
		}
	};

	public int getResponseCode() {
		return responseCode;
	}

	@Override
	protected void sendRequest(DefaultHttpClient client,
							   HttpContext httpContext, HttpUriRequest uriRequest,
							   String contentType, AsyncHttpResponseHandler responseHandler,
							   Context context) {
		if (contentType != null) {
			uriRequest.addHeader("Content-Type", contentType);
		}
		new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler)
				.run();
	}

	public String onRequestFailed(Throwable error, String content) {
		return "";
	}

	/**
	 * 同步delete
	 * */
	public void delete(String url, RequestParams queryParams,
					   AsyncHttpResponseHandler responseHandler) {
		delete(url, responseHandler);
	}

	/**
	 * 同步get方法
	 * */
	public String get(String url, RequestParams params) {
		this.get(url, params, responseHandler);
		return result;
	}

	/**
	 * 同步get方法
	 * */
	public String get(String url) {
		this.get(url, null, responseHandler);
		return result;
	}

	/**
	 * 同步get方法 返回的是jsonobjct
	 * */
	public JSONObject getToJson(String url) {
		JSONObject errorjObject = null;
		this.get(url, null, responseHandler);
		try {
			errorjObject = new JSONObject("{status:no,msg:\"系统繁忙，请稍后再试\"}");
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			return errorjObject;
		}
	}

	/**
	 * 同步put
	 * */
	public String put(String url, RequestParams params) {
		this.put(url, params, responseHandler);
		return result;
	}

	/**
	 * 同步put
	 * */
	public String put(String url) {
		this.put(url, null, responseHandler);
		return result;
	}

	/**
	 * 同步post
	 * */
	public String post(String url, RequestParams params) {
		this.post(url, params, responseHandler);
		return result;
	}

	/**
	 * 同步post方法 返回的是jsonobjct
	 * */
	public JSONObject postToJson(String url, RequestParams params) {
		JSONObject errorjObject = null;
		this.post(url, params, responseHandler);
		try {
			errorjObject = new JSONObject("{status:no,msg:\"系统繁忙，请稍后再试\"}");
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			return errorjObject;
		}
	}

	/**
	 * @param isEncript
	 *            :采用加密方式
	 * */
	public JSONObject postToJson(String url, RequestParams params,
								 boolean isEncript) {
		JSONObject errorjObject = null;
		String error = "{status:no,msg:\"系统繁忙，请稍后再试\"}";
		try {
			errorjObject = new JSONObject(error);
			if (!isEncript) {
				this.post(url, params, responseHandler);
			} else {
				String content = params.toString();
				result =UREncryption.URDecrypt(HTTPUtil.postByUroad(url, content));
			}
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			return errorjObject;
		}
	}

	/**
	 * 同步post方法
	 * */
	public String post(String url) {
		this.post(url, null, responseHandler);
		return result;
	}

	/**
	 * 同步post方法 返回的是jsonobject
	 * */
	public JSONObject postToJson(String url) {
		JSONObject errorjObject = null;
		this.post(url, null, responseHandler);
		try {
			errorjObject = new JSONObject("{status:no,msg:\"系统繁忙，请稍后再试\"}");
			if (result != null) {
				return new JSONObject(result);
			} else {
				return errorjObject;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return errorjObject;
		}
	}

	/**
	 * 同步delete方法
	 * */
	public String delete(String url, RequestParams params) {
		this.delete(url, params, responseHandler);
		return result;
	}

	/**
	 * 同步delete方法
	 * */
	public String delete(String url) {
		this.delete(url, null, responseHandler);
		return result;
	}

}
