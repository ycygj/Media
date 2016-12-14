package com.uroad.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.uroad.entity.FragmentInfo;

/**
 * FragmentAdapter的第二版,主要增加了  获取当前碎片的功能,优化的adapter的结构
 *
 */
public class OptimalFragmentAdapter extends FragmentPagerAdapter {

	Context mContext;
	FragmentManager mManager;
	private List<FragmentInfo> fragments = new ArrayList<FragmentInfo>();

	/**
	 * @param fm
	 */
	public OptimalFragmentAdapter(Context context, FragmentManager fm,
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

	public Fragment getFragment(int idx) {
		return mManager.findFragmentByTag(makeFragmentName(idx));
	}

	public void addFragment(FragmentInfo info){
		fragments.add(info);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	private FragmentTransaction mCurTransaction;
	private Fragment mCurrentPrimaryItem;

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCurTransaction == null) {
			mCurTransaction = mManager.beginTransaction();
		}
		mCurTransaction.detach((Fragment) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (mCurTransaction == null) {
			mCurTransaction = mManager.beginTransaction();
		}

		final long itemId = position;

		// Do we already have this fragment?
		String name = makeFragmentName(itemId);
		Fragment fragment = mManager.findFragmentByTag(name);
		if (fragment != null) {
			mCurTransaction.attach(fragment);
		} else {
			fragment = getItem(position);
			mCurTransaction.add(container.getId(), fragment,
					makeFragmentName(itemId));
		}
		if (mCurrentPrimaryItem != fragment) {
			fragment.setMenuVisibility(false);
			fragment.setUserVisibleHint(false);
		}

		return fragment;
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mManager.executePendingTransactions();
		} else {
			super.finishUpdate(container);
		}
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(false);
				mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;
		}
	}

	private String makeFragmentName(long itemId) {
		return "page:" + itemId;
	}
}
