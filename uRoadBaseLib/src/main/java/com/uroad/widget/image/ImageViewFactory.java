/**
 * @Title: ImageViewFactory.java
 * @Package com.uroad.image
 * @Description: 图片的工厂类，负责管理图片的缓存，加载
 * @author oupy
 * @date 2013-7-29 上午12:03:52
 * @version V1.0
 */
package com.uroad.widget.image;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

/**
 * todo 验证一下 目前所用 线程 管理是不是最优方案 todo 优化网络，优化显示，优化一切
 *
 * @author Administrator 负责管理图片的加载 显示 回收 缓存的使用 清除
 */

public class ImageViewFactory {

	private VCBitmapFactoryConfig mConfig;
	private static BitmapCache mImageCache;

	private boolean mExitTasksEarly = false;
	private boolean mPauseWork = false;
	private final Object mPauseWorkLock = new Object();
	private Context mContext;

	private static ExecutorService bitmapLoadAndDisplayExecutor;

	private static ImageViewFactory mVCBitmapFactory;

	// //////////////////////// config method
	// todo 缓存的策略要控制一下
	// start////////////////////////////////////
	private ImageViewFactory(Context context) {
		mContext = context;
		mConfig = new VCBitmapFactoryConfig(context);

		configDiskCachePath(ImageUtils.getDiskCacheDir(context, "bitmapCache1")
				.getAbsolutePath());// 配置缓存路径
		configDisplayer(new SimpleDisplayer());// 配置显示器
		configDownlader(new SimpleHttpDownloader());// 配置下载器
	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @return
	 */
	public static ImageViewFactory create(Context ctx) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.init();
		}
		return mVCBitmapFactory;
	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @return
	 */
	public static ImageViewFactory create(Context ctx, String diskCachePath) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.configDiskCachePath(diskCachePath);
			mVCBitmapFactory.init();
		}
		return mVCBitmapFactory;

	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSizePercent
	 *            缓存大小在当前进程的百分比（0.05-0.8之间）
	 * @return
	 */
	public static ImageViewFactory create(Context ctx, String diskCachePath,
										  float memoryCacheSizePercent) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.configDiskCachePath(diskCachePath);
			mVCBitmapFactory.configMemoryCachePercent(memoryCacheSizePercent);
			mVCBitmapFactory.init();
		}

		return mVCBitmapFactory;
	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSize
	 *            内存缓存大小
	 * @return
	 */
	public static ImageViewFactory create(Context ctx, String diskCachePath,
										  int memoryCacheSize) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.configDiskCachePath(diskCachePath);
			mVCBitmapFactory.configMemoryCacheSize(memoryCacheSize);
			mVCBitmapFactory.init();
		}

		return mVCBitmapFactory;
	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSizePercent
	 *            缓存大小在当前进程的百分比（0.05-0.8之间）
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static ImageViewFactory create(Context ctx, String diskCachePath,
										  float memoryCacheSizePercent, int threadSize) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.configDiskCachePath(diskCachePath);
			mVCBitmapFactory.configBitmapLoadThreadSize(threadSize);
			mVCBitmapFactory.configMemoryCachePercent(memoryCacheSizePercent);
			mVCBitmapFactory.init();
		}

		return mVCBitmapFactory;
	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSize
	 *            内存缓存大小
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static ImageViewFactory create(Context ctx, String diskCachePath,
										  int memoryCacheSize, int threadSize) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.configDiskCachePath(diskCachePath);
			mVCBitmapFactory.configBitmapLoadThreadSize(threadSize);
			mVCBitmapFactory.configMemoryCacheSize(memoryCacheSize);
			mVCBitmapFactory.init();
		}

		return mVCBitmapFactory;
	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSizePercent
	 *            缓存大小在当前进程的百分比（0.05-0.8之间）
	 * @param diskCacheSize
	 *            磁盘缓存大小
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static ImageViewFactory create(Context ctx, String diskCachePath,
										  float memoryCacheSizePercent, int diskCacheSize, int threadSize) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.configDiskCachePath(diskCachePath);
			mVCBitmapFactory.configBitmapLoadThreadSize(threadSize);
			mVCBitmapFactory.configMemoryCachePercent(memoryCacheSizePercent);
			mVCBitmapFactory.configDiskCacheSize(diskCacheSize);
			mVCBitmapFactory.init();
		}

		return mVCBitmapFactory;
	}

	/**
	 * 创建VCBitmapFactory
	 *
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSize
	 *            内存缓存大小
	 * @param diskCacheSize
	 *            磁盘缓存大小
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static ImageViewFactory create(Context ctx, String diskCachePath,
										  int memoryCacheSize, int diskCacheSize, int threadSize) {
		if (mVCBitmapFactory == null) {
			mVCBitmapFactory = new ImageViewFactory(ctx.getApplicationContext());
			mVCBitmapFactory.configDiskCachePath(diskCachePath);
			mVCBitmapFactory.configBitmapLoadThreadSize(threadSize);
			mVCBitmapFactory.configMemoryCacheSize(memoryCacheSize);
			mVCBitmapFactory.configDiskCacheSize(diskCacheSize);
			mVCBitmapFactory.init();
		}

		return mVCBitmapFactory;
	}

	/**
	 * 设置图片正在加载的时候显示的图片
	 *
	 * @param bitmap
	 */
	public ImageViewFactory configLoadingImage(Bitmap bitmap) {
		mConfig.defaultDisplayConfig.setLoadingBitmap(bitmap);
		return this;
	}

	/**
	 * 设置图片正在加载的时候显示的图片
	 *
	 * @param bitmap
	 */
	public ImageViewFactory configLoadingImage(int resId) {
		mConfig.defaultDisplayConfig.setLoadingBitmap(BitmapFactory
				.decodeResource(mContext.getResources(), resId));
		return this;
	}

	/**
	 * 设置图片加载失败时候显示的图片
	 *
	 * @param bitmap
	 */
	public ImageViewFactory configLoadfailImage(Bitmap bitmap) {
		mConfig.defaultDisplayConfig.setLoadfailBitmap(bitmap);
		return this;
	}

	/**
	 * 设置设置是否有动画效果
	 *
	 * @param bitmap
	 */
	public ImageViewFactory configLoadAnim(boolean load) {
		mConfig.defaultDisplayConfig.setHasAnimation(load);
		return this;
	}

	/**
	 * 设置图片加载失败时候显示的图片
	 *
	 * @param resId
	 */
	public ImageViewFactory configLoadfailImage(int resId) {
		mConfig.defaultDisplayConfig.setLoadfailBitmap(BitmapFactory
				.decodeResource(mContext.getResources(), resId));
		return this;
	}

	/**
	 * 配置默认图片的小的高度
	 *
	 * @param bitmapHeight
	 */
	public ImageViewFactory configBitmapMaxHeight(int bitmapHeight) {
		mConfig.defaultDisplayConfig.setBitmapHeight(bitmapHeight);
		return this;
	}

	/**
	 * 配置默认图片的小的宽度
	 *
	 * @param bitmapHeight
	 */
	public ImageViewFactory configBitmapMaxWidth(int bitmapWidth) {
		mConfig.defaultDisplayConfig.setBitmapWidth(bitmapWidth);
		return this;
	}

	/**
	 * 设置下载器，比如通过ftp或者其他协议去网络读取图片的时候可以设置这项
	 *
	 * @param downlader
	 * @return
	 */
	public ImageViewFactory configDownlader(Downloader downlader) {
		mConfig.downloader = downlader;
		return this;
	}

	/**
	 * 设置显示器，比如在显示的过程中显示动画等
	 *
	 * @param displayer
	 * @return
	 */
	public ImageViewFactory configDisplayer(Displayer displayer) {
		mConfig.displayer = displayer;
		return this;
	}

	/**
	 * 配置 加载图片的时候是否计算图片大小，如果配置为真，则decode图片的时候可能会造成out of memory的异常
	 *
	 * @param neverCalculate
	 *            是否decode的时候不计算图片大小
	 */
	public ImageViewFactory configCalculateBitmapSizeWhenDecode(
			boolean neverCalculate) {
		if (mConfig != null && mConfig.bitmapProcess != null)
			mConfig.bitmapProcess.configCalculateBitmap(neverCalculate);
		return this;
	}

	/**
	 * 配置磁盘缓存路径
	 *
	 * @param strPath
	 * @return
	 */
	private ImageViewFactory configDiskCachePath(String strPath) {
		if (!TextUtils.isEmpty(strPath)) {
			mConfig.cachePath = strPath;
		}
		return this;
	}

	/**
	 * 配置内存缓存大小 大于2MB以上有效
	 *
	 * @param size
	 *            缓存大小
	 */
	private ImageViewFactory configMemoryCacheSize(int size) {
		mConfig.memCacheSize = size;
		return this;
	}

	/**
	 * 设置应缓存的在APK总内存的百分比，优先级大于configMemoryCacheSize
	 *
	 * @param percent
	 *            百分比，值的范围是在 0.05 到 0.8之间
	 */
	private ImageViewFactory configMemoryCachePercent(float percent) {
		mConfig.memCacheSizePercent = percent;
		return this;
	}

	/**
	 * 设置磁盘缓存大小 5MB 以上有效
	 *
	 * @param size
	 */
	private ImageViewFactory configDiskCacheSize(int size) {
		mConfig.diskCacheSize = size;
		return this;
	}

	/**
	 * 设置加载图片的线程并发数量
	 *
	 * @param size
	 */
	private ImageViewFactory configBitmapLoadThreadSize(int size) {
		if (size >= 1)
			mConfig.poolSize = size;
		return this;
	}

	/**
	 * 这个方法必须被调用后 VCBitmapFactory 配置才能有效
	 *
	 * @return
	 */
	private ImageViewFactory init() {

		mConfig.init();

		BitmapCache.ImageCacheParams imageCacheParams = new BitmapCache.ImageCacheParams(
				mConfig.cachePath);
		if (mConfig.memCacheSizePercent > 0.05
				&& mConfig.memCacheSizePercent < 0.8) {
			imageCacheParams.setMemCacheSizePercent(mContext,
					mConfig.memCacheSizePercent);
		} else {
			if (mConfig.memCacheSize > 1024 * 1024 * 2) {
				imageCacheParams.setMemCacheSize(mConfig.memCacheSize);
			} else {
				// 设置默认的内存缓存大小
				imageCacheParams.setMemCacheSizePercent(mContext, 0.3f);
			}
		}
		if (mConfig.diskCacheSize > 1024 * 1024 * 5)
			imageCacheParams.setDiskCacheSize(mConfig.diskCacheSize);
		mImageCache = new BitmapCache(imageCacheParams);

		bitmapLoadAndDisplayExecutor = Executors
				.newFixedThreadPool(mConfig.poolSize);

		new CacheExecutecTask()
				.execute(CacheExecutecTask.MESSAGE_INIT_DISK_CACHE);

		return this;
	}

	public ImageViewFactory inits() {

		mConfig.init();

		BitmapCache.ImageCacheParams imageCacheParams = new BitmapCache.ImageCacheParams(
				mConfig.cachePath);
		if (mConfig.memCacheSizePercent > 0.05
				&& mConfig.memCacheSizePercent < 0.8) {
			imageCacheParams.setMemCacheSizePercent(mContext,
					mConfig.memCacheSizePercent);
		} else {
			if (mConfig.memCacheSize > 1024 * 1024 * 2) {
				imageCacheParams.setMemCacheSize(mConfig.memCacheSize);
			} else {
				// 设置默认的内存缓存大小
				imageCacheParams.setMemCacheSizePercent(mContext, 0.3f);
			}
		}
		if (mConfig.diskCacheSize > 1024 * 1024 * 5)
			imageCacheParams.setDiskCacheSize(mConfig.diskCacheSize);
		mImageCache = new BitmapCache(imageCacheParams);

		bitmapLoadAndDisplayExecutor = Executors
				.newFixedThreadPool(mConfig.poolSize);

		new CacheExecutecTask()
				.execute(CacheExecutecTask.MESSAGE_INIT_DISK_CACHE);

		return this;
	}

	public BitmapCache getBitmapCache() {
		return mImageCache;
	}

	// //////////////////////// config method 默认使用sd缓存，只有指定明用内存缓存才使用内存
	// end////////////////////////////////////

	// /*
	// * 从网络加载图片的方法，
	// */
	// public void display(ImageView imageView, String uri) {
	// doDisplay(imageView, uri, null);
	// }
	//
	//
	// /*
	// * 从网络加载图片的方法，
	// */
	// public void display(ImageView imageView, String uri,
	// boolean isUseMemoryCache) {
	// BitmapDisplayConfig displayConfig = getDisplayConfig();
	// displayConfig.setIsUseMemoryCache(isUseMemoryCache);
	// doDisplay(imageView, uri, displayConfig);
	// }
	//
	// public void display(ImageView imageView, String uri, int imageWidth,
	// int imageHeight, boolean isUseMemoryCache) {
	// BitmapDisplayConfig displayConfig = configMap.get(imageWidth + "_"
	// + imageHeight);
	// if (displayConfig == null) {
	// displayConfig = getDisplayConfig();
	// displayConfig.setBitmapHeight(imageHeight);
	// displayConfig.setBitmapWidth(imageWidth);
	// displayConfig.setIsUseMemoryCache(isUseMemoryCache);
	// configMap.put(imageWidth + "_" + imageHeight, displayConfig);
	// }
	//
	// doDisplay(imageView, uri, displayConfig);
	// }
	//
	// public void display(ImageView imageView, String uri, Bitmap
	// loadingBitmap,
	// boolean isUseMemoryCache) {
	// BitmapDisplayConfig displayConfig = configMap.get(String
	// .valueOf(loadingBitmap));
	// if (displayConfig == null) {
	// displayConfig = getDisplayConfig();
	// displayConfig.setLoadingBitmap(loadingBitmap);
	// displayConfig.setIsUseMemoryCache(isUseMemoryCache);
	// configMap.put(String.valueOf(loadingBitmap), displayConfig);
	// }
	//
	// doDisplay(imageView, uri, displayConfig);
	// }
	//
	// public void display(ImageView imageView, String uri, Bitmap
	// loadingBitmap,
	// Bitmap laodfailBitmap, boolean isUseMemoryCache) {
	// BitmapDisplayConfig displayConfig = configMap.get(String
	// .valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap));
	// if (displayConfig == null) {
	// displayConfig = getDisplayConfig();
	// displayConfig.setLoadingBitmap(loadingBitmap);
	// displayConfig.setLoadfailBitmap(laodfailBitmap);
	// displayConfig.setIsUseMemoryCache(isUseMemoryCache);
	// configMap.put(
	// String.valueOf(loadingBitmap) + "_"
	// + String.valueOf(laodfailBitmap), displayConfig);
	// }
	//
	// doDisplay(imageView, uri, displayConfig);
	// }
	//
	// public void display(ImageView imageView, String uri, int imageWidth,
	// int imageHeight, Bitmap loadingBitmap, Bitmap laodfailBitmap,
	// boolean isUseMemoryCache) {
	// BitmapDisplayConfig displayConfig = configMap.get(imageWidth + "_"
	// + imageHeight + "_" + String.valueOf(loadingBitmap) + "_"
	// + String.valueOf(laodfailBitmap));
	// if (displayConfig == null) {
	// displayConfig = getDisplayConfig();
	// displayConfig.setBitmapHeight(imageHeight);
	// displayConfig.setBitmapWidth(imageWidth);
	// displayConfig.setLoadingBitmap(loadingBitmap);
	// displayConfig.setLoadfailBitmap(laodfailBitmap);
	// displayConfig.setIsUseMemoryCache(isUseMemoryCache);
	// configMap.put(
	// imageWidth + "_" + imageHeight + "_"
	// + String.valueOf(loadingBitmap) + "_"
	// + String.valueOf(laodfailBitmap), displayConfig);
	// }
	//
	// doDisplay(imageView, uri, displayConfig);
	// }

	public void display(UroadImageView imageView, String uri,
						BitmapDisplayConfig config) {
		// uri = HTTPUtil.getUrlEncode(uri);
		doDisplay(imageView, uri, config);
	}

	public void doDisplayByAsset(ImageView imageView, String uri) {
		// Bitmap bitmap = null;

		// if (mImageCache != null) {
		// bitmap = mImageCache.getBitmapFromMemCache(uri);
		// }
		// mImageCache.clearMemoryCache();

		if (checkImageTask(uri, imageView)) {

			BitmapLoadAssetAndDisplayTask task = new BitmapLoadAssetAndDisplayTask(
					imageView, mConfig.defaultDisplayConfig);

			task.execute(uri);

		}
	}

	public void doDisplayBySD(ImageView imageView, String uri, int scale,
							  boolean loadAnim) {
		Bitmap bitmap = null;

		if (mImageCache != null && scale > 1) {
			bitmap = mImageCache.getBitmapFromCache(uri);
		}
		// mImageCache.clearMemoryCache();

		if (bitmap != null && !bitmap.isRecycled()) {
			// recycle(imageView);
			imageView.setImageBitmap(bitmap);

		} else if (checkImageTask(uri, imageView)) {

			BitmapLoadSDAndDisplayTask task = new BitmapLoadSDAndDisplayTask(
					imageView, mConfig.defaultDisplayConfig, scale, loadAnim);
			task.execute(uri);

		}
	}

	private void doDisplay(UroadImageView uv, final String uri,
						   BitmapDisplayConfig displayConfig) {
		if (TextUtils.isEmpty(uri) || uv == null) {
			return;
		}

		ImageView imageView = uv.getImageView();

		if (displayConfig == null)
			displayConfig = mConfig.defaultDisplayConfig;

		Bitmap bitmap = null;

		if (mImageCache != null && displayConfig.getIsUseCache()) {
			bitmap = mImageCache.getBitmapFromCache(uri);
		}

		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else if (checkImageTask(uri, imageView)) {

			final BitmapLoadAndDisplayTask task = new BitmapLoadAndDisplayTask(
					uv, displayConfig);
			// 设置默认图片
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					mContext.getResources(), displayConfig.getLoadingBitmap(),
					task);
			imageView.setImageDrawable(asyncDrawable);
			// imageView.setScaleType(ScaleType.FIT_XY);
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// TODO Auto-generated method stub
			task.executeOnExecutor(bitmapLoadAndDisplayExecutor, uri);
			// }
			// });
		}
	}

	// private HashMap<String, BitmapDisplayConfig> configMap = new
	// HashMap<String, BitmapDisplayConfig>();

	public BitmapDisplayConfig getDisplayConfig() {
		BitmapDisplayConfig config = new BitmapDisplayConfig();
		config.setAnimation(mConfig.defaultDisplayConfig.getAnimation());
		config.setAnimationType(mConfig.defaultDisplayConfig.getAnimationType());
		config.setBitmapHeight(mConfig.defaultDisplayConfig.getBitmapHeight());
		config.setBitmapWidth(mConfig.defaultDisplayConfig.getBitmapWidth());
		config.setLoadfailBitmap(mConfig.defaultDisplayConfig
				.getLoadfailBitmap());

		config.setLoadingBitmap(mConfig.defaultDisplayConfig.getLoadingBitmap());
		return config;
	}

	private void initDiskCacheInternalInBackgroud() {
		if (mImageCache != null) {
			mImageCache.initDiskCache();
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.initHttpDiskCache();
		}
	}

	private void clearCacheInternalInBackgroud() {
		if (mImageCache != null) {
			mImageCache.clearCache();
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.clearCacheInternal();
		}
	}

	private void clearMemoryCacheInBackgroud() {
		if (mImageCache != null) {
			mImageCache.clearMemoryCache();
		}
	}

	private void clearDiskCacheInBackgroud() {
		if (mImageCache != null) {
			mImageCache.clearDiskCache();
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.clearCacheInternal();
		}
	}

	private void clearCacheInBackgroud(String key) {
		if (mImageCache != null) {
			mImageCache.clearCache(key);
		}
	}

	private void clearDiskCacheInBackgroud(String key) {
		if (mImageCache != null) {
			mImageCache.clearDiskCache(key);
		}
	}

	private void clearMemoryCacheInBackgroud(String key) {
		if (mImageCache != null) {
			mImageCache.clearMemoryCache(key);
		}
	}

	private void flushCacheInternalInBackgroud() {
		if (mImageCache != null) {
			mImageCache.flush();
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.flushCacheInternal();
		}
	}

	private void closeCacheInternalInBackgroud() {
		if (mImageCache != null) {
			mImageCache.close();
			mImageCache = null;
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.clearCacheInternal();
		}
	}

	/**
	 * 网络加载bitmap
	 *
	 * @param data
	 * @return
	 */
	private Bitmap processBitmap(String uri, BitmapDisplayConfig config) {
		if (mConfig != null && mConfig.bitmapProcess != null) {
			return mConfig.bitmapProcess.processBitmap(uri, config);
		}
		return null;
	}

	// /**
	// * 网络加载bitmap
	// *
	// * @param data
	// * @return
	// */
	// private Bitmap processBitmap(String uri, BitmapDisplayConfig config,
	// boolean calculate) {
	// if (mConfig != null && mConfig.bitmapProcess != null) {
	// try {
	// return mConfig.bitmapProcess.processBitmap(uri, config,
	// calculate);
	// } catch (OutOfMemoryError e) {
	// throw e;
	// }
	// }
	// return null;
	// }

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
	}

	/**
	 * activity onResume的时候调用这个方法，让加载图片线程继续
	 */
	public void onResume() {
		setExitTasksEarly(false);
	}

	/**
	 * activity onPause的时候调用这个方法，让线程暂停
	 */
	public void onPause() {
		setExitTasksEarly(true);
		// flushCache();
	}

	/**
	 * activity onDestroy的时候调用这个方法，释放缓存
	 */
	public void onDestroy() {
		closeCache();
	}

	public Bitmap getByCache(String url) {
		if (mImageCache != null) {
			Bitmap bitmap = mImageCache.getBitmapFromCache(url);
			return bitmap;
		}
		return null;
	}

	/**
	 * 清除所有缓存（磁盘和内存）
	 */
	public void clearCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR);
	}

	/**
	 * 根据key清除指定的内存缓存
	 *
	 * @param key
	 */
	public void clearCache(String key) {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR_KEY,
				key);
	}

	/**
	 * 清除缓存
	 */
	public void clearMemoryCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR_MEMORY);
	}

	/**
	 * 根据key清除指定的内存缓存
	 *
	 * @param key
	 */
	public void clearMemoryCache(String key) {
		new CacheExecutecTask().execute(
				CacheExecutecTask.MESSAGE_CLEAR_KEY_IN_MEMORY, key);
	}

	/**
	 * 清除磁盘缓存
	 */
	public void clearDiskCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR_DISK);
	}

	/**
	 * 根据key清除指定的内存缓存
	 *
	 * @param key
	 */
	public void clearDiskCache(String key) {
		new CacheExecutecTask().execute(
				CacheExecutecTask.MESSAGE_CLEAR_KEY_IN_DISK, key);
	}

	/**
	 * 刷新缓存
	 */
	public void flushCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_FLUSH);
	}

	/**
	 * 关闭缓存
	 */
	public void closeCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLOSE);
	}

	/**
	 * 退出正在加载的线程，程序退出的时候调用词方法
	 *
	 * @param exitTasksEarly
	 */
	public void exitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
		if (exitTasksEarly)
			pauseWork(false);// 让暂停的线程结束
	}

	/**
	 * 暂停正在加载的线程，监听listview或者gridview正在滑动的时候条用词方法
	 *
	 * @param pauseWork
	 *            true停止暂停线程，false继续线程
	 */
	public void pauseWork(boolean pauseWork) {
		synchronized (mPauseWorkLock) {
			mPauseWork = pauseWork;
			if (!mPauseWork) {
				mPauseWorkLock.notifyAll();
			}
		}
	}

	private static BitmapLoadAndDisplayTask getBitmapTaskFromImageView(
			ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * 检测 imageView中是否已经有线程在运行
	 *
	 * @param data
	 * @param imageView
	 * @return true 没有 false 有线程在运行了
	 */
	public boolean checkImageTask(Object data, ImageView imageView) {
		final BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

		if (bitmapWorkerTask != null) {
			final Object bitmapData = bitmapWorkerTask.data;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.cancel(true);
			} else {
				// 同一个线程已经在执行
				return false;
			}
		}
		return true;
	}

	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapLoadAndDisplayTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
							 BitmapLoadAndDisplayTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapLoadAndDisplayTask>(
					bitmapWorkerTask);
		}

		public BitmapLoadAndDisplayTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	/**
	 * @title 缓存操作的异步任务
	 * @description 操作缓存
	 * @company 探索者网络工作室(www.tsz.net)
	 * @author michael Young (www.YangFuhai.com)
	 * @version 1.0
	 * @created 2012-10-28
	 */
	private class CacheExecutecTask extends ImageAsyncTask<Object, Void, Void> {
		public static final int MESSAGE_CLEAR = 0;
		public static final int MESSAGE_INIT_DISK_CACHE = 1;
		public static final int MESSAGE_FLUSH = 2;
		public static final int MESSAGE_CLOSE = 3;
		public static final int MESSAGE_CLEAR_MEMORY = 4;
		public static final int MESSAGE_CLEAR_DISK = 5;
		public static final int MESSAGE_CLEAR_KEY = 6;
		public static final int MESSAGE_CLEAR_KEY_IN_MEMORY = 7;
		public static final int MESSAGE_CLEAR_KEY_IN_DISK = 8;

		@Override
		protected Void doInBackground(Object... params) {
			switch ((Integer) params[0]) {
				case MESSAGE_CLEAR:
					clearCacheInternalInBackgroud();
					break;
				case MESSAGE_INIT_DISK_CACHE:
					initDiskCacheInternalInBackgroud();
					break;
				case MESSAGE_FLUSH:
					clearMemoryCacheInBackgroud();
					flushCacheInternalInBackgroud();
					break;
				case MESSAGE_CLOSE:
					clearMemoryCacheInBackgroud();
					closeCacheInternalInBackgroud();
				case MESSAGE_CLEAR_MEMORY:
					clearMemoryCacheInBackgroud();
					break;
				case MESSAGE_CLEAR_DISK:
					clearDiskCacheInBackgroud();
					break;
				case MESSAGE_CLEAR_KEY:
					clearCacheInBackgroud(String.valueOf(params[1]));
					break;
				case MESSAGE_CLEAR_KEY_IN_MEMORY:
					clearMemoryCacheInBackgroud(String.valueOf(params[1]));
					break;
				case MESSAGE_CLEAR_KEY_IN_DISK:
					clearDiskCacheInBackgroud(String.valueOf(params[1]));
					break;
			}
			return null;
		}
	}

	/**
	 * bitmap下载显示的线程
	 *
	 * @author michael yang
	 */
	private class BitmapLoadAndDisplayTask extends
			ImageAsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<ImageView> imageViewReference;
		private final BitmapDisplayConfig displayConfig;
		private UroadImageView uImageView;

		public BitmapLoadAndDisplayTask(UroadImageView imageView,
										BitmapDisplayConfig config) {
			imageViewReference = new WeakReference<ImageView>(
					imageView.getImageView());
			displayConfig = config;
			uImageView = imageView;
		}

		@Override
		protected void onPreExecute() {
			if (displayConfig.getShowLoading()) {
				uImageView.setLoading();
			}
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			Log.e("doInBackground", (String) params[0]);
			data = params[0];
			final String dataString = String.valueOf(data);
			Bitmap bitmap = null;

			synchronized (mPauseWorkLock) {
				while (mPauseWork && !isCancelled()) {
					try {
						mPauseWorkLock.wait();
					} catch (InterruptedException e) {
					}
				}
			}

			// <<<<<<< .mine
			// if (mImageCache != null && !isCancelled()
			// && getAttachedImageView() != null && !mExitTasksEarly) {
			// bitmap = mImageCache.getBitmapFromDiskCache(dataString);
			// }

			if (mImageCache != null && !isCancelled()
					&& getAttachedImageView() != null && !mExitTasksEarly
					&& displayConfig.getIsUseCache()) {
				bitmap = mImageCache.getBitmapFromDiskCache(dataString);
			}

			if (bitmap == null && !isCancelled() && !mExitTasksEarly) {
				bitmap = processBitmap(dataString, displayConfig);
			}

			if (bitmap != null && mImageCache != null) {
				if (displayConfig.getIsUseCache()) {
					if (displayConfig.getIsUseMemoryCache()) {
						mImageCache.addBitmapToMemoryCache(dataString, bitmap);
					} else {
						mImageCache.addBitmapToDiskCache(dataString, bitmap);
					}
				}
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled() || mExitTasksEarly) {
				bitmap = null;
			}
			if (displayConfig.getShowLoading()) {
				uImageView.setEndLoading();
			}
			// 判断线程和当前的imageview是否是匹配
			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				mConfig.displayer.loadCompletedisplay(imageView, bitmap,
						displayConfig);
			} else if (bitmap == null && imageView != null) {
				mConfig.displayer.loadFailDisplay(imageView,
						displayConfig.getLoadfailBitmap());
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			super.onCancelled(bitmap);
			if (displayConfig.getShowLoading()) {
				uImageView.setEndLoading();
			}
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		/**
		 * 获取线程匹配的imageView,防止出现闪动的现象
		 *
		 * @return
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			final BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/**
	 * bitmap下载显示的线程
	 *
	 * @author michael yang
	 */
	private class BitmapLoadAssetAndDisplayTask extends
			ImageAsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<ImageView> imageViewReference;
		private final BitmapDisplayConfig displayConfig;

		public BitmapLoadAssetAndDisplayTask(ImageView imageView,
											 BitmapDisplayConfig config) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			displayConfig = config;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			data = params[0];
			Bitmap bitmap = null;

			AssetManager am = mContext.getResources().getAssets();
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inSampleSize =2;

				InputStream is = am.open(String.valueOf(data));
				bitmap = ImageUtils.getUnErrorBitmap(is, options);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (displayConfig.getIsUseCache()) {
				if (displayConfig.getIsUseMemoryCache()) {
					mImageCache.addBitmapToMemoryCache(String.valueOf(data),
							bitmap);
				} else {
					mImageCache.addBitmapToDiskCache(String.valueOf(data),
							bitmap);
				}
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled() || mExitTasksEarly) {
				bitmap = null;
			}

			// 判断线程和当前的imageview是否是匹配
			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				mConfig.displayer.loadCompletedisplay(imageView, bitmap,
						displayConfig);
			} else if (bitmap == null && imageView != null) {
				mConfig.displayer.loadFailDisplay(imageView,
						displayConfig.getLoadfailBitmap());
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			super.onCancelled(bitmap);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		/**
		 * 获取线程匹配的imageView,防止出现闪动的现象
		 *
		 * @return
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			// final BitmapLoadAssetAndDisplayTask bitmapWorkerTask =
			// getBitmapTaskFromImageView(imageView);

			// if (this == bitmapWorkerTask) {
			return imageView;
			// }

			// return null;
		}
	}

	private class BitmapLoadSDAndDisplayTask extends
			ImageAsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<ImageView> imageViewReference;
		private final BitmapDisplayConfig displayConfig;
		private int scale = 1;
		private final boolean loadAnim;

		public BitmapLoadSDAndDisplayTask(ImageView imageView,
										  BitmapDisplayConfig config, int scale, boolean loadAnim) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			displayConfig = config;
			this.scale = scale;
			this.loadAnim = loadAnim;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			data = params[0];
			Bitmap bitmap = null;

			try {

				BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(String.valueOf(data), options);
				// options.inSampleSize = computeSampleSize(options, -1, 128 *
				// 128);

				options.inPreferredConfig = Config.ARGB_8888;
				// options.inPurgeable = true;//
				// options.inJustDecodeBounds = false;
				options.inSampleSize = scale;
				// mBitmap =
				// BitmapFactory.decodeFile(GlobalData.cachePath+fileUrlString,options);
				InputStream stream = null;
				try {
					stream = new FileInputStream(String.valueOf(data));
					bitmap = ImageUtils.getUnErrorBitmap(stream, options);
				} catch (Exception e) {
					/*
					 * do nothing. If the exception happened on open, bm will be
					 * null.
					 */
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							// do nothing here
						}
					}
				}
				// is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (displayConfig.getIsUseCache()) {
				if (displayConfig.getIsUseMemoryCache()) {
					mImageCache.addBitmapToMemoryCache(String.valueOf(data),
							bitmap);
				} else {
					mImageCache.addBitmapToDiskCache(String.valueOf(data),
							bitmap);
				}
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled() || mExitTasksEarly) {
				bitmap = null;
			}

			// 判断线程和当前的imageview是否是匹配
			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				recycle(imageView);
				mConfig.displayer.loadCompletedisplay(imageView, bitmap,
						displayConfig, loadAnim);
			} else if (bitmap == null && imageView != null) {
				mConfig.displayer.loadFailDisplay(imageView,
						displayConfig.getLoadfailBitmap());
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			super.onCancelled(bitmap);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		/**
		 * 获取线程匹配的imageView,防止出现闪动的现象
		 *
		 * @return
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			// final BitmapLoadAssetAndDisplayTask bitmapWorkerTask =
			// getBitmapTaskFromImageView(imageView);

			// if (this == bitmapWorkerTask) {
			return imageView;
			// }

			// return null;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * @title 配置信息
	 * @description VCBitmapFactory的配置信息
	 * @company 探索者网络工作室(www.tsz.net)
	 * @author michael Young (www.YangFuhai.com)
	 * @version 1.0
	 * @created 2012-10-28
	 */
	private class VCBitmapFactoryConfig {

		public String cachePath;

		public Displayer displayer;
		public Downloader downloader;
		public BitmapProcess bitmapProcess;
		public BitmapDisplayConfig defaultDisplayConfig;
		public float memCacheSizePercent;// 缓存百分比，android系统分配给每个apk内存的大小
		public int memCacheSize;// 内存缓存百分比
		public int diskCacheSize;// 磁盘百分比
		public int poolSize = 10;// 默认的线程池线程并发数量
		public int originalDiskCacheSize = 30 * 1024 * 1024;// 50MB

		public VCBitmapFactoryConfig(Context context) {
			defaultDisplayConfig = new BitmapDisplayConfig();

			defaultDisplayConfig.setAnimation(null);
			defaultDisplayConfig
					.setAnimationType(BitmapDisplayConfig.AnimationType.fadeIn);

			// 图片尺寸，在不异常的情况下 提供最大尺寸
			// DisplayMetrics displayMetrics = context.getResources()
			// .getDisplayMetrics();
			// int defaultWidth = (int) Math.floor(displayMetrics.widthPixels /
			// 4);
			// defaultDisplayConfig.setBitmapHeight(defaultWidth);
			// defaultDisplayConfig.setBitmapWidth(defaultWidth);

		}

		public void init() {
			if (downloader == null)
				downloader = new SimpleHttpDownloader();

			if (displayer == null)
				displayer = new SimpleDisplayer();

			bitmapProcess = new BitmapProcess(downloader, cachePath,
					originalDiskCacheSize);
		}

	}

	public void recycle(ImageView vcImageView) {
		try {
			if (vcImageView != null) {

				Drawable drawable = vcImageView.getDrawable();
				BitmapDrawable bd = null;
				if (drawable != null) {
					if (drawable instanceof TransitionDrawable) {

						TransitionDrawable ss = (TransitionDrawable) vcImageView
								.getDrawable();
						bd = (BitmapDrawable) ss.getDrawable(1);
					} else if (drawable instanceof BitmapDrawable) {
						bd = (BitmapDrawable) vcImageView.getDrawable();
					} else if (drawable instanceof FastBitmapDrawable) {

						FastBitmapDrawable fastBitmapDrawable = (FastBitmapDrawable) vcImageView
								.getDrawable();
						Bitmap bitmap = fastBitmapDrawable.getBitmap();
						rceycleBitmap(bitmap, vcImageView);

						return;
						// bd=(BitmapDrawable)((FastBitmapDrawable)vcImageView.getDrawable().getCurrent());
					}
				}

				// vcImageView.setDrawingCacheEnabled(false);
				if (bd != null) {
					vcImageView.setImageBitmap(null);
					Bitmap bitmap = bd.getBitmap();
					rceycleBitmap(bitmap, vcImageView);

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void rceycleBitmap(Bitmap bitmap, ImageView vImageView) {
		// TODO Auto-generated method stub
		if (bitmap != null && !bitmap.isRecycled()) {
			Log.e("rceycleBitmap", "rceycleBitmap");
			vImageView.setImageBitmap(null);
			bitmap.recycle();
			bitmap = null;
		}

	}
}
