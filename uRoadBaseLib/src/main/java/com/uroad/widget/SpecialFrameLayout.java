package com.uroad.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author Administrator
 * 
 */
public class SpecialFrameLayout extends FrameLayout {

	private SharedPreferences sp;
	private Context mContext;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SpecialFrameLayout(Context context) {
		super(context);
		mContext = context;
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SpecialFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SpecialFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	private void init() {
		sp = mContext.getSharedPreferences("com.uroad.zhgs",
				mContext.MODE_PRIVATE);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int tag = sp.getInt("positiontag", 0);
		if (tag == 1) {
			super.onLayout(changed, left, top, right, bottom);
			left = sp.getInt("left", 0);
			top = sp.getInt("top", 0);
			right = sp.getInt("right", 0);
			bottom = sp.getInt("bottom", 0);

			View view1 = getChildAt(0);
			View view2 = getChildAt(1);
			View view3 = getChildAt(2);
			View view4 = getChildAt(3);

			int _left = view4.getWidth() / 2 + left - view1.getWidth() / 2;
			int _top = view4.getHeight() / 2 + top - view1.getHeight() / 2;
			int _right = _left + view1.getWidth();
			int _bottom = _top + view1.getHeight();

			view4.layout(left, top, right, bottom);
			view1.layout(_left, _top, _right, _bottom);
			view2.layout(_left, _top, _right, _bottom);
			view3.layout(_left, _top, _right, _bottom);
		} else {
			super.onLayout(changed, left, top, right, bottom);
		}
	}

}
