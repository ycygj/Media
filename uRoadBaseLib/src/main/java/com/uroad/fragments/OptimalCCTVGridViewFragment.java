package com.uroad.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.uroad.adapter.OptimalCCTVGridViewAdapter;
import com.uroad.adapter.OptimalCCTVGridViewAdapter.CCTVGridDelInterface;
import com.uroad.entity.CCTV;
import com.uroad.uroad_ctllib.R;
import com.uroad.widget.pulltorefresh.PullToRefreshBase;
import com.uroad.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.uroad.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.uroad.widget.pulltorefresh.PullToRefreshGridView;

/**
 * 第二版 分离点击图片时的 弹出框 降低耦合度
 *
 */
public class OptimalCCTVGridViewFragment extends BaseFragment {

	PullToRefreshGridView mgGridView;
	OptimalCCTVGridViewAdapter adapter;
	protected List<CCTV> dataList;
	Context context;
	CCTVGridDelInterface delInterface;

	public OptimalCCTVGridViewFragment(Context mContext,
									   CCTVGridDelInterface cInterface,IFragmentRefreshInterface refreshInterface) {
		context = mContext;
		delInterface = cInterface;
		this.refreshInterface=refreshInterface;
	}

	public OptimalCCTVGridViewFragment() {

	}


	@Override
	public void onAttach(Activity activity) {
		if (context == null) {
			context = activity;
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.e("CCTVGridViewFragment", "ViewCreate");
		if (context == null) {
			context = inflater.getContext();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e("CCTVGridViewFragment", "onDestroyView");
	}
//
//	@Override
//	public void setLoading() {
//		mgGridView.setRefreshing();
//	}
//
//	@Override
//	public void setEndLoading() {
//		mgGridView.onRefreshComplete();
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = setBaseContentLayout(R.layout.base_gridview_layout);
		mgGridView = (PullToRefreshGridView) view.findViewById(R.id.gvList);
		mgGridView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		mgGridView.setOnRefreshListener(refreshListener);
		dataList = new ArrayList<CCTV>();
		adapter = new OptimalCCTVGridViewAdapter(context, dataList,
				delInterface);
		mgGridView.setAdapter(adapter);
		mgGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				CCTV cctv = dataList.get(arg2);
				itemClickInterface.onItemClick(arg0, arg1, arg2, arg3, cctv);
			}
		});
	}

	OnRefreshListener<GridView> refreshListener = new OnRefreshListener<GridView>() {

		@Override
		public void onRefresh(PullToRefreshBase<GridView> refreshView) {
			mgGridView.onRefreshComplete();
			refreshInterface.load(0);
		}
	};

	/*
	 * 设置数据源 必需先执行setInterface
	 */
	public void loadData(List<CCTV> list) {
		dataList.clear();
		dataList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	public void showDelete() {
		if (dataList != null) {
			List<CCTV> list = new ArrayList<CCTV>();
			for (CCTV cctv : dataList) {
				cctv.setTag("showdelelte");
				list.add(cctv);
			}
			loadData(list);
		}
	}

	public void hideDelete() {
		if (dataList != null) {
			List<CCTV> list = new ArrayList<CCTV>();
			for (CCTV cctv : dataList) {
				cctv.setTag(null);
				list.add(cctv);
			}
			loadData(list);
		}
	}

	public PullToRefreshGridView getGridView(){
		return mgGridView;
	}

}
