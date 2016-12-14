/**
 * Copyright@2012 uroad.com
 *
 * Author:oukaichao
 * Date:2012.08.13
 * Description:文件管理类
 */
package com.uroad.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FileHelper {
	/**
	 * 读取文件
	 *
	 * @param stream
	 * @return
	 */
	public static String GetStringByStream(InputStream stream) {

		String content = "";

		try {
			if (stream != null) {
				InputStreamReader inputreader = new InputStreamReader(stream,"GBK");
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				// 分行读取
				while ((line = buffreader.readLine()) != null) {
					content += line + "\n";
				}
				stream.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return content;
	}
	/**
	 * 读取文件
	 *
	 * @param stream
	 * @return
	 */
	public static JSONObject GetJsonByStream(InputStream stream) {

		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(GetStringByStream(stream));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	/**
	 * 读取文件
	 *
	 * @param filePath
	 * @return
	 */
	public static String readFile(String fileName, Context c) {

		String res = "";
		try {
			FileInputStream fi = c.openFileInput(fileName);

			// 文件长度
			int length = fi.available();

			if (length > 0) {
				byte[] buffer = new byte[length];
				fi.read(buffer);
				res = EncodingUtils.getString(buffer, "UTF-8");
				fi.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 写入文件
	 *
	 * @param filePath
	 *            文件路径
	 * @param content
	 *            内容
	 * @return
	 */
	public boolean writeFile(String filePath, String message, Context c) {
		try {

			FileOutputStream fo = c.openFileOutput(filePath, Context.MODE_PRIVATE);

			byte[] byteArray = message.getBytes();
			fo.write(byteArray);
			fo.close();
			return true;

		} catch (Exception ex) {
			Log.e("写文件失败", ex.getMessage());
		}

		return false;
	}

	private FileOutputStream openFileOutput(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 初始读取本地文件
	 */
	public synchronized static void loadLocalFile(Context c) {

	}

	public static Bitmap GetBitmapByFileName(Context context, String fileName) {

		try {
			String packetName = context.getPackageName();
			// 将fileName 转换成id
			int resId = context.getResources().getIdentifier(fileName, "drawable", packetName);
			Bitmap bitmap = null;
			// InputStream im = context.getResources().openRawResource(resId);
			// bitmap = BitmapFactory.decodeStream(im);
			//
			// im.close();
			// im = null;
			bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Drawable GetDrawableByFileName(Context context, String fileName) {

		try {
			String packetName = context.getPackageName();
			// 将fileName 转换成id
			int resId = context.getResources().getIdentifier(fileName, "drawable", packetName);
			Drawable drawable = null;
			// InputStream im = context.getResources().openRawResource(resId);
			// bitmap = BitmapFactory.decodeStream(im);
			//
			// im.close();
			// im = null;
			drawable = context.getResources().getDrawable(resId);
			return drawable;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String getFileName(String filePath) {
		try {
			int index = filePath.lastIndexOf("/");
			String fileName = filePath.substring(index + 1, filePath.length());
			return fileName;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	/*** 获取文件夹大小 ***/
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/*** 转换文件大小单位(b/kb/mb/gb) ***/
	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/*** 获取文件个数 ***/
	public  static long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
	public static void delAllFile(File file) {
		try {

			if (!file.exists()) {
				return;
			}
			if (!file.isDirectory()) {
				return;
			}
			File[] tempList = file.listFiles();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				try {

					if (tempList[i].isFile()) {
						tempList[i].delete();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
