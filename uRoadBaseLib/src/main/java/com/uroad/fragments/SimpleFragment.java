package com.uroad.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * 简化的fragment基类,除了接口相关的函数都要自己实现,比如setloading
 *
 */
public class SimpleFragment extends Fragment {

	protected IFragmentRefreshInterface refreshInterface;
	protected IFragmentOnItemClickInterface itemClickInterface;
	protected View view;

	public void setRefreshInterface(IFragmentRefreshInterface refresh) {
		this.refreshInterface = refresh;
	}

	public void setItemClickInterface(
			IFragmentOnItemClickInterface onItemClickInterface) {
		this.itemClickInterface = onItemClickInterface;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// container.removeAllViews();
		// return inflater.inflate(R.layout.base_fragment_layout, null);
		return view;
	}

	@Override
	public void onDestroyView() {
		ViewGroup viewp = (ViewGroup) view.getParent();
		viewp.removeView(view);
		// this.container.removeView(viewp);
		super.onDestroyView();
	}


	public void setLoading() {
	}

	public void setEndLoading() {
	}

	public void setLoadingNOdata() {
	}

	public void setLoadFail() {
	}

	public View setBaseContentLayout(int layoutResId) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layoutResId, null);
		return view;
	}


	public View setBaseContentView(View v) {
		view=v;
		return view;
	}

	public interface IFragmentRefreshInterface {
		abstract void load(int pageIndex);
	}

	public interface IFragmentOnItemClickInterface {
		abstract void onItemClick(AdapterView<?> parent, View view,
								  int position, long id, Object object);
	}

}
