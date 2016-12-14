package com.uroad.common;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uroad.uroad_ctllib.R;
import com.uroad.util.ActivityUtil;
import com.uroad.util.DensityHelper;
import com.uroad.util.DialogHelper;
import com.uroad.widget.slidingmenu.SlidingMenu;

/**
 * @author Administrator 碎片Activity的基类，使用上面是标题栏，下面是内容的　样式
 */
public class BaseFragmentActivity extends FragmentActivity {

	protected FrameLayout llContent, base_ld_container, base_lf_container,
			base_ln_container;
	protected RelativeLayout base_titlelayout, base_main_relative;
	protected View toplayout;
	Button btnBack, btnRight;
	TextView tvTitle;
	private ImageView base_ivloadingfail;
	private ImageView ivRight;
	private TextView base_txt_neterr;
	private Button btnBaseBaoliao;
	LinearLayout base_view_load_nodata;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base_main_activity);
		Log.i("base", "you were opened " + getClass().toString());
		initBase();
	}

	public Button getRightButton() {
		return btnRight;
	}

	public Button getLeftButton() {
		return btnBack;
	}

	public void HideLeftBtn() {
		if (btnBack != null) {
			btnBack.setVisibility(View.GONE);
		}
	}

	private void initBase() {
		llContent = (FrameLayout) findViewById(R.id.base_content);
		base_main_relative = (RelativeLayout) findViewById(R.id.base_main_relative);
		base_lf_container = (FrameLayout) findViewById(R.id.base_lf_container);
		base_ld_container = (FrameLayout) findViewById(R.id.base_ld_container);
		base_ln_container = (FrameLayout) findViewById(R.id.base_ln_container);
		base_titlelayout = (RelativeLayout) findViewById(R.id.base_titlelayout);
		btnBack = (Button) findViewById(R.id.btnBaseLeft);
		btnRight = (Button) findViewById(R.id.btnBaseRight);
		tvTitle = (TextView) findViewById(R.id.tvBaseTitle);
		toplayout = (View) findViewById(R.id.toplayout);
		ivRight = (ImageView) findViewById(R.id.ivBaseRight);

		base_view_load_nodata = (LinearLayout) findViewById(R.id.base_view_load_nodata);
		base_ivloadingfail = (ImageView) base_view_load_nodata
				.findViewById(R.id.base_ivloadingfail);
		base_txt_neterr = (TextView) base_view_load_nodata
				.findViewById(R.id.base_txt_neterr);
		btnBaseBaoliao = (Button) base_view_load_nodata
				.findViewById(R.id.btnBaseBaoliao);
		if (btnBack != null) {
			btnBack.setOnClickListener(clickListener);
			btnRight.setOnClickListener(clickListener);
			ivRight.setOnClickListener(clickListener);
		}
	}

	protected RelativeLayout getMainContainer() {
		return base_main_relative;
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setTitleTextColor(int color) {
		tvTitle.setTextColor(color);
	}

	public void Back() {
		onBackPressed();
	}

	protected void onRightClick(View v) {

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btnBaseLeft) {
				Back();
			} else if (v.getId() == R.id.btnBaseRight
					|| v.getId() == R.id.ivBaseRight) {
				onRightClick(v);
			}
		}

	};

	public void setTitleBackground(int res) {
		setTitleBackground(res, false);
	}

	public void setTitleBackground(int res, boolean isColor) {
		if (isColor) {
			toplayout.setBackgroundColor(res);
		} else {
			toplayout.setBackgroundResource(res);
		}
	}

	public void setNoTitle() {
		toplayout.setVisibility(View.GONE);
	}

	public void setRightBtn(int res) {
		btnRight.setVisibility(View.GONE);
		if (res != 0) {
			ivRight.setVisibility(View.VISIBLE);
			ivRight.setImageResource(res);
		}
	}

	public void setRightBtn(String text) {
		if (text != null) {
			btnRight.setVisibility(View.VISIBLE);
			btnRight.setText(text);
			btnRight.setBackgroundResource(0);
		} else {
			btnRight.setVisibility(View.GONE);
		}
	}

	public void setRightBtn(String text, int res) {
		if (text != null) {
			btnRight.setVisibility(View.VISIBLE);
			btnRight.setText(text);
			btnRight.setBackgroundResource(res);
		} else {
			btnRight.setVisibility(View.GONE);
		}
	}

	public void setRightBtn(String text, int res, boolean isWap) {
		if (text != null) {
			btnRight.setVisibility(View.VISIBLE);
			btnRight.setText(text);
			btnRight.setBackgroundResource(res);
			RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) btnRight
					.getLayoutParams();
			lParams.width = DensityHelper.dip2px(this, 60);
			lParams.height = DensityHelper.dip2px(this, 40);
		} else {
			btnRight.setVisibility(View.GONE);
		}
	}

	public void setRightBtn(String text, int res, int w, int h) {
		if (text != null) {
			btnRight.setVisibility(View.VISIBLE);
			btnRight.setText(text);
			btnRight.setBackgroundResource(res);
			RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) btnRight
					.getLayoutParams();
			lParams.width = DensityHelper.dip2px(this, w);
			lParams.height = DensityHelper.dip2px(this, h);
		} else {
			btnRight.setVisibility(View.GONE);
		}
	}

	protected void setPageLoading() {
		base_ld_container.setVisibility(View.VISIBLE);
		base_ld_container.getChildAt(0).setVisibility(View.VISIBLE);
		base_lf_container.setVisibility(View.GONE);
		base_ln_container.setVisibility(View.GONE);
		llContent.setVisibility(View.GONE);
	}

	protected void setPageEndLoading() {
		base_ld_container.setVisibility(View.GONE);
		base_lf_container.setVisibility(View.GONE);
		base_ln_container.setVisibility(View.GONE);
		llContent.setVisibility(View.VISIBLE);
	}

	protected void setPageLoadFail() {
		base_ld_container.setVisibility(View.GONE);
		base_ln_container.setVisibility(View.GONE);
		base_lf_container.setVisibility(View.VISIBLE);
		base_lf_container.getChildAt(0).setVisibility(View.VISIBLE);
		llContent.setVisibility(View.GONE);
	}

	protected void setPageLoadNoData() {
		base_ld_container.setVisibility(View.GONE);
		base_lf_container.setVisibility(View.GONE);
		base_ln_container.setVisibility(View.VISIBLE);
		base_ln_container.getChildAt(0).setVisibility(View.VISIBLE);
		llContent.setVisibility(View.GONE);
	}

	public void setPageLoadingNOdata(int res, String text, OnClickListener l,
									 String btnText) {
		try {
			base_ld_container.setVisibility(View.GONE);
			base_lf_container.setVisibility(View.GONE);
			base_ln_container.setVisibility(View.VISIBLE);
			base_view_load_nodata.setVisibility(View.VISIBLE);
			llContent.setVisibility(View.GONE);
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

	public void setBaseContentLayout(int layoutResId) {
		llContent.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llContent.addView(v);
	}

	public void setBaseContentLayoutWithoutTitle(int layoutResId) {
		toplayout.setVisibility(View.GONE);
		setBaseContentLayout(layoutResId);
	}

	public void setBaseContentLayoutWithoutTitle(View view) {
		toplayout.setVisibility(View.GONE);
		setBaseContentLayout(view);
	}

	public void setBaseContentLayout(View v) {
		llContent.removeAllViews();
		llContent.addView(v);
	}

	protected void startMonitor() {
		if (!isServiceRunning("com.uroad.service.MemoryService")) {
			Intent intent = new Intent();
			intent.setAction("com.uroad.MemoryService");
			startService(intent);
		}
	}

	private boolean isServiceRunning(String serviceName) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	protected void showLongToast(String pMsg) {
		if (!TextUtils.isEmpty(pMsg))
			Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	protected void showShortToast(String pMsg) {
		if (!TextUtils.isEmpty(pMsg))
			Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	/************************** dialog *****************************************/

	protected void showIOSProgressDialog(String msg) {
		DialogHelper.showIOSProgressDialog(msg, this);
	}

	protected void closeIOSProgressDialog() {
		DialogHelper.closeIOSProgressDialog();
	}

	/*************************** ***********************************/

	/*************************** SlidingMenu ************************************/

	protected SlidingMenu getSlidingMenu(int dire, Fragment view) {
		int swidth = getWindowManager().getDefaultDisplay().getWidth();
		SlidingMenu menu1 = new SlidingMenu(this);
		menu1.setMode(dire);
		menu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu1.setShadowWidthRes(R.dimen.base_shadow_width);
		menu1.setBehindOffset(swidth / 2 - 50);
		menu1.setFadeDegree(0.35f);
		menu1.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu1.setMenu(R.layout.base_slidingmenu_layout);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.base_slidingmenu_content, view)
				.commitAllowingStateLoss();
		return menu1;
	}

	protected SlidingMenu getSlidingMenu(int dire, Fragment view, int width) {
		SlidingMenu menu1 = new SlidingMenu(this);
		menu1.setMode(dire);
		menu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu1.setShadowWidthRes(R.dimen.base_shadow_width);
		menu1.setBehindOffset(width);
		menu1.setFadeDegree(0.35f);
		menu1.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu1.setMenu(R.layout.base_slidingmenu_layout);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.base_slidingmenu_content, view)
				.commitAllowingStateLoss();
		return menu1;
	}

	/*************************** SlidingMenu ************************************/

	/*********** ***************/
	protected void pendingTransition_start() {
		ActivityUtil.pendingTransition_start(this);
	}

	protected void pendingTransition_end() {
		ActivityUtil.pendingTransition_end(this);
	}

	/** Activity */
	protected void openActivity(Class<?> pClass) {
		ActivityUtil.openActivity(this, pClass);
	}

	/** Activity */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		ActivityUtil.openActivity(this, pClass, pBundle);
	}

	protected void openHistoryActivity(Class<?> pClass) {
		ActivityUtil.openActivityFromHistory(this, pClass);
	}

	protected void openActivityForResult(Class<?> pClass, Bundle pBundle,
										 int requestCode) {
		ActivityUtil.openActivityForResult(this, pClass, pBundle, requestCode);
	}

	protected void openActivity(String pAction) {
		ActivityUtil.openActivity(this, pAction);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		ActivityUtil.openActivity(this, pAction, pBundle);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		pendingTransition_end();
	}

	public void finish() {
		super.finish();
		pendingTransition_end();
	}

	public void defaultFinish() {
		super.finish();
	}

	/**************************************************** 8 */

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("base", getClass().toString() + " onDestory");
	}
	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// BaseApplication.getInstance().factory.onDestroy();
	// }
	//
	// @Override
	// protected void onPause() {
	// // TODO Auto-generated method stub
	// super.onPause();
	// BaseApplication.getInstance().factory.onPause();
	// }
	//
	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// BaseApplication.getInstance().factory.onResume();
	// }

}
