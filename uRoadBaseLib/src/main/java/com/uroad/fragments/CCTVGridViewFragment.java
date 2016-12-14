package com.uroad.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.uroad.adapter.CCTVGridViewAdapter;
import com.uroad.adapter.CCTVGridViewAdapter.iBeforeItemClick;
import com.uroad.dialog.CCTVDialog.ICCTVDialogInfaterface;
import com.uroad.entity.CCTV;
import com.uroad.uroad_ctllib.R;
import com.uroad.widget.pulltorefresh.PullToRefreshBase;
import com.uroad.widget.pulltorefresh.PullToRefreshGridView;
import com.uroad.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.uroad.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;

/**
 * @author Administrator
 * 快拍的碎片，使用gridview结构，可定制点击事件，默认使用CCTVDialog弹出框
 */
@SuppressLint("ValidFragment")
public class CCTVGridViewFragment extends BaseFragment {

	public static int MODE_ONE_IMG = 1;
	public static int MODE_THREE_IMG = 0;
	PullToRefreshGridView mgGridView;
	CCTVGridViewAdapter adapter;
	RelativeLayout rlBackground;
	List<CCTV> dataList;
	ICCTVDialogInfaterface iDialogInfaterface;
	Context context;
	int mode = MODE_THREE_IMG;
	ImageView imgNodata;

	public CCTVGridViewFragment(Context mContext) {
		context = mContext;
	}

	public CCTVGridViewFragment() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
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

	public void setGridViewLoading() {
		mgGridView.setRefreshing();
	}

	public void setGridViewEndLoading() {
		mgGridView.onRefreshComplete();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = setBaseContentLayout(R.layout.base_gridview_layout);
		setBlackBG();
		mgGridView = (PullToRefreshGridView) view.findViewById(R.id.gvList);
		imgNodata = (ImageView) view.findViewById(R.id.imgNodata);
		rlBackground = (RelativeLayout) view.findViewById(R.id.rlBackground);
		mgGridView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		mgGridView.setOnRefreshListener(refreshListener);

		mgGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				CCTV cctv = dataList.get(arg2);
				// itemClickInterface.onItemClick(arg0, arg1, arg2, arg3, cctv);
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

	public void setLoadNoCCTV() {
		rlBackground.setBackgroundColor(Color.WHITE);
		imgNodata.setVisibility(View.VISIBLE);
	}

	public void setInterface(ICCTVDialogInfaterface face) {
		iDialogInfaterface = face;
		dataList = new ArrayList<CCTV>();
		adapter = new CCTVGridViewAdapter(context, dataList,
				iDialogInfaterface, this.mode);
		mgGridView.setAdapter(adapter);
	}

	public void setBeforeInterface(iBeforeItemClick click) {
		adapter.setBeforeInterface(click);
	}

	/*
	 * 设置数据源 必需先执行setInterface
	 */
	public void loadData(List<CCTV> list) {
		dataList.clear();
		dataList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	public void setRefresh(CCTV cctv, int pageindex) {
		if (adapter != null) {
			adapter.setRefresh(cctv, pageindex);
		}
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

	public void setMode(int imgMode) {
		this.mode = imgMode;
	}

}
