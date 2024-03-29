package com.uroad.widget.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface Displayer {

	/**
	 * 图片加载完成 回调的函数
	 * @param imageView
	 * @param bitmap
	 * @param config
	 */
	public void loadCompletedisplay(ImageView imageView,Bitmap bitmap,BitmapDisplayConfig config);

	public void loadCompletedisplay(ImageView imageView,Bitmap bitmap,BitmapDisplayConfig config,boolean loadAnim);

	/**
	 * 图片加载失败回调的函数
	 * @param imageView
	 * @param bitmap
	 */
	public void loadFailDisplay(ImageView imageView,Bitmap bitmap);

}
