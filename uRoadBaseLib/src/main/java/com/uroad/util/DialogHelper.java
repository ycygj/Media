package com.uroad.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.uroad.dialog.CCTVDialog;
import com.uroad.dialog.CCTVDialog.ICCTVDialogInfaterface;
import com.uroad.dialog.CustomerProgressDialog;
import com.uroad.dialog.IOSProgressDialog;
import com.uroad.entity.CCTV;
import com.uroad.uroad_ctllib.R;

public class DialogHelper {
	static ProgressDialog progress;
	// static IOSProgressDialog dialog;
	static CCTVDialog cctvDialog;
	static CustomerProgressDialog custdialog;

	// static AlertDialog cctvdialog;

	public static void showDialog(Context context, String mess) {
		try {
			// <<<<<<< .mine
			new AlertDialog.Builder(context)
					.setTitle("")
					.setMessage(mess)
					.setNegativeButton("确 定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
													int which) {
								}
							}).show();
			// =======
			// new
			// AlertDialog.Builder(context).setTitle("").setMessage(mess).setNegativeButton("确定",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// }
			// }).show();
			// >>>>>>> .r193
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static CCTVDialog showCCTVDialog(Context mContext, CCTV cctv,
											ICCTVDialogInfaterface face) {

		cctvDialog = new CCTVDialog(mContext);
		WindowManager.LayoutParams params = cctvDialog.getWindow()
				.getAttributes();
		params.width = DensityHelper.getScreenWidth(mContext)
				- DensityHelper.dip2px(mContext, 10);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.type = LayoutParams.FIRST_SUB_WINDOW;
		cctvDialog.getWindow().setAttributes(params);

		cctvDialog.getWindow().setWindowAnimations(
				R.style.base_cctvdialog_style);
		cctvDialog.show();
		cctvDialog.setData(cctv);
		cctvDialog.setInterface(face);
		return cctvDialog;
	}

