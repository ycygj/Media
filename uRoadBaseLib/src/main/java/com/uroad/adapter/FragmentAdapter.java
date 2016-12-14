package com.uroad.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/*
 * 碎片适配器，主要用于，viewpager 与radiogroup配合使用的界面
 * */
public class FragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> list = new ArrayList<Fragment>();

	public FragmentAdapter(FragmentManager fm, List<Fragment> frags) {
		super(fm);
		this.list = frags;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}


	@Override
	public int getItemPosition(Object object) {
		Log.e("fragment", "getItemPosition");
		return super.getItemPosition(object);
	}

	@Override
	public long getItemId(int position) {
		Log.e("fragment", "getItemId");
		return super.getItemId(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.e("fragment", "instantiateItem");
		return super.instantiateItem(container, position);
	}



}
