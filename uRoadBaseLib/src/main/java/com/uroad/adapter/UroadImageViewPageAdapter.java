/**
 * @Title: UroadImageViewPageAdapter.java
 * @Package com.uroad.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-12-11 下午5:33:31
 * @version V1.0
 */
package com.uroad.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.uroad.uroad_ctllib.R;
import com.uroad.widget.image.ImageViewFactory;
import com.uroad.widget.image.UroadImageView;

/**
 * @author Administrator
 * UroadImageView 特制的适配器，适合把图片放在viewpager里使用
 */
public class UroadImageViewPageAdapter extends PagerAdapter {

	Context mContext;
	List<String> dataList;
	UVOnItemClickListener listener;

	public UroadImageViewPageAdapter(Context context, List<String> list,
									 UVOnItemClickListener clickListener) {
		super();
		mContext = context;
		dataList = list;
		listener = clickListener;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ImageViewFactory.create(mContext).recycle(
				((UroadImageView) object).getImageView());
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		UroadImageView imageView = new UroadImageView(mContext);
		imageView.setBaseScaleType(ScaleType.FIT_XY);
		imageView.setScaleEnable(false);
		imageView.setImageUrl(dataList.get(position), R.drawable.base_nodata);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(v, position);
			}
		});
		container.addView(imageView);
		return imageView;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	public interface UVOnItemClickListener {
		abstract void onClick(View view, int position);
	}

}
