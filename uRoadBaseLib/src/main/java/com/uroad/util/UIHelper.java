package com.uroad.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UIHelper {
	/**
	 * 计算ListView的高度
	 *
	 * @param listView
	 */
	public static void setListViewHeight(ListView listView) {
		// 获取ListView对应的Adapter

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			// pre-condition
			return;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

			View listItem = listAdapter.getView(i, null, listView);

			// if (listItem != null) {
			listItem.measure(0, 0); // 计算子项View 的宽高

			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			// }

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// listView.getDividerHeight()获取子项间分隔符占用的高度

		// params.height最后得到整个ListView完整显示需要的高度
		// params.height = height;
		listView.setLayoutParams(params);
		// listView.invalidate();
	}
}
