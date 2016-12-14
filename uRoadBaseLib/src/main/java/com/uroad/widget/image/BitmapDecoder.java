package com.uroad.widget.image;

import java.io.FileDescriptor;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmapDecoder {
	private static final String TAG = "BitmapDecoder";

	private BitmapDecoder() {
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
														 int resId, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		try {
			return getBitmap(res, resId, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromResource内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap decodeSampledBitmapFromFile(String filename,
													 int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeFile(filename, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		try {
			return getBitmap(filename, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromFile内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor) {

		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			return getBitmap(fileDescriptor, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromDescriptor内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int scale) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;

		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		options.inSampleSize = scale;

		options.inJustDecodeBounds = false;
		try {
			return getBitmap(fileDescriptor, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromDescriptor内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		try {
			return getBitmap(fileDescriptor, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromDescriptor内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap decodeSampledBitmapFromInputStream(InputStream is) {

		final BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			return getBitmap(is, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromDescriptor内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			e.printStackTrace();
			return null;
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			final float totalPixels = width * height;

			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	private static Bitmap getBitmap(FileDescriptor fileDescriptor,
									BitmapFactory.Options options) {
		try {
			return BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
					options);
		} catch (OutOfMemoryError e) {
			options.inSampleSize = options.inSampleSize + 1;
			return getBitmap(fileDescriptor, options);
		} catch (Exception e) {
			return null;
		}
	}

	private static Bitmap getBitmap(String path, BitmapFactory.Options options) {
		try {
			return BitmapFactory.decodeFile(path, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromDescriptor内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			options.inSampleSize = options.inSampleSize + 1;
			return getBitmap(path, options);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private static Bitmap getBitmap(InputStream is,
									BitmapFactory.Options options) {
		try {
			return BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromDescriptor内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			options.inSampleSize = options.inSampleSize + 1;
			return getBitmap(is, options);
		} catch (Exception e) {
			Log.e("bitmapcache", e.getMessage());
			return null;
		}
	}

	private static Bitmap getBitmap(Resources res, int resId,
									BitmapFactory.Options options) {
		try {
			return BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			Log.e(TAG,
					"decodeSampledBitmapFromDescriptor内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
			options.inSampleSize = options.inSampleSize + 1;
			return getBitmap(res, resId, options);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
