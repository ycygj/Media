/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.uroad.widget;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.uroad.adapter.BasePageAdapter;
import com.uroad.widget.image.ImageViewTouchBase;
import com.uroad.widget.image.UroadImageView;

/**
 * Class wraps URLs to adapter, then it instantiates <b>UrlTouchImageView</b>
 * objects to paging up through them.
 * 使用这个adapter　viewpager里面的东西要么是uroadimageview 要么里面有个uroadimageview id 为 imgPic
 */
public class GalleryViewPagerAdapter extends BasePageAdapter {

	public GalleryViewPagerAdapter(Context c, List< ? extends View> vs) {
		super(c, vs);
	}

	protected int mCurrentPosition = -1;
	protected OnItemChangeListener mOnItemChangeListener;

	// 每当滑动到这个页面时都会执行这个方法
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		if (mCurrentPosition == position || object == null)
			return;
		mCurrentPosition = position;
		if (mOnItemChangeListener != null) {
			mOnItemChangeListener.onItemChange(mCurrentPosition);
		}
		if (object instanceof UroadImageView) {
			((GalleryViewPager) container).mCurrentView = ((UroadImageView) object)
					.getImageViewTouchBase();
		}else{
			((GalleryViewPager) container).mCurrentView =((UroadImageView)((View)object).findViewWithTag("uroadimageview")).getImageViewTouchBase();
		}
//		((GalleryViewPager) container).mCurrentView = ((UroadImageView) object)
//				.getImageViewTouchBase();
		if(((GalleryViewPager) container).mCurrentView!=null){
			((GalleryViewPager) container).mCurrentView.resetScale();
		}
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	public void setOnItemChangeListener(OnItemChangeListener listener) {
		mOnItemChangeListener = listener;
	}

	public static interface OnItemChangeListener {
		public void onItemChange(int currentPosition);
	}
}
