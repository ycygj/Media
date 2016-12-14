package com.uroad.pulltorefresh;

import com.uroad.uroad_ctllib.R;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FootLoadingLayout extends RelativeLayout {

	Context mContext;
	ProgressBar pbLoading;
	TextView tvLoadMsg;
	private String mPullLabel;
	private String mRefreshingLabel;
	private String mReleaseLabel;

	public FootLoadingLayout(Context context) {
		super(context);
		mContext = context;
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		// TODO Auto-generated method stub
		LayoutInflater.from(mContext).inflate(
				R.layout.base_pulltorefresh_footer, this, true);

		mPullLabel="加载更多";
		mRefreshingLabel="正在加载中......";
		mReleaseLabel="加载完成";
		pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
		tvLoadMsg = (TextView) findViewById(R.id.tvLoadMsg);

	}

	public void setPullLabel(String pullLabel) {
		mPullLabel = pullLabel;
	}

	public void setRefreshingLabel(String refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	public void refreshing() {
		tvLoadMsg.setText(wrapHtmlLabel(mRefreshingLabel));
		// mHeaderImage.startAnimation(mRotateAnimation);
		pbLoading.setVisibility(View.VISIBLE);
		// mArrowImageView.setVisibility(View.GONE);
		// mSubHeaderText.setVisibility(View.GONE);
	}

	private CharSequence wrapHtmlLabel(String label) {

		if (!isInEditMode()) {
			return Html.fromHtml(label);
		} else {
			return label;
		}
	}

	public void pullToRefresh() {
		tvLoadMsg.setText(wrapHtmlLabel(mPullLabel));

	}

	public void releaseToRefresh() {
		// TODO Auto-generated method stub
		tvLoadMsg.setText(wrapHtmlLabel(mReleaseLabel));

		// mArrowImageView.startAnimation(mImageRotateAnimation);
	}

	public void reset() {
		// TODO Auto-generated method stub
		tvLoadMsg.setText(wrapHtmlLabel(mPullLabel));
		pbLoading.setVisibility(View.INVISIBLE);

	}
}
