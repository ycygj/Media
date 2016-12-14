/**
 * @Title: SystemUtil.java
 * @Package com.uroad.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-9-23 下午2:32:36
 * @version V1.0
 */
package com.uroad.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.uroad.service.AutoUpdateService;
import com.uroad.widget.image.ImageUtils;
import com.uroad.widget.image.ImageViewFactory;

/**
 * @author Administrator
 *
 */
public class SystemUtil {

	/**
	 * 关闭软键盘
	 *
	 * @param obj
	 * @return
	 */
	public static void closeSoftKeyboard(Context c, EditText et) {
		try {
			InputMethodManager inputManager = (InputMethodManager) c
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("关闭软键盘出错", e.getMessage());
		}

	}

	/**
	 * 打开软键盘
	 *
	 * @param obj
	 * @return
	 */
	public static void openSoftKeyboard(Context c, EditText et) {
		try {
			et.requestFocus();
			InputMethodManager inputManager = (InputMethodManager) c
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("关闭软键盘出错", e.getMessage());
		}

	}

	/**
	 * 获取设备deviceid
	 *
	 * @param obj
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = ((TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE));
		return telephonyManager.getDeviceId();
	}

	/**
	 * 获取设备mac地址
	 *
	 * @param obj
	 * @return
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			// 执行命令cmd，只取结果中含有filter的这一行
			while ((line = br.readLine()) != null
					&& line.contains(filter) == false) {
				// result += line;
			}

			result = line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 自动更新
	 *
	 * @param context
	 * @param url
	 *            apk的下载url
	 * @param updateInfo
	 *            更新说明，有默认的
	 * @param res
	 *            出现在通知栏的那个图标
	 * @return
	 */
	public static void update(final Context context, final String url,
							  final String updateInfo, final int res) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("检测到新版本,是否下载更新?");
		if (!StringUtils.isEmpty(updateInfo)) {
			builder.setMessage(updateInfo);
		} else {
			builder.setMessage("更新说明：\n1、修复部分bug\n2、优化程序部分性能");
		}
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context.getApplicationContext(),
						AutoUpdateService.class);
				intent.putExtra("url", url);
				intent.putExtra("launcher", res);
				context.startService(intent);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 自动更新
	 *
	 * @param context
	 * @param url
	 *            apk的下载url
	 * @param updateInfo
	 *            更新说明，有默认的
	 * @param res
	 *            出现在通知栏的那个图标
	 * @return
	 */
	public static void update(final Context context, final String url,
							  final int res) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("检测到新版本,是否下载更新?");
		builder.setMessage("");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context.getApplicationContext(),
						AutoUpdateService.class);
				intent.putExtra("url", url);
				intent.putExtra("launcher", res);
				context.startService(intent);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 清除app缓存(图片和sharedpreference文件)
	 */
	public static void clearAppCache(Context context, String spname) {
		clearCacheFolder(context.getFilesDir(), System.currentTimeMillis());
		clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
		String cacheString = ImageUtils.getDiskCacheDir(context,
				"bitmapCache/original").getAbsolutePath();
		File cache = new File(cacheString);

		if (cache.exists()) {
			ImageViewFactory.create(context).clearCache();
			ImageViewFactory.create(context).flushCache();
			ImageViewFactory.create(context).closeCache();
			// clearCacheFolder(cache, System.currentTimeMillis());
		}
		SharedPreferences preferences = context.getSharedPreferences(spname,
				Activity.MODE_PRIVATE);
		if (preferences != null) {
			Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
		}
	}

	/**
	 * 清除app缓存(仅sharedprference)
	 */
	public static void clearAppCacheOfASp(Context context, String spname) {
		SharedPreferences preferences = context.getSharedPreferences(spname,
				Activity.MODE_PRIVATE);
		if (preferences != null) {
			Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
		}
	}

	/**
	 * 计算App缓存
	 */
	public static String ComputeAppCache(Context context) {

		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = context.getFilesDir();
		File cacheDir = context.getCacheDir();
		String cacheString = ImageUtils.getDiskCacheDir(context, "bitmapCache")
				.getAbsolutePath();
		File cache = new File(cacheString);
		if (cache.exists()) {
			fileSize += FileUtils.getDirSize(cache);
		}
		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);
		if (fileSize > 0) {
			cacheSize = FileUtils.formatFileSize(fileSize);
		}
		return cacheSize;
	}

	/**
	 * 用于高速通系列图片缓存的计算
	 * **/
	public static String ComputeImageCache(Context context, String filename) {
		String cacheSize = "0KB";
		long fileSize = 0;
		File file = getImageCache(context, filename);
		try {
			if (file.isDirectory()) {
				fileSize = getFileSizes(file);
			} else {
				fileSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小", "获取失败!");
		}
		if (fileSize > 0) {
			cacheSize = FileUtils.formatFileSize(fileSize);
		}
		return cacheSize;
	}

	/**
	 * 获取指定文件夹
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private static long getFileSizes(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes(flist[i]);
			} else {
				size = size + getFileSize(flist[i]);
			}
		}
		return size;
	}

	/**
	 * 获取指定文件大小
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}

	public static File getImageCache(Context context, String filename) {
		File cacheDir = null;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					filename);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir;
	}

	/**
	 * 清除缓存目录
	 *
	 * @param dir
	 *            目录
	 * @param numDays
	 *            当前系统时间
	 * @return
	 */
	private static int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	// 打电话
	public static void CallPhone(Context c, String tel) {
		try {
			Uri phoneNumber = Uri.parse("tel:" + tel);
			Intent call = new Intent(Intent.ACTION_DIAL, phoneNumber);
			c.startActivity(call);
		} catch (Exception e) {
			Log.e("客服热线", "手机没有电话功能");
			DialogHelper.showTost(c, "设备不支持电话功能");
		}
	}

	public static void CallPhone2(Context c, String tel) {
		try {
			Uri phoneNumber = Uri.parse("tel:" + tel);
			Intent call = new Intent(Intent.ACTION_CALL, phoneNumber);
			c.startActivity(call);
		} catch (Exception e) {
			Log.e("客服热线", "手机没有电话功能");
			DialogHelper.showTost(c, "设备不支持电话功能");
		}
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 *
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isGPSOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		// boolean network =
		// locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps) {
			return true;
		}

		return false;
	}

	public static final boolean isNetworkLocationOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (network) {
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
	 * 打开蓝牙
	 *
	 * @param askuser
	 *            是否询问用户
	 */
	public static final void OpenBlue(boolean askuser, Context ct) {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (null == adapter) {
			return;
		} else {
			if (!adapter.isEnabled()) {
				if (askuser) {
					// 询问用户
					Intent intent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					// 设置蓝牙可见性，最多300秒
					intent.putExtra(
							BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					ct.startActivity(intent);
				} else {
					adapter.enable();
				}

			}

		}
	}

	public static final void CloseBlue() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (null == adapter) {
			return;
		} else {
			if (adapter.isEnabled()) {
				adapter.disable();

			}

		}
	}

}
