package com.uroad.imageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.uroad.uroad_ctllib.R;
import com.uroad.util.HTTPUtil;

public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	ExecutorService executorService;
	int stub_id = R.drawable.base_cctv_nodata;
	boolean isDecode = true; // 默认压缩图片
	boolean isthumbnails = false;
	int thumbnailsWidth = 150;
	int thumbnailsHeight = 150;
	int REQUIRED_SIZE = 800;
	int radius = 0;
	boolean isRound = false;// 是否为圆形图片
	ImageView imgLoading;
	Context mContext;
	boolean isCache = true;// 是否缓存图片，默认缓存

	public ImageLoader(Context context) {
		mContext = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(10);
	}

	public ImageLoader(Context context, String dirName) {
		mContext = context;
		fileCache = new FileCache(context, dirName);
		executorService = Executors.newFixedThreadPool(10);
	}

	// 最主要的方法
	public void DisplayImage(String url, ImageView imageView) {
		// 先从内存缓存中查找
		// url = HTTPUtil.getUrlEncode(url);
		Bitmap bitmap = memoryCache.get(url);
		this.isDecode = false;
		this.isthumbnails = false;
		isRound = false;
		isCache = true;
		REQUIRED_SIZE = 800;
		if (bitmap != null) {
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageBitmap(bitmap);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);

			imageView.setImageResource(stub_id);
		}

	}

	/**
	 * resId 设置默认加载图片 isDecode 是否压缩图片 （默认压缩图片）
	 * **/
	// 最主要的方法
	public void DisplayImage(String url, ImageView imageView, int resId,
							 boolean isDecode) {
		// url = HTTPUtil.getUrlEncode(url);
		stub_id = resId;
		this.isDecode = isDecode;
		this.isthumbnails = false;
		isRound = false;
		isCache = true;
		REQUIRED_SIZE = 800;
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageBitmap(bitmap);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}

	}

	/**
	 * resId 设置默认加载图片 isDecode 是否压缩图片 （默认压缩图片）
	 * **/
	// 最主要的方法
	public void DisplayImageNoCache(String url, ImageView imageView, int resId,
									boolean isDecode) {
		stub_id = resId;
		this.isDecode = isDecode;
		this.isthumbnails = false;
		isRound = false;
		isCache = false;
		REQUIRED_SIZE = 800;
		// 直接下载
		queuePhoto(url, imageView);
		imageView.setImageResource(stub_id);

	}

	/**
	 * resId 设置默认加载图片 isDecode 是否压缩图片 （默认压缩图片）
	 *
	 * @param required_size
	 *            :设置压缩图片的质量（默认为800）
	 * **/
	// 最主要的方法
	public void DisplayImage(String url, ImageView imageView, int resId,
							 boolean isDecode, int required_size) {
		url = HTTPUtil.getUrlEncode(url);
		stub_id = resId;
		this.isDecode = isDecode;
		this.isthumbnails = false;
		isRound = false;
		isCache = true;
		REQUIRED_SIZE = 800;
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageBitmap(bitmap);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}

	}

	/**
	 * @param resId
	 *            :添加默认图片
	 * @param imgLoading
	 *            :加载时图片
	 * **/
	public void DisplayImage(String url, ImageView imageView, int resId,
							 ImageView imgLoad) {
		// 先从内存缓存中查找
		stub_id = resId;
		imgLoading = imgLoad;
		this.isDecode = false;
		this.isthumbnails = false;
		isRound = false;
		isCache = true;
		REQUIRED_SIZE = 800;
		imgLoading.setVisibility(View.VISIBLE);
		imageView.setVisibility(View.INVISIBLE);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			imgLoading.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
			imageView.setScaleType(ScaleType.FIT_XY);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}

	}

	/**
	 * isDecode :是否压缩（默认压缩） isthumbnails ：是否为缩略图（默认不是缩略图） width ：缩略图时的宽 height
	 * ：缩略图时的高
	 * **/
	public void DisplayImage(String url, ImageView imageView, int resId,
							 boolean isDecode, boolean isthumbnails, int width, int height) {
		url = HTTPUtil.getUrlEncode(url);
		stub_id = resId;
		this.isDecode = isDecode;
		this.isthumbnails = isthumbnails;
		isRound = false;
		isCache = true;
		REQUIRED_SIZE = 800;
		if (width > 0 && height > 0) {
			this.thumbnailsWidth = width;
			this.thumbnailsHeight = height;
		}
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			imageView.setScaleType(ScaleType.FIT_XY);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}

	}

	public void DisplayImage(String url, ImageView imageView, int resId,
							 int radius) {
		url = HTTPUtil.getUrlEncode(url);
		this.isDecode = false;
		this.isthumbnails = false;
		stub_id = resId;
		isRound = true;
		isCache = true;
		this.radius = radius;
		REQUIRED_SIZE = 800;
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			Bitmap bm = BitMapUtil.getCroppedRoundBitmap(bitmap, radius);
			imageView.setImageBitmap(bm);
			imageView.setScaleType(ScaleType.FIT_XY);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	public void DisplayImage(String url, ImageView imageView, int resId,
							 int radius, boolean isDecode) {
		url = HTTPUtil.getUrlEncode(url);
		this.isDecode = isDecode;
		this.isthumbnails = false;
		stub_id = resId;
		isRound = true;
		isCache = true;
		this.radius = radius;
		REQUIRED_SIZE = 800;
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			Bitmap bm = BitMapUtil.getCroppedRoundBitmap(bitmap, radius);
			imageView.setImageBitmap(bm);
			imageView.setScaleType(ScaleType.FIT_XY);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	public void DisplayImage(String url, ImageView imageView, int resId,
							 int radius, boolean isthumbnails, int width, int height) {
		url = HTTPUtil.getUrlEncode(url);
		this.isDecode = isDecode;
		this.isthumbnails = isthumbnails;
		stub_id = resId;
		isRound = true;
		isCache = true;
		this.radius = radius;
		REQUIRED_SIZE = 800;
		if (width > 0 && height > 0) {
			this.thumbnailsWidth = width;
			this.thumbnailsHeight = height;
		}
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			Bitmap bm = BitMapUtil.getCroppedRoundBitmap(bitmap, radius);
			imageView.setImageBitmap(bm);
			imageView.setScaleType(ScaleType.FIT_XY);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);
		if (isCache) {
			// 先从文件缓存中查找是否有
			Bitmap b = decodeFile(f);
			if (b != null)
				return b;
		}
		// 最后从指定的url中下载图片
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setRequestMethod("GET");
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setRequestProperty("Content-Type",
//					"image/*");

			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			if (isCache) {
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f);
			} else {
				byte[] data = readInputStream(is);
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			}
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*
	 * 从数据流中获得数据
	 */
	private static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();

	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();

			if (isDecode) {
				o2.inSampleSize = scale;
				return BitmapFactory.decodeStream(new FileInputStream(f), null,
						o2);
			} else {
				Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(f),
						null, null);
				return bm;
			}
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", e.getMessage());
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			Bitmap bmp = getBitmap(photoToLoad.url);
			if (isCache) {
				memoryCache.put(photoToLoad.url, bmp);
			}
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 *
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (bitmap != null) {
				photoToLoad.imageView.setVisibility(View.VISIBLE);
				if (imgLoading != null) {
					imgLoading.setVisibility(View.GONE);
				}
				if (isthumbnails) {
					photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);
					photoToLoad.imageView.setImageBitmap(ThumbnailUtils
							.extractThumbnail(bitmap, thumbnailsWidth,
									thumbnailsHeight));

				} else if (isRound) {// 是否为圆形tup
					Bitmap bm = BitMapUtil
							.getCroppedRoundBitmap(bitmap, radius);
					photoToLoad.imageView.setImageBitmap(bm);

				} else {
					photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);
					photoToLoad.imageView.setImageBitmap(bitmap);
				}
			} else {
				if (isRound) {
					stub_id = R.drawable.icon_service_nodata;

					Bitmap bitmap = BitMapUtil.drawable2Bitmap(mContext
							.getResources().getDrawable(stub_id));
					Bitmap bm = BitMapUtil
							.getCroppedRoundBitmap(bitmap, radius);
					photoToLoad.imageView.setImageBitmap(bm);
				} else {
					photoToLoad.imageView.setImageResource(stub_id);
				}
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public void remove(String url) {
		memoryCache.remove(url);
		fileCache.remove(url);
	}

	public String getCacheSize() {
		return fileCache.ComputeAppCache(mContext);
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

}