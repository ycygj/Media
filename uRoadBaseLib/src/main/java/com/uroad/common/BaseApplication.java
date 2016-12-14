package com.uroad.common;

import com.uroad.imageUtil.ImageLoader;
import com.uroad.widget.image.ImageViewFactory;

import android.app.Application;
import android.content.Context;

/**
 * @author Administrator
 * app的程序基类
 */
public class BaseApplication extends Application {

	private static BaseApplication instance;
	public Context currContext;
	public ImageViewFactory factory;
	public static ImageLoader baseImageLoader;

	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
		factory = ImageViewFactory.create(instance);
		baseImageLoader = new ImageLoader(this);
		// ExceptionHandler Error = ExceptionHandler.getInstance();
		// Error.init(getApplicationContext());
		// Error.sendPreviousReportsToServer();
	}

	public static BaseApplication getInstance() {
		if (instance == null) {
			instance = new BaseApplication();
		}
		return instance;
	}
}
