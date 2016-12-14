package com.uroad.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uroad.uroad_ctllib.R;

/**
 * @author Administrator
 * 碎片的基类
 */
public class BaseFragment extends Fragment {

	protected IFragmentRefreshInterface refreshInterface;
	protected IFragmentOnItemClickInterface itemClickInterface;
	private FrameLayout base_content;
	private LinearLayout base_viewloading, base_view_load_fail,
			base_view_load_nodata;
	protected View view;

	private ImageView base_ivloadingfail;
	private TextView base_txt_neterr;
	private Button btnBaseBaoliao;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// container.removeAllViews();
		// return inflater.inflate(R.layout.base_fragment_layout, null);
		Log.i("base", "you were opened "+getClass().toString());
		return view;
	}

	@Override
	public void onDestroyView() {
		try {
			ViewGroup viewp = (ViewGroup) view.getParent();
			viewp.removeView(view);
		} catch (Exception e) {
			// TODO: handle exception
		}
//		Log.i("base", getClass().toString()+" onDestory");
		// this.container.removeView(viewp);
		super.onDestroyView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getLayoutInflater(savedInstanceState);
		view = inflater.inflate(R.layout.base_fragment_layout, null);
		base_content = (FrameLayout) view.findViewById(R.id.base_content);
		base_view_load_fail = (LinearLayout) view
				.findViewById(R.id.base_view_load_fail);
		base_viewloading = (LinearLayout) view
				.findViewById(R.id.base_viewloading);
		base_view_load_nodata = (LinearLayout) view
				.findViewById(R.id.base_view_load_nodata);

		base_ivloadingfail = (ImageView) base_view_load_nodata
				.findViewById(R.id.base_ivloadingfail);
		base_txt_neterr = (TextView) base_view_load_nodata
				.findViewById(R.id.base_txt_neterr);
		btnBaseBaoliao = (Button) base_view_load_nodata
				.findViewById(R.id.btnBaseBaoliao);
		// base_view_load_fail.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// if (refreshInterface != null) {
		// refreshInterface.load(0);

		// try {
		// base_ivloadingfail = (ImageView)
		// base_view_load_nodata.findViewById(R.id.base_ivloadingfail);
		// base_txt_neterr = (TextView)
		// base_view_load_nodata.findViewById(R.id.base_txt_neterr);
		// btnBaseBaoliao = (Button)
		// base_view_load_nodata.findViewById(R.id.btnBaseBaoliao);
		// base_view_load_fail.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// if (refreshInterface != null) {
		// refreshInterface.load(0);
		// }
		// }
		// });
		//
		// } catch (Exception e) {
		// TODO: handle exception
		// }
		// base_view_load_nodata.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // refreshInterface.load(0);
		// if (refreshInterface != null) {
		// refreshInterface.load(0);
		// }
		// }
		// });
	}

	public void setLoading() {
		try {
			base_viewloading.setVisibility(View.VISIBLE);
			base_view_load_fail.setVisibility(View.GONE);
			base_content.setVisibility(View.GONE);
			base_view_load_nodata.setVisibility(View.GONE);
		} catch (Exception e) {
		}
	}

	public void setEndLoading() {
		try {
			base_viewloading.setVisibility(View.GONE);
			base_view_load_fail.setVisibility(View.GONE);
			base_content.setVisibility(View.VISIBLE);
			base_view_load_nodata.setVisibility(View.GONE);
		} catch (Exception e) {
		}
	}

	public void setLoadingNOdata(int res, String text, OnClickListener l) {
		try {
			base_viewloading.setVisibility(View.GONE);
			base_view_load_fail.setVisibility(View.GONE);
			base_view_load_nodata.setVisibility(View.VISIBLE);
			base_content.setVisibility(View.GONE);
			base_ivloadingfail.setImageResource(res);
			if (!TextUtils.isEmpty(text)) {
				base_txt_neterr.setText(text);
				base_txt_neterr.setVisibility(View.VISIBLE);
			}
			if (l != null) {
				base_view_load_nodata.setOnClickListener(l);
			}
		} catch (Exception e) {
		}
	}

	public void setLoadingNOdata(int res, String text, OnClickListener l,
								 boolean isBtn) {
		try {
			base_viewloading.setVisibility(View.GONE);
			base_view_load_fail.setVisibility(View.GONE);
			base_view_load_nodata.setVisibility(View.VISIBLE);
			base_content.setVisibility(View.GONE);
			base_ivloadingfail.setImageResource(res);
			if (!TextUtils.isEmpty(text)) {
				base_txt_neterr.setText(text);
				base_txt_neterr.setVisibility(View.VISIBLE);
			}
			btnBaseBaoliao.setVisibility(View.VISIBLE);
			if (l != null) {
				btnBaseBaoliao.setOnClickListener(l);
			}
		} catch (Exception e) {
		}
	}

	public void setLoadingNOdata(int res, String text, OnClickListener l,
								 String btnText) {
		try {
			base_viewloading.setVisibility(View.GONE);
			base_view_load_fail.setVisibility(View.GONE);
			base_view_load_nodata.setVisibility(View.VISIBLE);
			base_content.setVisibility(View.GONE);
			base_ivloadingfail.setImageResource(res);
			if (!TextUtils.isEmpty(text)) {
				base_txt_neterr.setText(text);
				base_txt_neterr.setVisibility(View.VISIBLE);
			}

			if (l != null) {
				btnBaseBaoliao.setVisibility(View.VISIBLE);
				btnBaseBaoliao.setOnClickListener(l);
				btnBaseBaoliao.setText(btnText);
			} else {
				btnBaseBaoliao.setVisibility(View.GONE);
			}
		} catch (Exception e) {
		}
	}

	public void setLoadingNOdata() {
		try {
			base_viewloading.setVisibility(View.GONE);
			base_view_load_fail.setVisibility(View.GONE);
			base_view_load_nodata.setVisibility(View.VISIBLE);
			base_content.setVisibility(View.GONE);
		} catch (Exception e) {
		}
	}

	public void setWhiteBG() {
		base_content.setBackgroundColor(Color.WHITE);

	}

	public void setLoadFail() {
		try {
			base_viewloading.setVisibility(View.GONE);
			base_view_load_fail.setVisibility(View.VISIBLE);
			base_content.setVisibility(View.GONE);
			base_view_load_nodata.setVisibility(View.GONE);
		} catch (Exception e) {
		}
	}

	public View setBaseContentLayout(int layoutResId) {
		base_content.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		base_content.addView(v);
		return view;
	}

	protected void setBlackBG() {
		base_content.setBackgroundColor(Color.BLACK);
	}

	public View setBaseContentView(View v) {
		base_content.removeAllViews();
		base_content.addView(v);
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
