/**
 * @Title: ImageUtil.java
 * @Package com.uroad.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-8-9 下午2:45:55
 * @version V1.0
 */
package com.uroad.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Base64;
import android.view.View;

/**
 * @author Administrator
 *
 */
public class ImageUtil {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;

	public static boolean isSDCardExist() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static void writeFile(byte[] data, String fileName) {
		File f = new File(fileName);
		FileOutputStream fout = null;
		try {
			if (!f.exists())
				f.createNewFile();
			fout = new FileOutputStream(f);
			fout.write(data);
			fout.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fout != null) {
					fout.close();
					fout = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap decodeFile(File f) {
		Bitmap b = null;
		int IMAGE_MAX_SIZE = 1000;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
			return null;
		}
		return b;
	}

	public static enum ScalingLogic {
		CROP, FIT
	}

	public static int calculateSampleSize(int srcWidth, int srcHeight,
										  int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcWidth / dstWidth;
			} else {
				return srcHeight / dstHeight;
			}
		} else {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcHeight / dstHeight;
			} else {
				return srcWidth / dstWidth;
			}
		}
	}

	public static Bitmap decodeSampledBitmapFromByte(byte[] res, int reqWidth,
													 int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(res, 0, res.length, options);

		if (reqHeight == -1) {
			options.inSampleSize = calculateSampleSize(options.outWidth,
					options.outHeight, options.outWidth, options.outHeight,
					ScalingLogic.CROP);
		} else {
			options.inSampleSize = calculateSampleSize(options.outWidth,
					options.outHeight, reqWidth, reqHeight, ScalingLogic.CROP);

		}
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return getUnErrorBitmap(res, options);
	}

	private static Bitmap getUnErrorBitmap(byte[] res,
										   BitmapFactory.Options options) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeByteArray(res, 0, res.length, options);
		} catch (OutOfMemoryError e) {
			options.inSampleSize += 1;
			return getUnErrorBitmap(res, options);
		}
		return bitmap;
	}

	public static String getBitmapWH(Bitmap bitmap) {// 获取android当前可用内存大小
		byte[] datas = Bitmap2Bytes(bitmap);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
		return options.outWidth + "X" + options.outHeight;
	}

	/*
	 * 压缩图片，以质量为代价
	 */
	public static Bitmap CompressBitmapByQuality(Bitmap bitmap, int quality) {
		InputStream is = Bitmap2InputStream(bitmap, quality);
		return BitmapFactory.decodeStream(is);
	}

	/*
	 * 压缩图片，以大小为代价 quality 为2就是原来的1/2 4就是1/4
	 */
	public static Bitmap CompressBitmapBySize(Bitmap bitmap, int quality) {
		InputStream is = Bitmap2InputStream(bitmap);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = quality;
		return BitmapFactory.decodeStream(is, null, options);
	}

	// 将Bitmap转换成InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// 将Bitmap转换成InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// 将InputStream转换成Bitmap
	public static Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * 把Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static String getBitmapWH(Context context, int res) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), res, options);
		return options.outWidth + "X" + options.outHeight;
	}

	public static String getBitmapSize(Context context, int res) {// 获取android当前可用内存大小

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), res, options);
		int size = 0;
		if (options.inPreferredConfig == Bitmap.Config.ARGB_8888) {
			size = options.outHeight * options.outWidth * 4;
		} else if (options.inPreferredConfig == Bitmap.Config.ARGB_4444) {
			size = options.outHeight * options.outWidth * 2;
		} else if (options.inPreferredConfig == Bitmap.Config.ALPHA_8) {
			size = options.outHeight * options.outWidth;
		}
		return Formatter.formatFileSize(context, size);// 将获取的内存大小规格化
	}

	public static String getBitmapSize(Context context, Bitmap bitmap) {// 获取android当前可用内存大小

		long size = bitmap.getRowBytes() * bitmap.getHeight();

		return Formatter.formatFileSize(context, size);// 将获取的内存大小规格化
	}

	/**
	 * 放大缩小图片
	 *
	 * @param bitmap
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return 处理了的图片
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	/**
	 * 将Drawable转化为Bitmap
	 *
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		if (width == 0 || height == 0) {
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap转换成Drawable
	 *
	 * @param bmp
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bmp) {
		return new BitmapDrawable(bmp);
	}

	/**
	 * 获得圆角图片的方法
	 *
	 * @param bitmap
	 *            原图
	 * @param roundPx
	 *            角度
	 * @return 处理的图片
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获得带倒影的图片方法
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * byte数组转换成Bitmap
	 *
	 * @param bmp
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 数组转换成Bitmap
	 *
	 * @param buffer
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] buffer) {
		return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
	}

	/**
	 * 图片效果叠加
	 *
	 * @param bmp
	 *            限制了尺寸大小的Bitmap
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap overlay(Bitmap bmp, int overRef, Resources res) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		// 对边框图片进行缩放
		Bitmap overlay = BitmapFactory.decodeResource(res, overRef);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);

		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix,
				true);

		int pixColor = 0;
		int layColor = 0;

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;

		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;

		final float alpha = 0.5F;

		int[] srcPixels = new int[width * height];
		int[] layPixels = new int[width * height];
		bmp.getPixels(srcPixels, 0, width, 0, 0, width, height);
		overlayCopy.getPixels(layPixels, 0, width, 0, 0, width, height);

		int pos = 0;
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				pos = i * width + k;
				pixColor = srcPixels[pos];
				layColor = layPixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);

				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);

				newR = (int) (pixR * alpha + layR * (1 - alpha));
				newG = (int) (pixG * alpha + layG * (1 - alpha));
				newB = (int) (pixB * alpha + layB * (1 - alpha));
				layA = (int) (pixA * alpha + layA * (1 - alpha));

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));

				srcPixels[pos] = Color.argb(newA, newR, newG, newB);
			}
		}

		bitmap.setPixels(srcPixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 组合涂鸦图片和源图片
	 *
	 * @param src
	 *            源图片
	 * @param watermark
	 *            涂鸦图片
	 * @return
	 */
	public static Bitmap doodle(Bitmap src, Bitmap watermark) {
		// 另外创建一张图片
		Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas canvas = new Canvas(newb);
		canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
		canvas.drawBitmap(watermark,
				(src.getWidth() - watermark.getWidth()) / 2,
				(src.getHeight() - watermark.getHeight()) / 2, null); // 涂鸦图片画到原图片中间位置
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		watermark.recycle();
		watermark = null;

		return newb;
	}

	/**
	 * 怀旧效果(相对之前做了优化快一倍)
	 *
	 * @param bmp
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap oldRemeber(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR,
						newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixels[width * i + k] = newColor;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		return bitmap;
	}

	/**
	 * 模糊效果
	 *
	 * @param bmp
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap blurImage(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int newColor = 0;

		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++) {
			for (int k = 1, len = height - 1; k < len; k++) {
				for (int m = 0; m < 9; m++) {
					int s = 0;
					int p = 0;
					switch (m) {
						case 0:
							s = i - 1;
							p = k - 1;
							break;
						case 1:
							s = i;
							p = k - 1;
							break;
						case 2:
							s = i + 1;
							p = k - 1;
							break;
						case 3:
							s = i + 1;
							p = k;
							break;
						case 4:
							s = i + 1;
							p = k + 1;
							break;
						case 5:
							s = i;
							p = k + 1;
							break;
						case 6:
							s = i - 1;
							p = k + 1;
							break;
						case 7:
							s = i - 1;
							p = k;
							break;
						case 8:
							s = i;
							p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}

				for (int m = 0; m < 9; m++) {
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}

				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		return bitmap;
	}

	/**
	 * 柔化效果(高斯模糊)(优化后比上面快三倍)
	 *
	 * @param bmp
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap blurImageAmeliorate(Bitmap bmp) {
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		return bitmap;
	}

	/**
	 * 图片锐化（拉普拉斯变换）
	 *
	 * @param bmp
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
		// 拉普拉斯矩阵
		int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		return bitmap;
	}

	/**
	 * 浮雕效果
	 *
	 * @param bmp
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap emboss(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				pixColor = pixels[pos + 1];
				newR = Color.red(pixColor) - pixR + 127;
				newG = Color.green(pixColor) - pixG + 127;
				newB = Color.blue(pixColor) - pixB + 127;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 底片效果
	 *
	 * @param bmp
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Bitmap film(Bitmap bmp) {
		// RGBA的最大值
		final int MAX_VALUE = 255;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				newR = MAX_VALUE - pixR;
				newG = MAX_VALUE - pixG;
				newB = MAX_VALUE - pixB;

				newR = Math.min(MAX_VALUE, Math.max(0, newR));
				newG = Math.min(MAX_VALUE, Math.max(0, newG));
				newB = Math.min(MAX_VALUE, Math.max(0, newB));

				pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 光照效果
	 *
	 * @param bmp
	 * @return
	 */
	public static Bitmap sunshine(Bitmap bmp) {
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int centerX = width / 2;
		int centerY = height / 2;
		int radius = Math.min(centerX, centerY);

		final float strength = 150F; // 光照强度 100~150
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				newR = pixR;
				newG = pixG;
				newB = pixB;

				// 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
				int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
						centerX - k, 2));
				if (distance < radius * radius) {
					// 按照距离大小计算增加的光照值
					int result = (int) (strength * (1.0 - Math.sqrt(distance)
							/ radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 色彩度调整
	 *
	 * @param drawable
	 * @param sature
	 *            为0表示图片显示为没有色彩，数字越大图片显示的色彩就越浓
	 * @return
	 */
	public static Drawable chroma(Drawable drawable, int sature) {
		drawable.mutate();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(sature);
		ColorMatrixColorFilter cmf = new ColorMatrixColorFilter(cm);
		drawable.setColorFilter(cmf);

		return drawable;
	}

	/**
	 * 将图片保存到设备上
	 *
	 * @param photoPath
	 *            --原图路经
	 * @param aFile
	 *            --保存缩图
	 * @param newWidth
	 *            --缩图宽度
	 * @param newHeight
	 *            --缩图高度
	 */

	public static boolean bitmapToFile(String photoPath, File aFile,
									   int newWidth, int newHeight) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
		options.inJustDecodeBounds = false;

		// 计算缩放比
		options.inSampleSize = reckonThumbnail(options.outWidth,
				options.outHeight, newWidth, newHeight);
		bitmap = BitmapFactory.decodeFile(photoPath, options);

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photoBytes = baos.toByteArray();

			if (aFile.exists()) {
				aFile.delete();
			}

			aFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(aFile);
			fos.write(photoBytes);
			fos.flush();
			fos.close();

			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			if (aFile.exists()) {
				aFile.delete();
			}
			return false;
		}
	}

	/**
	 * 保存图片为PNG
	 *
	 * @param bitmap
	 * @param name
	 */
	public static void savePNG_After(Bitmap bitmap, String name) {
		if (bitmap == null) {
			return;
		}
		File file = new File(name);
		try {
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存图片为JPEG
	 *
	 * @param bitmap
	 * @param path
	 */
	public static boolean saveJPGE_After(Bitmap bitmap, String path) {
		boolean ret = false;
		if (bitmap != null) {
			File file = new File(path);
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
					out.flush();
					out.close();
				}
				ret = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ret = false;
		}
		return ret;
	}

	/**
	 * 计算缩放比
	 *
	 * @param oldWidth
	 * @param oldHeight
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static int reckonThumbnail(int oldWidth, int oldHeight,
									  int newWidth, int newHeight) {

		if ((oldHeight > newHeight && oldWidth > newWidth)
				|| (oldHeight <= newHeight && oldWidth > newWidth)) {
			int be = (int) (oldWidth / (float) newWidth);
			if (be <= 1) {
				be = 1;
			}

			return be;
		} else if (oldHeight > newHeight && oldWidth <= newWidth) {

			int be = (int) (oldHeight / (float) newHeight);
			if (be <= 1) {
				be = 1;
			}

			return be;
		}
		return 1;
	}

	/**
	 * 水印
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}

		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 截图View，返回Bitmap
	 *
	 * @param v
	 * @return
	 */
	public static Bitmap getViewBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);

		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);

		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);

		return bitmap;
	}

	/**
	 * 图片合成
	 *
	 * @return
	 */
	public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second,
												 int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		int fw = first.getWidth();
		int fh = first.getHeight();
		int sw = second.getWidth();
		int sh = second.getHeight();
		Bitmap newBitmap = null;
		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);
		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);
		}
		return newBitmap;
	}

	public static Bitmap decodeBitmap(String path, int displayWidth,
									  int displayHeight) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
		// 获取比例大小
		int wRatio = (int) Math.ceil(op.outWidth / (float) displayWidth);
		int hRatio = (int) Math.ceil(op.outHeight / (float) displayHeight);
		// 如果超出指定大小，则缩小相应的比例
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				op.inSampleSize = wRatio;
			} else {
				op.inSampleSize = hRatio;
			}
		}
		op.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, op);
		return Bitmap
				.createScaledBitmap(bmp, displayWidth, displayHeight, true);
	}

	/**
	 * 采用复杂计算来决定缩放
	 *
	 * @param path
	 * @param maxImageSize
	 * @return
	 */
	public static Bitmap decodeBitmap(String path, int maxImageSize) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
		int scale = 1;
		if (op.outWidth > maxImageSize || op.outHeight > maxImageSize) {
			scale = (int) Math.pow(
					2,
					(int) Math.round(Math.log(maxImageSize
							/ (double) Math.max(op.outWidth, op.outHeight))
							/ Math.log(0.5)));
		}
		op.inJustDecodeBounds = false;
		op.inSampleSize = scale;
		bmp = BitmapFactory.decodeFile(path, op);
		return bmp;
	}

	public static Cursor queryThumbnails(Activity context) {
		String[] columns = new String[] { MediaStore.Images.Thumbnails.DATA,
				MediaStore.Images.Thumbnails._ID,
				MediaStore.Images.Thumbnails.IMAGE_ID };
		return context.managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, columns,
				null, null, MediaStore.Images.Thumbnails.DEFAULT_SORT_ORDER);
	}

	public static Cursor queryThumbnails(Activity context, String selection,
										 String[] selectionArgs) {
		String[] columns = new String[] { MediaStore.Images.Thumbnails.DATA,
				MediaStore.Images.Thumbnails._ID,
				MediaStore.Images.Thumbnails.IMAGE_ID };
		return context.managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, columns,
				selection, selectionArgs,
				MediaStore.Images.Thumbnails.DEFAULT_SORT_ORDER);
	}

	public static Bitmap queryThumbnailById(Activity context, int thumbId) {
		String selection = MediaStore.Images.Thumbnails._ID + " = ?";
		String[] selectionArgs = new String[] { thumbId + "" };
		Cursor cursor = ImageUtil.queryThumbnails(context, selection,
				selectionArgs);

		if (cursor.moveToFirst()) {
			String path = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
			cursor.close();
			return ImageUtil.decodeBitmap(path, 100, 100);
		} else {
			cursor.close();
			return null;
		}
	}

	public static Bitmap[] queryThumbnailsByIds(Activity context,
												Integer[] thumbIds) {
		Bitmap[] bitmaps = new Bitmap[thumbIds.length];
		for (int i = 0; i < bitmaps.length; i++) {
			bitmaps[i] = ImageUtil.queryThumbnailById(context, thumbIds[i]);
		}

		return bitmaps;
	}

	/**
	 * 获取全部
	 *
	 * @param context
	 * @return
	 */
	public static List<Bitmap> queryThumbnailList(Activity context) {
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		Cursor cursor = ImageUtil.queryThumbnails(context);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			String path = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
			Bitmap b = ImageUtil.decodeBitmap(path, 100, 100);
			bitmaps.add(b);
		}
		cursor.close();
		return bitmaps;
	}

	public static List<Bitmap> queryThumbnailListByIds(Activity context,
													   int[] thumbIds) {
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		for (int i = 0; i < thumbIds.length; i++) {
			Bitmap b = ImageUtil.queryThumbnailById(context, thumbIds[i]);
			bitmaps.add(b);
		}

		return bitmaps;
	}

	public static Cursor queryImages(Activity context) {
		String[] columns = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DISPLAY_NAME };
		return context.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
	}

	public static Cursor queryImages(Activity context, String selection,
									 String[] selectionArgs) {
		String[] columns = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DISPLAY_NAME };
		return context.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
				selection, selectionArgs,
				MediaStore.Images.Media.DEFAULT_SORT_ORDER);
	}

	public static Bitmap queryImageById(Activity context, int imageId) {
		String selection = MediaStore.Images.Media._ID + "=?";
		String[] selectionArgs = new String[] { imageId + "" };
		Cursor cursor = ImageUtil
				.queryImages(context, selection, selectionArgs);
		if (cursor.moveToFirst()) {
			String path = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			cursor.close();
			// return ImageUtil.decodeBitmap(path, 260, 260);
			return ImageUtil.decodeBitmap(path, 220); // 看看和上面这种方式的差别,看了，差不多
		} else {
			cursor.close();
			return null;
		}
	}

	/**
	 * 根据缩略图的Id获取对应的大图 cursor = BitmapUtils.queryThumbnails(this);
	 * if(cursor.moveToFirst()){ List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	 * thumbIds = new int[cursor.getCount()]; for(int i=0;
	 * i<cursor.getCount();i++){ cursor.moveToPosition(i); String currPath =
	 * cursor
	 * .getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails
	 * .DATA)); thumbIds[i] =
	 * cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore
	 * .Images.Thumbnails._ID)); Bitmap b =
	 * BitmapUtils.decodeBitmap(currPath,100,100); bitmaps.add(b); }
	 *
	 * @param context
	 * @param thumbId
	 * @return
	 */
	public static Bitmap queryImageByThumbnailId(Activity context,
												 Integer thumbId) {

		String selection = MediaStore.Images.Thumbnails._ID + " = ?";
		String[] selectionArgs = new String[] { thumbId + "" };
		Cursor cursor = ImageUtil.queryThumbnails(context, selection,
				selectionArgs);

		if (cursor.moveToFirst()) {
			int imageId = cursor
					.getInt(cursor
							.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
			cursor.close();
			return ImageUtil.queryImageById(context, imageId);
		} else {
			cursor.close();
			return null;
		}
	}

	/**
	 * 该方法中我们将对图像的颜色，亮度，对比度等进行设置 需要用到ColorMatrix类。ColorMatrix类是一个四行五列的矩阵
	 * 每一行影响着[R,G,B,A]中的一个 ------------------------- a1 b1 c1 d1 e1 a2 b2 c2 d2
	 * e2 a3 b3 c3 d3 e3 a4 b4 c4 d4 e4 ------------------------- Rnew =>
	 * a1*R+b1*G+c1*B+d1*A+e1 Gnew => a2*R+b2*G+c2*B+d2*A+e2 Bnew =>
	 * a3*R+b3*G+c3*B+d3*A+e3 Gnew => a4*R+b4*G+c4*B+d4*A+e4 其中R，G，B的值是128，A的值是0
	 *
	 * 最后将颜色的修改，通过Paint.setColorFilter应用到Paint对象中。
	 * 主要对于ColorMatrix,需要将其包装成ColorMatrixColorFilter对象，再传给Paint对象
	 *
	 * 同样的，ColorMatrix提供给我们相应的方法，setSaturation()就可以设置一个饱和度
	 */
	public static Bitmap ajustImage(Bitmap bitemap) {
		ColorMatrix cMatrix = new ColorMatrix();
		// int brightIndex = -25;
		// int doubleColor = 2;
		// cMatrix.set(new float[]{
		// doubleColor,0,0,0,brightIndex, //这里将1改为2则我们让Red的值为原来的两倍
		// 0,doubleColor,0,0,brightIndex,//改变最后一列的值，我们可以不改变RGB同道颜色的基础上，改变亮度
		// 0,0,doubleColor,0,brightIndex,
		// 0,0,0,doubleColor,0
		// });
		// cMatrix.setSaturation(2.0f);//设置饱和度
		cMatrix.setScale(2.0f, 2.0f, 2.0f, 2.0f);// 设置颜色同道色彩缩放
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		Bitmap bmp = null;
		// 下面这个Bitmap中创建的函数就可以创建一个空的Bitmap
		// 返回的是一个可以改变的Bitmap对象，这样我们后面就可以对其进行变换和颜色调整等操作了
		bmp = Bitmap.createBitmap(bitemap.getWidth(), bitemap.getHeight(),
				bitemap.getConfig());
		// 创建Canvas对象，
		Canvas canvas = new Canvas(bmp);

		// 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了

		canvas.drawBitmap(bitemap, 0, 0, paint);
		return bmp;
	}

	/**
	 * 把View绘制到Bitmap上 http://yunfeng.sinaapp.com/?p=228
	 *
	 * @param view
	 *            需要绘制的View
	 * @param width
	 *            该View的宽度
	 * @param height
	 *            该View的高度
	 * @return 返回Bitmap对象
	 */
	public static Bitmap getBitmapFromView(View view, int width, int height) {
		int widthSpec = View.MeasureSpec.makeMeasureSpec(width,
				View.MeasureSpec.EXACTLY);
		int heightSpec = View.MeasureSpec.makeMeasureSpec(height,
				View.MeasureSpec.EXACTLY);
		view.measure(widthSpec, heightSpec);
		view.layout(0, 0, width, height);
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	/**
	 *
	 * @param context
	 * @param resId
	 * @return Bitmap
	 */
	public Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opts);
	}

	/**
	 * 通过文件名找图片
	 * **/
	public static Bitmap GetBitmapByFileName(Context context, String fileName) {

		try {
			String packetName = context.getPackageName();
			// 将fileName 转换成id
			int resId = context.getResources().getIdentifier(fileName,
					"drawable", packetName);
			Bitmap bitmap = null;
			// InputStream im = context.getResources().openRawResource(resId);
			// bitmap = BitmapFactory.decodeStream(im);
			//
			// im.close();
			// im = null;
			bitmap = BitmapFactory
					.decodeResource(context.getResources(), resId);
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将一个view转换为bitmap
	 * **/
	public static Bitmap getBitmapFromView(View view) {
		Bitmap bitmap = null;
		try {
			view.destroyDrawingCache();
			view.measure(View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
					.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
			view.setDrawingCacheEnabled(true);
			bitmap = view.getDrawingCache(true);
		} catch (Exception e) {
		}
		return bitmap;
	}

	/**
	 * bitmap转为base64
	 *
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 *
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}
