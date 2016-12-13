package suncere.androidappcf.tools;

import android.app.Activity;
import android.util.DisplayMetrics;

///屏幕像素大小适配工具
public class ScreenSuitableTool 
{	
	private static boolean hasIni=false;
	
	public static int ScreenWidthPixels;
	
	public static int ScreenHeightPixels;
	
	public static float ScreenDensity;
	
	public static int ScreenDensityDpi;
	
	public static void MeasureScreen(Activity activity )
	{
		if(hasIni)return;
		DisplayMetrics metric = new DisplayMetrics();
//		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		activity.getWindowManager().getDefaultDisplay().getRealMetrics(metric);
		int width = metric.widthPixels;  // 屏幕宽度（像素）
		int height = metric.heightPixels;  // 屏幕高度（像素）
		float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
		ScreenHeightPixels=width;
		ScreenWidthPixels=height;
		ScreenDensity=density;
		ScreenDensityDpi=densityDpi;
		hasIni=true;
	}
	
	public static float ConvertSuitableDpi(int samsungSize) 
	{
		return samsungSize*1f/480*ScreenDensityDpi;
//		return samsungSize;
	}
	
	public static float ConvertSuitableDpi(float samsungSize) 
	{
		return samsungSize/480*ScreenDensityDpi;
//		return samsungSize;
	}
}
