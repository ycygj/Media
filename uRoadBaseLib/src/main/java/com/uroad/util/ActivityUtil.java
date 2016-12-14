/**  
 * @Title: ActivityUtil.java
 * @Package com.uroad.util
 * @Description: TODO(闂備焦妞块弸宀勫礉鐎ｎ剛鍗氶柟缁㈠枛閻鏌曟径鍫濆婵☆垰鐗撻弻鐔烘嫚閼碱剛顔戦梺鍓插亽娴滅偤骞忛悩璇参ㄩ柨鏃傛櫕閿熺瓔鍠栭湁闁绘鍎ょ涵鎯归悩宕団檨缂佸倹甯￠弫鎾绘偩瀹�拷婢�
 * @author oupy 
 * @date 2013-8-24 濠电偞鍨堕幐鎼佹晝閵堝懐鏆﹂柨鐕傛嫹0:54:40
 * @version V1.0  
 */
package com.uroad.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.uroad.uroad_ctllib.R;

/**
 * @author Administrator
 * 
 */
public class ActivityUtil {

	public static void pendingTransition_start(Activity context) {
		context.overridePendingTransition(R.anim.base_push_left_in,
				R.anim.base_push_left_out);
	}

	public static void pendingTransition_end(Activity context) {
		context.overridePendingTransition(R.anim.base_push_right_in,
				R.anim.base_push_right_out);
	}

	public static void openActivity(Activity activity, Class<?> pClass) {
		openActivity(activity, pClass, null);
	}
	
	/**
	 * 假如Activity存在，则打开这个Activity而不是新建
	 * **/
	public static void openActivityFromHistory(Activity activity, Class<?> pClass) {
		Intent intent = new Intent(activity, pClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		activity.startActivity(intent);
	}

	public static void openActivity(Activity activity, Class<?> pClass,
			Bundle pBundle) {
		Intent intent = new Intent(activity, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		activity.startActivity(intent);
		pendingTransition_start(activity);
	}

	public static void openActivityForResult(Activity activity,
			Class<?> pClass, Bundle pBundle, int requestCode) {
		Intent intent = new Intent(activity, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		activity.startActivityForResult(intent, requestCode);
		pendingTransition_start(activity);
	}

	public static void openActivity(Activity activity, String pAction) {
		openActivity(activity, pAction, null);
	}

	public static void openActivity(Activity activity, String pAction,
			Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		activity.startActivity(intent);
		pendingTransition_start(activity);
	}

	public static void CloseActivity(Activity activity, Class<?> class1) {
		Intent intent = new Intent(activity, class1);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		activity.startActivity(intent);
	}

	public static void restart(Context f, Class<?> s) {
		Intent i = f.getPackageManager().getLaunchIntentForPackage(
				f.getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		f.startActivity(i);
	}

	/**
	 * get top activity from the stack
	 * **/
	public static String getTopActivity(Activity context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return (runningTaskInfos.get(0).topActivity).toString();
		else
			return null;
	}
	

}
