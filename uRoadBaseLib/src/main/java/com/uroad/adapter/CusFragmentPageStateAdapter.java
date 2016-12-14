package com.uroad.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uroad.entity.FragmentInfo;

/**
 * FragmentAdapter的第二版,主要增加了  获取当前碎片的功能,优化的adapter的结构
 *
 */
public class CusFragmentPageStateAdapter extends FragmentStatePagerAdapter {

	Context mContext;
	FragmentManager mManager;
	private List<FragmentInfo> fragments = new ArrayList<FragmentInfo>();

	/**
	 * @param fm
	 */
	public CusFragmentPageStateAdapter(Context context, FragmentManager fm,
									   List<FragmentInfo> infos) {
		super(fm);
		mManager = fm;
		mContext = context;
		fragments = infos;
	}

	@Override
	public Fragment getItem(int position) {
		FragmentInfo fragmentInfo = fragments.get(position);
		//参数在碎片的 getArag中获取
		return Fragment.instantiate(mContext,
				fragmentInfo.getClss().getName(), fragmentInfo.getArgs());
	}


	@Override
	public int getCount() {
		return fragments.size();
	}

}
