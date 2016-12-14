package com.uroad.util;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.uroad.uroad_ctllib.R;

public class QQWeiboUtil {

	Tencent mTencent;
	Context mContext;
	QQShare mQQShare = null;
	public static QQAuth mQQAuth;
	public OnQQInterface qqInterface;

	public QQWeiboUtil(Context mCon) {
		mContext = mCon;
		mQQAuth = QQAuth.createInstance(
				mContext.getResources().getString(R.string.qqshare_Id),
				mContext.getApplicationContext());
		try {
			mTencent = Tencent.createInstance(mContext.getResources()
					.getString(R.string.qqshare_Id), mContext
					.getApplicationContext());
			mQQShare = new QQShare(mContext, mQQAuth.getQQToken());
		} catch (Exception e) {
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					try {
						JSONObject response = (JSONObject) msg.obj;
						qqInterface.OnGetLoginResult(response);
						DialogHelper.showTost(mContext, "da" + response.toString());
					} catch (Exception e) {
						// Log.e("qqshare", e.getMessage());
					}
					break;
			}
		}
	};

	/**
	 * 登录 所有权限用“all”
	 * **/
	public void login() {
		try {
			// mTencent.loginWithOEM((Activity) mContext, "all",
			// new QQUIListener(), "101030481", "101030481", "xxxx");
			mTencent.login((Activity) mContext, "all", new QQUIListener());
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void logout() {
		mTencent.logout(mContext);
	}

	public void shareTextToQQ(String content, String appName) {
		Bundle bundle = new Bundle();

		bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		// 这条分享消息被好友点击后的跳转URL。
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				"http://connect.qq.com/");
		// 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, appName);
		// 分享的图片URL
		// bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
		// "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
		// 分享的消息摘要，最长50个字
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		// 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		// // 标识该消息的来源应用，值为应用名称+AppId。
		// bundle.putString(QQShare.SHARE_TO_QQ_KEY_TYPE, appName
		// + mContext.getResources().getString(R.string.qqshare_Id));
		if (isApkInstalled(mContext, "com.tencent.mobileqq"))
			doShareToQQ(bundle);
	}

	public void shareTextToQQ(String content, String url, String appName) {
		Bundle bundle = new Bundle();

		bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		// 这条分享消息被好友点击后的跳转URL。
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
		// 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, appName);
		// 分享的图片URL
		// 分享的消息摘要，最长50个字
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		// 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		// // 标识该消息的来源应用，值为应用名称+AppId。
		bundle.putString(QQShare.SHARE_TO_QQ_KEY_TYPE, appName
				+ mContext.getResources().getString(R.string.qqshare_Id));
		if (isApkInstalled(mContext, "com.tencent.mobileqq"))
			doShareToQQ(bundle);
	}

	/**
	 *
	 * **/
	public void shareImageToQQ(String title, String content, String imgurl,
							   String url, String appName) {
		Bundle bundle = new Bundle();

		bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		// 这条分享消息被好友点击后的跳转URL。
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
		// 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		// 分享的图片URL
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgurl);
		// 分享的消息摘要，最长50个字
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		// 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		// // 标识该消息的来源应用，值为应用名称+AppId。
		bundle.putString(QQShare.SHARE_TO_QQ_KEY_TYPE, appName
				+ mContext.getResources().getString(R.string.qqshare_Id));

		doShareToQQ(bundle);
	}

	public void shareTextToQZone(String content, String appName) {
		Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.qq.com/");
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, appName);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);
		if (isApkInstalled(mContext, "com.tencent.mobileqq"))
			doShareToQzone(params);
	}

	public static boolean isApkInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 用异步方式启动分享
	 *
	 * @param params
	 */
	private void doShareToQQ(final Bundle params) {
		final Activity activity = (Activity) mContext;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mQQShare.shareToQQ(activity, params, new QQUIListener());
			}
		}).start();
	}

	/**
	 * 用异步方式启动分享
	 *
	 * @param params
	 */
	private void doShareToQzone(final Bundle params) {
		final Activity activity = (Activity) mContext;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mTencent.shareToQzone(activity, params, new QQUIListener());
			}
		}).start();
	}

	private class QQUIListener implements IUiListener {

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			DialogHelper.showTost(mContext, "取消操作!");
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			Message msg = mHandler.obtainMessage();
			msg.what = 0;
			msg.obj = response;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub
			DialogHelper.showTost(mContext, "操作失败：" + arg0.errorMessage);
		}
	}

	public void setQQInterface(OnQQInterface qqInter) {
		this.qqInterface = qqInter;
	}

	public interface OnQQInterface {
		abstract void OnGetLoginResult(JSONObject result);
	}
}