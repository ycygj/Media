/**
 * @Title: CCTVDialog.java
 * @Package com.uroad.dialog
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-8-30 下午7:10:14
 * @version V1.0
 */
package com.uroad.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uroad.adapter.BasePageAdapter;
import com.uroad.common.BaseApplication;
import com.uroad.entity.CCTV;
import com.uroad.uroad_ctllib.R;
import com.uroad.util.DensityHelper;
import com.uroad.widget.image.BitmapDisplayConfig;
import com.uroad.widget.image.UroadImageView;

/**
 * @author Administrator
 * 快拍的弹出dialog类
 */
public class CCTVDialog extends Dialog {

	// public static int MODE_THREE_IMG=0;
	// public static int MODE_ONE_IMG=1;
	ViewPager pager;
	TextView title;
	ToggleButton tbFav;
	Button btnRefresh;
	CCTV cctv;
	ImageView imgSplot1, imgSplot2, imgSplot3;
	Context mcContext;
	List<View> dataList;
	BasePageAdapter adapter;
	ICCTVDialogInfaterface infaterface;
	UroadImageView imageView1, imageView2, imageView3;
	LinearLayout llSplot;

	// int mode=MODE_THREE_IMG;//0为点击弹出三张图片模式

	/**
	 * @param context
	 * @param theme
	 */
	public CCTVDialog(Context context) {
		super(context, R.style.base_cctvdialog_style);
		// TODO Auto-generated constructor stub
		mcContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_cctv_dialog);
		pager = (ViewPager) findViewById(R.id.base_pager);
		title = (TextView) findViewById(R.id.base_tvcctvdialogTitle);
		tbFav = (ToggleButton) findViewById(R.id.tbFav);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		imgSplot1 = (ImageView) findViewById(R.id.imgSplot1);
		imgSplot2 = (ImageView) findViewById(R.id.imgSplot2);
		imgSplot3 = (ImageView) findViewById(R.id.imgSplot3);
		llSplot = (LinearLayout) findViewById(R.id.llSplot);
		setCanceledOnTouchOutside(true);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setSelectSplot(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		dataList = new ArrayList<View>();
		adapter = new BasePageAdapter(mcContext, dataList);
		pager.setAdapter(adapter);
		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (infaterface != null) {
					infaterface.onRightClick(cctv, pager.getCurrentItem());
				}
				if (dataList != null && dataList.size() > 0) {
					UroadImageView imageView = (UroadImageView) dataList
							.get(pager.getCurrentItem());
					String urlString = "";
					if (pager.getCurrentItem() == 0) {
						urlString = cctv.getImageurl();
					} else if (pager.getCurrentItem() == 1) {
						urlString = cctv.getImageurl2();
					} else {
						urlString = cctv.getImageurl3();
					}
					imageView.dispose(true);
					imageView.setImageUrl(urlString);
				}

			}
		});
		// tbFav.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// // TODO Auto-generated method stub
		// if (infaterface != null)
		// infaterface.onLeftClick(cctv, arg0, arg1);
		// }
		// });
		tbFav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CompoundButton arg0 = (CompoundButton) v;
				if (infaterface != null)
					infaterface.onLeftClick(cctv, arg0, arg0.isChecked());
			}
		});
		imageView1 = new UroadImageView(mcContext);
		imageView2 = new UroadImageView(mcContext);
		imageView3 = new UroadImageView(mcContext);
		imageView1.setBorder(1, Color.GRAY);
		imageView2.setBorder(1, Color.GRAY);
		imageView3.setBorder(1, Color.GRAY);
		imageView1.setBaseScaleType(ScaleType.FIT_XY);
		imageView2.setBaseScaleType(ScaleType.FIT_XY);
		imageView3.setBaseScaleType(ScaleType.FIT_XY);
		imageView1.setScaleEnabled(false);
		imageView3.setScaleEnabled(false);
		imageView2.setScaleEnabled(false);
		// if(mode==MODE_THREE_IMG){

		// }else if(mode==MODE_ONE_IMG){ //只有一张图片的模式
		// imageView1 = new UroadImageView(mcContext);
		// imageView1.setBorder(1, Color.GRAY);
		// imageView1.setBaseScaleType(ScaleType.FIT_XY);
		// imageView1.setScaleEnabled(false);
		// dataList.add(imageView1);
		// }

	}

	/**
	 * 刷新图片
	 *
	 * @param data
	 *            新的CCTV
	 * @param pageindex
	 *            :当前为第几页
	 * **/
	public void setRefresh(CCTV data, int pageindex) {
		if (cctv != null) {
			cctv = data;
			setRefreshImg(pageindex);
		}
	}

	private void setRefreshImg(int index) {
		UroadImageView imageView = (UroadImageView) dataList.get(index);
		imageView.setLoading();
		if (index == 0) {
			imageView.setImageUrl(cctv.getImageurl());
			((UroadImageView) dataList.get(1)).setImageUrl(cctv.getImageurl2());
			((UroadImageView) dataList.get(2)).setImageUrl(cctv.getImageurl3());
		} else if (index == 1) {
			imageView.setImageUrl(cctv.getImageurl2());
			((UroadImageView) dataList.get(0)).setImageUrl(cctv.getImageurl());
			((UroadImageView) dataList.get(2)).setImageUrl(cctv.getImageurl3());
		} else {
			imageView.setImageUrl(cctv.getImageurl3());
			((UroadImageView) dataList.get(0)).setImageUrl(cctv.getImageurl());
			((UroadImageView) dataList.get(1)).setImageUrl(cctv.getImageurl2());
		}
		imageView.setEndLoading();
		adapter.notifyDataSetChanged();
	}

	private void setSelectSplot(int arg0) {
		Bitmap bitmap1 = BitmapFactory.decodeResource(mcContext.getResources(),
				R.drawable.base_ic_pager_index_f1);
		Bitmap bitmap2 = BitmapFactory.decodeResource(mcContext.getResources(),
				R.drawable.base_ic_pager_index_f2);
		switch (arg0) {
			case 0:
				imgSplot1.setImageBitmap(bitmap2);
				imgSplot2.setImageBitmap(bitmap1);
				imgSplot3.setImageBitmap(bitmap1);
				break;
			case 1:
				imgSplot1.setImageBitmap(bitmap1);
				imgSplot2.setImageBitmap(bitmap2);
				imgSplot3.setImageBitmap(bitmap1);
				break;
			case 2:
				imgSplot1.setImageBitmap(bitmap1);
				imgSplot2.setImageBitmap(bitmap1);
				imgSplot3.setImageBitmap(bitmap2);
				break;

			default:
				break;
		}
	}

	public void setInterface(ICCTVDialogInfaterface face) {
		infaterface = face;
	}

	public void setData(CCTV data) {
		cctv = data;
		title.setText(data.getPoiName());
		tbFav.setChecked(cctv.getIsfav());
		pager.setCurrentItem(0);
		setSelectSplot(0);
		BitmapDisplayConfig config = new BitmapDisplayConfig();
		config.setBitmapHeight(DensityHelper.getScreenWidth(mcContext)
				- DensityHelper.dip2px(mcContext, 10));
		config.setBitmapWidth(DensityHelper.getScreenWidth(mcContext)
				- DensityHelper.dip2px(mcContext, 60));
		config.setHasAnimation(false);
		if (data.getImageurl() != null && !"".equals(data.getImageurl())) {
//			imageView1.setImageUrlCus(data.getImageurl(), config);
			BaseApplication.baseImageLoader.DisplayImage(data.getImageurl(), imageView1.getImageView(), R.drawable.base_cctv_nodata, false);
			imageView1.setConfig(config);
			dataList.add(imageView1);
			imgSplot1.setVisibility(View.VISIBLE);
		} else {
			imgSplot1.setVisibility(View.INVISIBLE);
		}
		if (data.getImageurl2() != null && !"".equals(data.getImageurl2())) {
//			imageView2.setImageUrlCus(data.getImageurl2(), config);
			BaseApplication.baseImageLoader.DisplayImage(data.getImageurl2(), imageView2.getImageView(), R.drawable.base_cctv_nodata, false);
			imageView2.setConfig(config);
			dataList.add(imageView2);
			imgSplot2.setVisibility(View.VISIBLE);
		} else {
			imgSplot2.setVisibility(View.INVISIBLE);
		}
		if (data.getImageurl3() != null && !"".equals(data.getImageurl3())) {
//			imageView3.setImageUrlCus(data.getImageurl3(), config);
			BaseApplication.baseImageLoader.DisplayImage(data.getImageurl3(), imageView3.getImageView(), R.drawable.base_cctv_nodata, false);
			imageView3.setConfig(config);
			dataList.add(imageView3);
			imgSplot3.setVisibility(View.VISIBLE);
		} else {
			imgSplot3.setVisibility(View.INVISIBLE);
		}
		if (TextUtils.isEmpty(data.getImageurl3())
				&& TextUtils.isEmpty(data.getImageurl2())) {
			llSplot.setVisibility(View.INVISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	public interface ICCTVDialogInfaterface {
		abstract void onLeftClick(CCTV data, CompoundButton arg0,
								  boolean isCheck);

		abstract void onRightClick(CCTV data, int index);

		abstract void delFav(String cctvId);

	}

}
