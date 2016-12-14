package com.uroad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uroad.uroad_ctllib.R;
import com.uroad.util.DensityHelper;

/*
 * 定制的进度对话框
 * */
public class CustomerProgressDialog extends Dialog {

	private Context mContext = null;
	private static CustomerProgressDialog dialog = null;

	public CustomerProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomerProgressDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public static CustomerProgressDialog createDialog(Context context) {
		dialog = new CustomerProgressDialog(context, R.style.baseCustomDialog);
		dialog.setContentView(R.layout.cusprogressdialoglayout);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		return dialog;
	}

	public static CustomerProgressDialog createDialog(Context context, boolean canceledontouchoutside, boolean cancelable) {
		dialog = new CustomerProgressDialog(context, R.style.baseCustomDialog);
		dialog.setContentView(R.layout.cusprogressdialoglayout);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.setCanceledOnTouchOutside(canceledontouchoutside);
		dialog.setCancelable(cancelable);
		return dialog;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (dialog == null) {
			return;
		}
		ImageView imageView = (ImageView) findViewById(R.id.ivloading);
		RelativeLayout llbg = (RelativeLayout) findViewById(R.id.rldialog);
		int width = DensityHelper.getScreenWidth(mContext);
		int height = DensityHelper.getScreenHeight(mContext);
		LinearLayout.LayoutParams lParams = (android.widget.LinearLayout.LayoutParams) llbg.getLayoutParams();
		lParams.width = (int) (width * (3 / 4.0));
		lParams.height = (int) (height * (1 / 6.0))-50;

		llbg.getBackground().setAlpha(160);
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1500);
		rotateAnimation.setRepeatCount(-1);
		LinearInterpolator lin = new LinearInterpolator();
		rotateAnimation.setInterpolator(lin);
		imageView.startAnimation(rotateAnimation);
	}

	public CustomerProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) dialog.findViewById(R.id.tvshowtxt);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return dialog;
	}

}
