package com.uroad.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.uroad.uroad_ctllib.R;

/*
 * 自动更新的服务，可以自动下载，显示进度条
 * */
public class AutoUpdateService extends Service {

	private static final int NOTIFY_ID = 0;
	private int progress;
	private NotificationManager mNotificationManager;
	private boolean canceled;
	// 返回的安装包url
	private String apkUrl = "";
	// private String apkUrl = MyApp.downloadApkUrl;
	/* 下载包安装路径 */
	private static final String savePath = Environment
			.getExternalStorageDirectory().getPath() + "/updateApk/";

	private Context mContext = this;
	private String appnameString;
	private String saveFileName;
	private int app_launcher;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
				installApk();
				break;
			case 2:
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				int rate = msg.arg1;
				if (rate < 100) {
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, rate + "%");
					contentview.setProgressBar(R.id.progressbar, 100, rate,
							false);
				} else {
					System.out.println("下载完毕!!!!!!!!!!!");
					// 下载完毕后变换通知形式
					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					stopSelf();// 停掉服务自身
				}
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate(); 
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE); 
		appnameString = mContext.getResources().getString(R.string.app_name);
		String visionnameString = "";
		PackageManager pManager= getPackageManager();
		try {
			PackageInfo pInfo=pManager.getPackageInfo(getPackageName(),0);
			visionnameString=pInfo.versionName;
		} catch (NameNotFoundException e) { 
			e.printStackTrace();
		}
		saveFileName =savePath +appnameString+visionnameString+ ".apk";  
	}

	private void startDownload() {
		canceled = false;
		downloadApk();
	}

	//
	Notification mNotification;

	// 通知栏
	/**
	 * 创建通知
	 */
	@SuppressWarnings("deprecation")
	private void setUpNotification() {
//		int icon = R.drawable.ic_launcher;
		int icon = app_launcher;
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.base_update_notification_layout);
		String appnameString = mContext.getResources().getString(
				R.string.app_name);
		contentView.setTextViewText(R.id.name, "正在下载" + appnameString + "...");
		contentView.setImageViewResource(R.id.image, icon);
		// 指定个性化视图
		mNotification.contentView = contentView;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}
	
	 
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) { 
		super.onStart(intent, startId);
		if(intent!=null){
			apkUrl = intent.getStringExtra("url");	
			app_launcher = intent.getIntExtra("launcher", 0);
			if(apkUrl!=null){
				if (downLoadThread == null || !downLoadThread.isAlive()) {

					progress = 0;
					setUpNotification();
					new Thread() {
						public void run() {
							// 下载
							startDownload();
						};
					}.start();
				}						
			}	
		}
	}

	//
	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private Thread downLoadThread;

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);

	}

	private int lastRate = 0;
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1) {
						mHandler.sendMessage(msg);
						lastRate = progress;
					}
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(0);
						// 下载完了，cancelled也要设置
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				} while (!canceled);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

}
