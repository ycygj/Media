/**
 * @Title: HalfScreenPopuWindow.java
 * @Package com.uroad.dialog
 * @Description: 这是一个半屏的弹出框,有几个选项和一个取消按钮
 * @author oupy
 * @date 2013-12-4 下午6:12:38
 * @version V1.0
 */
package com.uroad.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.uroad.uroad_ctllib.R;

/**
 * @author Administrator
 * 定制的半透明弹出框
 */
public class HalfScreenPopuWindow extends PopupWindow {

	ListView lvHP;
	Button btn_cancel;

	public HalfScreenPopuWindow(Context context,
								OnItemClickListener clickListener, List<String> items) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.base_popuwindow_halfscreen, null);
		lvHP = (ListView) view.findViewById(R.id.lvHP);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (String string : items) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", string);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(context, list,
				R.layout.base_textview, new String[] { "name" },
				new int[] { R.id.btnItem });
		lvHP.setAdapter(adapter);
		lvHP.setOnItemClickListener(clickListener);


		this.setContentView(view);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setClippingEnabled(true);
		this.setAnimationStyle(R.style.base_AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
	}


	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {

		super.showAtLocation(parent, gravity, x, y);
	}



}