	public static void showLogoutAlert(final Activity content, final int what,
									   final String msg) {

		DialogHelper.showComfrimDialog(content, "提示", "确定" + msg + "？",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						ProgressDialog
								.show(content, "请等待...", "正在" + msg, true);
						content.finish();
						System.exit(0);
						dialog.dismiss();

					}
				}, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	public static void showTost(Context context, String mess) {
		try {
			if (!TextUtils.isEmpty(mess))
				Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void showProgressDialog(Context c, String msg) {

		try {
			if (progress == null || !progress.isShowing()) {
				progress = new ProgressDialog(c);
				progress.setIndeterminate(true);
				progress.setCancelable(true);
				progress.setCanceledOnTouchOutside(false);
			}
			progress.setMessage(msg);
			if (!progress.isShowing()) {
				progress.show();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	//
	// public static void showCCTVDialog(final Context context,CCTV data){
	// View view = LayoutInflater.from(context).inflate(
	// R.layout.base_cctv_dialog, null);
	// ViewPager pager=(ViewPager) view.findViewById(R.id.base_pager);
	// TextView title=(TextView) view.findViewById(R.id.base_tvcctvdialogTitle);
	// ToggleButton tbFav=(ToggleButton) view.findViewById(R.id.tbFav);
	// Button btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
	// final ImageView imgSplot1=(ImageView) view.findViewById(R.id.imgSplot1);
	// final ImageView imgSplot2=(ImageView) view.findViewById(R.id.imgSplot2);
	// final ImageView imgSplot3=(ImageView) view.findViewById(R.id.imgSplot3);
	// UroadImageView imageView1=new UroadImageView(context);
	// UroadImageView imageView2=new UroadImageView(context);
	// UroadImageView imageView3=new UroadImageView(context);
	// imageView1.setBorder(1,Color.GRAY);
	// imageView2.setBorder(1,Color.GRAY);
	// imageView3.setBorder(1,Color.GRAY);
	// imageView1.setBaseScaleType(ScaleType.FIT_XY);
	// imageView2.setBaseScaleType(ScaleType.FIT_XY);
	// imageView3.setBaseScaleType(ScaleType.FIT_XY);
	// imageView1.setScaleEnabled(false);
	// imageView3.setScaleEnabled(false);
	// imageView2.setScaleEnabled(false);
	// imageView1.setImageUrl(data.getImageurl());
	// imageView2.setImageUrl(data.getImageurl2());
	// imageView3.setImageUrl(data.getImageurl3());
	//
	// List<View> dataList=new ArrayList<View>();
	// dataList.add(imageView1);
	// dataList.add(imageView2);
	// dataList.add(imageView3);
	// BasePageAdapter adapter=new BasePageAdapter(context, dataList);
	// pager.setAdapter(adapter);
	// pager.setOnPageChangeListener(new OnPageChangeListener() {
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// // TODO Auto-generated method stub
	// Bitmap bitmap1=BitmapFactory.decodeResource(context.getResources(),
	// R.drawable.base_ic_pager_index_f1);
	// Bitmap bitmap2=BitmapFactory.decodeResource(context.getResources(),
	// R.drawable.base_ic_pager_index_f2);
	// switch (arg0) {
	// case 0:
	// imgSplot1.setImageBitmap(bitmap2);
	// imgSplot2.setImageBitmap(bitmap1);
	// imgSplot3.setImageBitmap(bitmap1);
	// break;
	// case 1:
	// imgSplot1.setImageBitmap(bitmap1);
	// imgSplot2.setImageBitmap(bitmap2);
	// imgSplot3.setImageBitmap(bitmap1);
	// break;
	// case 2:
	// imgSplot1.setImageBitmap(bitmap1);
	// imgSplot2.setImageBitmap(bitmap1);
	// imgSplot3.setImageBitmap(bitmap2);
	// break;
	//
	// default:
	// break;
	// }
	// }
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// // TODO Auto-generated method stub
	// }
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// // TODO Auto-generated method stub
	// }
	// });
	// title.setText(data.getPoiName());
	//
	//
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(context);
	// builder.setView(view);
	// cctvdialog = builder.create();
	// cctvdialog.getWindow().setWindowAnimations(R.style.base_cctvdialog_style);
	// cctvdialog.show();
	// cctvdialog.setCanceledOnTouchOutside(true);
	// WindowManager.LayoutParams params =
	// cctvdialog.getWindow().getAttributes();
	// params.width =
	// DensityHelper.getScreenWidth(context)-DensityHelper.dip2px(context, 10);
	// params.height =
	// DensityHelper.getScreenWidth(context)+DensityHelper.dip2px(context, 80);
	// cctvdialog.getWindow().setAttributes(params);
	// }
	//

	public static void closeProgressDialog() {
		if (progress != null && progress.isShowing()) {
			try {
				progress.dismiss();
				progress = null;
			} catch (Exception e) {
				progress = null;
			}
		}
	}

	public static void showComfrimDialog(Context c, String title, String msg,
										 DialogInterface.OnClickListener okClickListener,
										 DialogInterface.OnClickListener cancleClickListener) {
		Builder dialog = new AlertDialog.Builder(c).setTitle(title)
				.setMessage(msg).setPositiveButton("确 定", okClickListener);
		if (cancleClickListener != null)
			dialog.setNegativeButton("取消", cancleClickListener);

		dialog.show();
	}

	public static void showComfrimDialog(Context c, String title, String msg,
										 String ok, String cancle,
										 DialogInterface.OnClickListener okClickListener,
										 DialogInterface.OnClickListener cancleClickListener) {

		Builder dialog = new AlertDialog.Builder(c).setTitle(title)
				.setMessage(msg).setNegativeButton(ok, okClickListener);
		if (cancleClickListener != null)
			dialog.setPositiveButton(cancle, cancleClickListener);
		else {
			dialog.setPositiveButton(cancle, null);
		}
		dialog.show();
	}

	public static void showComfrimDialog(Context c, String title, String msg,
										 String ok, DialogInterface.OnClickListener okClickListener) {

		Builder dialog = new AlertDialog.Builder(c).setTitle(title)
				.setMessage(msg).setNegativeButton(ok, okClickListener);
		dialog.show();
	}

	/**
	 * 弃用IOSProgress，改为系统的加载
	 * **/
	public static void showIOSProgressDialog(String msg, Context context) {
		DialogHelper.showProgressDialog(context, msg);
		// if (dialog == null) {
		// dialog = IOSProgressDialog.createDialog(context);
		// dialog.setOnCancelListener(new OnCancelListener() {
		//
		// @Override
		// public void onCancel(DialogInterface dialog) {
		// closeIOSProgressDialog();
		// onIOSDialogCanceled(dialog);
		// }
		// });
		// }
		// dialog.setMessage(msg);
		// try {
		// if (!dialog.isShowing()) {
		// dialog.show();
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
	}

	public static void showCustomerDialog(String msg, Context context) {
		if (custdialog == null) {
			custdialog = CustomerProgressDialog.createDialog(context);
			custdialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					closecusProgressDialog();
				}
			});
		}
		custdialog.setMessage(msg);
		try {
			if (!custdialog.isShowing()) {
				custdialog.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showCustomerDialog(String msg, Context context,
										  boolean canceledontouchoutside, boolean cancelable) {
		if (custdialog == null) {
			custdialog = CustomerProgressDialog.createDialog(context,
					canceledontouchoutside, cancelable);
			custdialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					closecusProgressDialog();
				}
			});
		}
		custdialog.setMessage(msg);
		try {
			if (!custdialog.isShowing()) {
				custdialog.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 增加两个参数配置但前框是否可以取消
	 *
	 * @Title: showIOSProgressDialog
	 * @Description: TODO
	 * @param msg
	 * @param context
	 * @param canceledontouchoutside
	 * @param cancelable
	 * @return: void
	 */
	public static void showIOSProgressDialog(String msg, Context context,
											 boolean canceledontouchoutside, boolean cancelable) {

		DialogHelper.showProgressDialog(context, msg);
		// try {
		// if (dialog == null) {
		// dialog = IOSProgressDialog.createDialog(context,
		// canceledontouchoutside, cancelable);
		// dialog.setOnCancelListener(new OnCancelListener() {
		//
		// @Override
		// public void onCancel(DialogInterface dialog) {
		// closeIOSProgressDialog();
		// onIOSDialogCanceled(dialog);
		// }
		// });
		// }
		// dialog.setMessage(msg);
		// if (!dialog.isShowing()) {
		// dialog.show();
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }

	}

	public static void onIOSDialogCanceled(DialogInterface dialog) {

	}

	public static void closeIOSProgressDialog() {
		DialogHelper.closeProgressDialog();
		// if (dialog != null) {
		// dialog.dismiss();
		// dialog = null;
		// }
	}

	public static void closecusProgressDialog() {
		if (custdialog != null) {
			custdialog.dismiss();
			custdialog = null;
		}
	}
}
