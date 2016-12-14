package com.uroad.widget.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageUtils {

	private static final String TAG = "BitmapCommonUtils";

	public static Bitmap getUnErrorBitmap(InputStream is,
										  BitmapFactory.Options options) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {
			options.inSampleSize += 1;
			return getUnErrorBitmap(is, options);
		}
		return bitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}


	public static String getBitmapWH(Bitmap bitmap) {// 获取android当前可用内存大小
		byte[] datas = Bitmap2Bytes(bitmap);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
		return options.outWidth + "X" + options.outHeight;
	}

	/**
	 * 获取可以使用的缓存目录
	 *
	 * @param context
	 * @param uniqueName
	 *            目录名称
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(context)
				.getPath() : context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 获取bitmap的字节大小
	 *
	 * @param bitmap
	 * @return
	 */
	public static int getBitmapSize(Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 获取程序外部的缓存目录
	 *
	 * @param context
	 * @return
	 */
	public static File getExternalCacheDir(Context context) {
		final String cacheDir =Environment.getExternalStorageDirectory().getPath();
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	/**
	 * 获取文件路径空间大小
	 *
	 * @param path
	 * @return
	 */
	public static long getUsableSpace(File path) {
		try {
			final StatFs stats = new StatFs(Environment.getExternalStorageDirectory().getPath());
			return (long) stats.getBlockSize()
					* (long) stats.getAvailableBlocks();
		} catch (Exception e) {
			Log.e(TAG,
					"获取 sdcard 缓存大小 出错，请查看AndroidManifest.xml 是否添加了sdcard的访问权限");
			e.printStackTrace();
			return -1;
		}

	}

}