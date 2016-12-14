package com.uroad.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.uroad.imageUtil.ImageLoader;
import com.uroad.uroad_ctllib.R;

public class WelcomeView extends LinearLayout {

	View view;
	ImageView iv_Pic, iv_Welcome;
	Button btn_skip;
	ImageLoader imgHelper = null;
	Context mContext;
	WelcomeClick welcomeClick;
	Handler handler = new Handler();

	public WelcomeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public WelcomeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	private void init() {
		view = LayoutInflater.from(mContext).inflate(R.layout.view_welcome,
				this, true);

		iv_Pic = (ImageView) view.findViewById(R.id.iv_Pic);
		iv_Welcome = (ImageView) view.findViewById(R.id.iv_Welcome);
		btn_skip = (Button) view.findViewById(R.id.btn_skip);

		String packagename = mContext.getPackageName();
		imgHelper = new ImageLoader(mContext, packagename);
		btn_skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				welcomeClick.onSkipClick();
			}
		});

		iv_Pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				welcomeClick.onPicClick();
			}
		});

	}

	/**
	 * 显示本地加载的加载页
	 * **/
	public void setLocalPic(int ResId) {
		iv_Pic.setImageResource(ResId);
	}

	/**
	 * 显示网络加载的欢迎页
	 * */
	public void setWelcomePic(String url, int resFail) {
		imgHelper.DisplayImage(url, iv_Welcome, resFail, resFail);
		hideAlpha(iv_Pic);
		btn_skip.setVisibility(View.VISIBLE);
	}

	private void hideAlpha(View view) {
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f);
		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(1000);
		animSet.play(anim1);
		animSet.start();
	}

	public void setWelcomeClick(WelcomeClick listener) {
		welcomeClick = listener;
	}

	public void finishPage(long delaytime) {
		handler.postDelayed(runnable, delaytime * 1000);
	}

	public void stopWelcome() {
		handler.removeCallbacks(runnable);
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			welcomeClick.pageFinish();
		}
	};

	public interface WelcomeClick {
		void onSkipClick();

		void onPicClick();

		void pageFinish();
	}

}
