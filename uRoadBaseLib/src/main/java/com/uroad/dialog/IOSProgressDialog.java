package com.uroad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.uroad.uroad_ctllib.R;
import com.uroad.util.DensityHelper;

/**
 * @author Administrator
 * 定制的进度框，模仿ios的进度框
 */
public class IOSProgressDialog extends Dialog {
	private Context mContext = null;
	private static IOSProgressDialog dialog = null;

	public IOSProgressDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public IOSProgressDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public static IOSProgressDialog createDialog(Context context) {
		dialog = new IOSProgressDialog(context, R.style.baseCustomDialog);
		dialog.setContentView(R.layout.base_iosdialog);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		return dialog;
	}


	/**
	 * 增加两个参数 用来配置当前dialog是否可以取消
	 * @Title: createDialog
	 * @Description: TODO
	 * @param context
	 * @param canceledontouchoutside    点击框的外面是否可以取消当前的框
	 * @param cancelable            点击返回键是否可以取消当前的框
	 * @return
	 * @return: IOSProgressDialog
	 */
	public static IOSProgressDialog createDialog(Context context,boolean canceledontouchoutside,boolean cancelable) {
		dialog = new IOSProgressDialog(context, R.style.baseCustomDialog);
		dialog.setContentView(R.layout.base_iosdialog);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.setCanceledOnTouchOutside(canceledontouchoutside);
		dialog.setCancelable(cancelable);
		return dialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (dialog == null) {
			return;
		}
		ImageView imageView = (ImageView) findViewById(R.id.loadingImageView);
		LinearLayout llbg = (LinearLayout) findViewById(R.id.llbg);
		int width = DensityHelper.getScreenWidth(mContext);
		LinearLayout.LayoutParams lParams = (LayoutParams) llbg.getLayoutParams();
		lParams.width = width * 3 / 5;
		lParams.height = width * 3 / 5;

		llbg.getBackground().setAlpha(100);
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(2500);
		rotateAnimation.setRepeatCount(-1);
		LinearInterpolator lin = new LinearInterpolator();
		rotateAnimation.setInterpolator(lin);
		imageView.startAnimation(rotateAnimation);
	}

	/**
	 *
	 * [Summary] setMessage 锟斤拷示锟斤拷锟斤拷
	 *
	 * @param strMessage
	 * @return
	 *
	 */
	public IOSProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return dialog;
	}

}
