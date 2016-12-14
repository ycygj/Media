package com.uroad.imageUtil;

import java.io.File;

import com.uroad.util.FileUtils;
import com.uroad.widget.image.ImageUtils;

import android.content.Context;
import android.util.Log;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context) {
		// 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
		// 没有SD卡就放在系统的缓存目录中
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"LazyList");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * 计算App缓存
	 */
	public String ComputeAppCache(Context context) {

		long fileSize = 0;
		String cacheSize = "0KB";
		// File filesDir = context.getFilesDir();
		// File cacheDir = context.getCacheDir();
		// // String cacheString = ImageUtils.getDiskCacheDir(context,
		// // "bitmapCache")
		// // .getAbsolutePath();
		// String cacheString = cacheDir.getAbsolutePath();
		// File cache = new File(cacheString);
		// if (cacheDir.exists()) {
		// fileSize += FileUtils.getDirSize(cache);
		// // }
		// fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);
		if (fileSize > 0) {
			cacheSize = FileUtils.formatFileSize(fileSize);
		}
		return cacheSize;
	}

	public FileCache(Context context, String dirName) {
		// 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
		// 没有SD卡就放在系统的缓存目录中
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					dirName);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		// 将url的hashCode作为缓存的文件名
		String filename = String.valueOf(url.hashCode());
		// Another possible solution
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void remove(String key) {
		try {
			String filename = String.valueOf(key.hashCode());
			File f = new File(cacheDir, filename);
			deleteFile(f);
		} catch (Exception e) {
		}
	}

	public void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			Log.e("FileCache", "文件不存在！" + "\n");
		}
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}
