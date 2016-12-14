package com.uroad.util;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

public class DensityHelper {
	/** 
     * ����ֻ�ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * ����ֻ�ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    /** 
     * ����ֻ�ķֱ��ʿ�� 
     */ 
    public static int getScreenWidth(Context context)
    {
    	return ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
    }
    
    /** 
     * ����ֻ�ķֱ��ʸ߶� 
     */ 
    public static int getScreenHeight(Context context)
    {
    	return ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
    }
    
    

	/**
	 * ��ȡ��Ļ�ĳߴ�
	 * @param context
	 * @return
	 */
	public static int[] getScreenSize(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(
						    Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();//��Ļ���
		int height = wm.getDefaultDisplay().getHeight();//��Ļ�߶�
		int[] size = {width,height};
		
		return size;
	}
	
	/**
	 * ��ȡ״̬���߶�
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context){
		int statusBarHeight = 0;
		// ����ֻ��Ҫ��ȡ��Ļ�߶�
		int screenHeight = getScreenSize(context)[1];
		
		switch(screenHeight){
		case 240:
			statusBarHeight = 20;
			break;
		case 480:
			statusBarHeight = 25;
			break;
		case 800:
			statusBarHeight = 38;
			break;
		default:
			break;
		}
		
		return statusBarHeight;
	}
}
