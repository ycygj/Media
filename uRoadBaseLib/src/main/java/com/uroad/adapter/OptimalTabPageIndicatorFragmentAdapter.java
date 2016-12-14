/**
 * @Title: TabPageIndicatorFragmentAdapter.java
 * @Package com.uroad.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-12-9 下午7:16:20
 * @version V1.0
 */
package com.uroad.adapter;

import java.util.List;

import com.uroad.entity.FragmentInfo;

import android.content.Context;
import android.support.v4.app.FragmentManager;

/**
 * @author Administrator
 * 经过优化的　TabPageIndicatorFragment适配器
 */
public class OptimalTabPageIndicatorFragmentAdapter extends OptimalFragmentAdapter {

	String[] datas;

	/**
	 * @param fm
	 * @param frags
	 */
	public OptimalTabPageIndicatorFragmentAdapter(Context context, FragmentManager fm, List<FragmentInfo> frags, String[] titles) {
		super(context, fm, frags);
		datas = titles;
	}

	public CharSequence getPageTitle(int position) {
		return datas[position % datas.length].toUpperCase();
	}

	public void notifydatas(String[] datas) {
		this.datas = datas;
	}

}
