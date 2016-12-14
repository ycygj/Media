package com.uroad.widget.image;

import android.content.Context;
import android.net.Uri;

/*
 * 图像类的接口，定义了图像所应有的功能
 * */
public interface IImageView {

	/*
	 * 图像的销毁方法 包括bitmap的回收和缓存的清除
	 *
	 * @param isClearCache 是否清除缓存
	 */
	abstract void dispose(boolean isClearCache);

	/*
	 * 加载指定的网络url图片
	 *
	 * @param url url 默认缓存起来
	 */
	abstract void setImageUrl(String url);

	/*
	 * 加载指定的网络url图片
	 *
	 * @param url url
	 *
	 * @param 不缓存图片
	 */
	abstract void setImageUrlNotCache(String url);

	/*
	 * 加载指定的网络url图片
	 *
	 * @param url url 默认缓存起来
	 *
	 * @param faileResid 加载失败时显示的资源图片
	 */
	abstract void setImageUrl(String url, int faileResid);

	/*
	 * 加载指定的资源
	 *
	 * @param res 资源id
	 */
	abstract void loadRes(int res);

	/*
	 * 从相册返回的uri中加载图片
	 *
	 * @param uri uri
	 */
	abstract void loadUri(Uri uri);

	/*
	 * 从相册返回的uri中加载图片
	 *
	 * @param fileName 文件名
	 */
	abstract void loadUri(String fileName);

	/*
	 * 指定图片是否双指拨动 放大 缩小 功能 此功能默认 以原图 为最小 手机屏幕 为最大 伸缩范围
	 *
	 * @param isScale
	 */
	abstract void setScaleEnable(boolean isScale);

	/*
	 * 把图片放大到塞满屏幕，或 缩小到原来大小
	 *
	 * @param isScale
	 */
	abstract void toggleFillScreen();

	abstract void toggleFillScreen(String url);

	abstract void hideFullScreen(Context mContext);

	/*
	 * 用于网络加载图片时取消加载图片的异步线程
	 */
	abstract void cancelLoadingTask();

	/*
	 * 客户端渲染
	 *
	 * @param res 底图
	 */
	abstract void clientRender(int res);

	/**
	 * @Title: setImageUrlMemoryCache
	 * @Description: TODO
	 * @param @param url
	 * @return void
	 * @throws
	 */
	void setImageUrlMemoryCache(String url);

	void setImageUrlNotCache(String url, int resId);

}
