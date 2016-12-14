package com.uroad.widget.image;

import android.graphics.Bitmap;
import android.view.animation.Animation;

public class BitmapDisplayConfig {

	private int bitmapWidth;
	private int bitmapHeight;
	private boolean isCache = true;
	private boolean useMemoryCache = false;
	private boolean showLoading = true;

	private Animation animation;

	private int animationType;
	private Bitmap loadingBitmap;
	private Bitmap loadfailBitmap;
	private boolean hasAnimation = false;
	private int scale = 0;
	
	/**
	 * @param showLoading the showLoading to set
	 */
	public void setShowLoading(boolean showLoading) {
		this.showLoading = showLoading;
	}
	
	public boolean getShowLoading() {
		return showLoading;
	}
	
	
	public boolean getIsUseCache() {
		return isCache;
	}

	public void setIsUseCache(boolean isCache) {
		this.isCache = isCache;
	}

	public boolean getIsUseMemoryCache() {
		return useMemoryCache;
	}

	public void setIsUseMemoryCache(boolean isUse) {
		useMemoryCache = isUse;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean getHasAnimation() {
		return hasAnimation;
	}

	public void setHasAnimation(boolean hasAnimation) {
		this.hasAnimation = hasAnimation;
	}

	public int getBitmapWidth() {
		return bitmapWidth;
	}

	public void setBitmapWidth(int bitmapWidth) {
		this.bitmapWidth = bitmapWidth;
	}

	public int getBitmapHeight() {
		return bitmapHeight;
	}

	public void setBitmapHeight(int bitmapHeight) {
		this.bitmapHeight = bitmapHeight;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public int getAnimationType() {
		return animationType;
	}

	public void setAnimationType(int animationType) {
		this.animationType = animationType;
	}

	public Bitmap getLoadingBitmap() {
		return loadingBitmap;
	}

	public void setLoadingBitmap(Bitmap loadingBitmap) {
		this.loadingBitmap = loadingBitmap;
	}

	public Bitmap getLoadfailBitmap() {
		return loadfailBitmap;
	}

	public void setLoadfailBitmap(Bitmap loadfailBitmap) {
		this.loadfailBitmap = loadfailBitmap;
	}

	public class AnimationType {
		public static final int userDefined = 0;
		public static final int fadeIn = 1;
	}
}