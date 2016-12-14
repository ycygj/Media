/**
 * @Title: CCTVGridViewAdapter.java
 * @Package com.uroad.gst.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-8-29 下午3:09:15
 * @version V1.0
 */
package com.uroad.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.uroad.common.BaseApplication;
import com.uroad.dialog.CCTVDialog;
import com.uroad.dialog.CCTVDialog.ICCTVDialogInfaterface;
import com.uroad.entity.CCTV;
import com.uroad.fragments.CCTVGridViewFragment;
import com.uroad.uroad_ctllib.R;
import com.uroad.util.DensityHelper;
import com.uroad.util.ObjectHelper;
import com.uroad.widget.image.UroadImageView;

/**
 * @author Administrator
 * 快拍的通用适配器，一般配合　CCTVGridViewFragment及其子类使用
 */
public class CCTVGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	List<CCTV> dataCctvNews;
	CCTVDialog dialog;
	ICCTVDialogInfaterface face;
	int mode = CCTVGridViewFragment.MODE_THREE_IMG;
	iBeforeItemClick beforeItemClick;

	public CCTVGridViewAdapter(Context context, List<CCTV> list,
							   ICCTVDialogInfaterface cctvinterface, int imgMode) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		dataCctvNews = list;
		face = cctvinterface;
		mode = imgMode;
	}

	public void setBeforeInterface(iBeforeItemClick itemClick) {
		beforeItemClick = itemClick;
	}

	@Override
	public int getCount() {
		return dataCctvNews.size();
	}

	@Override
	public Object getItem(int position) {
		return dataCctvNews.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.base_cctvfragment_layout,
					null);
			holder.cimageView = (UroadImageView) convertView
					.findViewById(R.id.cImageView1);
			holder.tvSnapDescrition = (TextView) convertView
					.findViewById(R.id.tvSnapDescrition);
			holder.imgDelete = (ImageView) convertView
					.findViewById(R.id.imgDelete);

			int displaywidth = DensityHelper.getScreenWidth(mContext);
			int width = displaywidth / 2;
			int height = (int) (width * (((double) 3) / ((double) 4)));
			FrameLayout.LayoutParams layParams = new FrameLayout.LayoutParams(
					width - 20, height);
			holder.cimageView.setLayoutParams(layParams);
			holder.cimageView.setBaseScaleType(ScaleType.FIT_XY);
			holder.cimageView.setScaleEnabled(false);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setData(holder, position);
		return convertView;
	}

	public void setRefresh(CCTV cctv, int pageindex) {
		if (dialog != null) {
			dialog.setRefresh(cctv, pageindex);
		}
	}

	private void setData(final ViewHolder holder, int position) {
		final CCTV cctvNew = dataCctvNews.get(position);
		holder.tvSnapDescrition.setText(cctvNew.getPoiName());
		if (!ObjectHelper.isEmpty(cctvNew.getImageurl())) {
			holder.cimageView.setEndLoading();
			holder.cimageView.setImageUrlNotCache(cctvNew.getImageurl(),
					R.drawable.base_cctv_nodata);
		} else {
			holder.cimageView.setLoading();
			holder.cimageView.setImageUrlNotCache(cctvNew.getImageurl(),
					R.drawable.base_cctv_nodata);
		}
		holder.cimageView.setText(cctvNew.getPoiName());

		holder.cimageView.setTag(cctvNew);
		holder.cimageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				picClick(arg0);
			}
		});

		if (cctvNew.getTag() != null
				&& cctvNew.getTag().toString().equalsIgnoreCase("showdelelte")) {
			holder.imgDelete.setVisibility(View.VISIBLE);
			holder.imgDelete.setTag(cctvNew.getPoiId());
			holder.imgDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v.getTag() != null) {
						face.delFav(v.getTag().toString());
					}
				}
			});
		} else {
			holder.imgDelete.setVisibility(View.GONE);
		}
	}

	public void picClick(View arg0) {
		CCTV tagCctv = (CCTV) arg0.getTag();
		if (beforeItemClick != null) {
			beforeItemClick.beforeLoad(tagCctv, arg0);
		}
		if (mode == CCTVGridViewFragment.MODE_THREE_IMG) {
			if (dialog == null) {
				dialog = new CCTVDialog(mContext);
				WindowManager.LayoutParams params = dialog.getWindow()
						.getAttributes();
				params.width = DensityHelper.getScreenWidth(mContext)
						- DensityHelper.dip2px(mContext, 10);
				params.height = DensityHelper.getScreenWidth(mContext)
						+ DensityHelper.dip2px(mContext, 60);
				dialog.getWindow().setAttributes(params);
			}
			dialog.getWindow().setWindowAnimations(
					R.style.base_cctvdialog_style);
			dialog.show();
			dialog.setData(tagCctv);
			dialog.setInterface(face);
		} else if (mode == CCTVGridViewFragment.MODE_ONE_IMG) {
			/** 要先设置文字再全屏才能显示文字 **/
			((UroadImageView) arg0).toggleFillScreen(tagCctv.getImageurl());
		}
	}

	class ViewHolder {
		UroadImageView cimageView;
		TextView tvSnapDescrition;
		ImageView imgDelete;
	}

	public interface iBeforeItemClick {
		abstract void beforeLoad(CCTV cctv, View dialog);
	}

}
