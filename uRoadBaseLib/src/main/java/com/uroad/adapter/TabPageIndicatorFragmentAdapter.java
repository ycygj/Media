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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * @author Administrator
 * TabPageIndicatorFragment适配器
 */
public class TabPageIndicatorFragmentAdapter extends FragmentAdapter {

	String[] datas;
	/**
	 * @param fm
	 * @param frags
	 */
	public TabPageIndicatorFragmentAdapter(FragmentManager fm,
										   List<Fragment> frags,String[] titles) {
		super(fm, frags);
		datas=titles;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return datas[position % datas.length].toUpperCase();
	}

}
