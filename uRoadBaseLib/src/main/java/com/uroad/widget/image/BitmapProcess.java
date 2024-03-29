package com.uroad.widget.image;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.util.Log;

public class BitmapProcess {
	private static final String TAG = "BitmapProcess";
	private boolean mHttpDiskCacheStarting = true;
	private int cacheSize;
	private static final int DEFAULT_CACHE_SIZE = 20 * 1024 * 1024; // 20MB

	private LruDiskCache mOriginalDiskCache;// 原始图片的路径，不进行任何的压缩操作
	private final Object mHttpDiskCacheLock = new Object();
	private static final int DISK_CACHE_INDEX = 0;

	private File mOriginalCacheDir;
	private Downloader downloader;

	private boolean neverCalculate = false;

	public BitmapProcess(Downloader downloader, String filePath, int cacheSize) {
		this.mOriginalCacheDir = new File(filePath + "/original");
		this.downloader = downloader;
		if (cacheSize <= 0)
			cacheSize = DEFAULT_CACHE_SIZE;
		this.cacheSize = cacheSize;
	}

	public void configCalculateBitmap(boolean neverCalculate) {
		this.neverCalculate = neverCalculate;
	}

	public Bitmap processBitmap(String data, BitmapDisplayConfig config,
								boolean calculate) {
		Log.e("processBitmap", data);
		final String key = FileNameGenerator.generator(data);
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		LruDiskCache.Snapshot snapshot;
		synchronized (mHttpDiskCacheLock) {
			// Wait for disk cache to initialize
			while (mHttpDiskCacheStarting) {
				try {
					mHttpDiskCacheLock.wait();
				} catch (InterruptedException e) {
				}
			}

			if (mOriginalDiskCache != null) {
				try {
					snapshot = mOriginalDiskCache.get(key);
					if (!config.getIsUseCache()) {//只要是使用缓存 代码能执行到这里说明 缓存拿 不到数据 就不能用原来那个了
						snapshot = null;
					}
					if (snapshot == null) {
						Log.e("LruDiskCache_begin", key);
						LruDiskCache.Editor editor = mOriginalDiskCache
								.edit(key);
						if (editor != null) {
							if (downloader.downloadToLocalStreamByUrl(data,
									editor.newOutputStream(DISK_CACHE_INDEX))) {
								Log.e("LruDiskCache_end", key);
								editor.commit();
							} else {
								editor.abort();
							}
						}
						snapshot = mOriginalDiskCache.get(key);
					}
					if (snapshot != null) {
						fileInputStream = (FileInputStream) snapshot
								.getInputStream(DISK_CACHE_INDEX);
						fileDescriptor = fileInputStream.getFD();
					}
				} catch (IOException e) {
					Log.e(TAG, "processBitmap - " + e);
				} catch (IllegalStateException e) {
					Log.e(TAG, "processBitmap - " + e);
				} finally {
					if (fileDescriptor == null && fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}

		Bitmap bitmap = null;
		// ///
		if (fileDescriptor != null) {
			if (neverCalculate || !calculate) {
				if (config.getScale() == 0)
					bitmap = BitmapDecoder
							.decodeSampledBitmapFromDescriptor(fileDescriptor);
				else
					bitmap = BitmapDecoder.decodeSampledBitmapFromDescriptor(
							fileDescriptor, config.getScale());
			} else
				bitmap = BitmapDecoder.decodeSampledBitmapFromDescriptor(
						fileDescriptor, config.getBitmapWidth(),
						config.getBitmapHeight());

		}
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
			} catch (IOException e) {
			}
		}
		return bitmap;
	}

	public Bitmap processBitmap(String data, BitmapDisplayConfig config) {
		return processBitmap(data, config, true);
	}

	public void initHttpDiskCache() {
		if (!mOriginalCacheDir.exists()) {
			mOriginalCacheDir.mkdirs();
		}
		synchronized (mHttpDiskCacheLock) {
			if (ImageUtils.getUsableSpace(mOriginalCacheDir) > cacheSize) {
				try {
					mOriginalDiskCache = LruDiskCache.open(mOriginalCacheDir,
							1, 1, cacheSize);
				} catch (IOException e) {
					mOriginalDiskCache = null;
				}
			}else {
				cacheSize = (int) (ImageUtils.getUsableSpace(mOriginalCacheDir)-1000);
				try {
					mOriginalDiskCache = LruDiskCache.open(mOriginalCacheDir,
							1, 1, cacheSize);
				} catch (IOException e) {
					mOriginalDiskCache = null;
				}
			}
			mHttpDiskCacheStarting = false;
			mHttpDiskCacheLock.notifyAll();
		}
	}

	public void clearCacheInternal() {
		synchronized (mHttpDiskCacheLock) {
			if (mOriginalDiskCache != null && !mOriginalDiskCache.isClosed()) {
				try {
					mOriginalDiskCache.delete();
				} catch (IOException e) {
					Log.e(TAG, "clearCacheInternal - " + e);
				}
				mOriginalDiskCache = null;
				mHttpDiskCacheStarting = true;
				initHttpDiskCache();
			}
		}
	}

	public void flushCacheInternal() {
		synchronized (mHttpDiskCacheLock) {
			if (mOriginalDiskCache != null) {
				try {
					mOriginalDiskCache.flush();
				} catch (IOException e) {
					Log.e(TAG, "flush - " + e);
				}
			}
		}
	}

	public void closeCacheInternal() {
		synchronized (mHttpDiskCacheLock) {
			if (mOriginalDiskCache != null) {
				try {
					if (!mOriginalDiskCache.isClosed()) {
						mOriginalDiskCache.close();
						mOriginalDiskCache = null;
					}
				} catch (IOException e) {
					Log.e(TAG, "closeCacheInternal - " + e);
				}
			}
		}
	}

}