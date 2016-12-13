package suncere.androidappcf.components;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.controls.SuncereApplication;
import android.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;


public class NotificationTool {

	//static final int NOTIFY_ID=1512189319;
	///通知Id列表
	private static final List<Integer> NOTIFY_ID_LIST=new ArrayList<Integer>();
	
	///通知Id字典集
	private static final HashMap<String,Integer> NOTIFY_ID_DIC=new  HashMap<String,Integer>();
	
	static
	{
		int id=GetDefaultNotifyId();
		if(id!=-1)
		{
			RegistNotify("default",id);
		}
	}
	
	///关闭指定id的通知
	public static void CloseNotify(int notifyId)
	{
		NotificationManager nm=(NotificationManager) SuncereApplication.CurrentApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(notifyId);
	}
	
	///关闭指定名称的通知
	public static void CloseNotify(String notifyKey)
	{
		if(!NOTIFY_ID_DIC.containsKey(notifyKey))return ;
		CloseNotify(NOTIFY_ID_DIC.get(notifyKey));
	}
	
	///关闭所有通知
	public static void CloseNotifyAll()
	{
		NotificationManager nm=(NotificationManager) SuncereApplication.CurrentApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		//nm.cancel(NOTIFY_ID);
		for(Integer notifyId : NOTIFY_ID_LIST)
			nm.cancel(notifyId);
	}
	
	///显示通知
	public static int ShowNotify(Activity goalActivity,String content)
	{
		return ShowNotify(goalActivity,GetAppIcon(),GetAppName(),content);
	}
	
	public static int ShowNotify(Class<?> goalActivity,String content)
	{
		return ShowNotify(goalActivity,GetAppIcon(),GetAppName(),content);
	}
	
	///显示通知
	public static int ShowNotify(Activity goalActivity,int icon,String title,String content)
	{
		return ShowNotify(goalActivity,NOTIFY_ID_DIC.get("default"),title,icon,content,true,true,true,false);
	}
	
	public static int ShowNotify(Class<?> goalActivity,int icon,String title,String content)
	{
		return ShowNotify(goalActivity,NOTIFY_ID_DIC.get("default"),title,icon,content,true,true,true,false);
	}
	
	///显示持久通知
	public static int ShowAlongNotify(Activity goalActivity,int icon,String title,String content )
	{
		return ShowNotify(goalActivity,NOTIFY_ID_DIC.get("default"),title,icon,content,false,false,false,true);
	}
	
	///显示持久通知
	public static int ShowAlongNotify(Activity goalActivity,String content)
	{
		return ShowAlongNotify( goalActivity,GetAppIcon(),GetAppName(), content );
	}
	
	///显示通知
	public static int ShowNotify(Activity goalActivity,int notifyID,String title,int icon, String content,boolean needSound,boolean needVibrate,boolean needLight,boolean isAlong)
	{
		return ShowNotify(goalActivity.getClass(),notifyID,title,icon,content,needSound,needVibrate,needLight,isAlong);
	}

	public static int ShowNotify(Class<?> goalActivity,int notifyID,String title,int icon, String content,boolean needSound,boolean needVibrate,boolean needLight,boolean isAlong)
	{

		NotificationManager nm=(NotificationManager) SuncereApplication.CurrentApplication().getSystemService(Context.NOTIFICATION_SERVICE);


		Intent intent=new Intent(SuncereApplication.CurrentApplication(),goalActivity);
		intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		PendingIntent pendingIntent=PendingIntent.getActivity(SuncereApplication.CurrentApplication(), 0, intent, 0);
		//notify.contentIntent=pendingIntent;
		//	Notification notify=new Notification(icon,content,System.currentTimeMillis());   // 1. 框架修改  hjo:2016-11-16    理由：过时,无法在当前版本使用
       //   notify.setLatestEventInfo(SuncereApplication.CurrentApplication(), title, content, pendingIntent);//2. 框架修改  hjo:2016-11-16    理由：过时,无法在当前版本使用
		/********以上对 1 和 2 的替代**************/

		Notification.Builder notify = new Notification.Builder(SuncereApplication.CurrentApplication());

		notify.setSmallIcon(icon);//设置图片
		notify.setAutoCancel(true);
		notify.setContentTitle(title);//设置标题
		notify.setContentText(content);//设置内容
		notify.setContentIntent(pendingIntent);//设置pendingIntent




		if(needSound)
		{
			notify.setDefaults(Notification.DEFAULT_SOUND); //替代
//			notify.defaults|=Notification.DEFAULT_SOUND; // 框架修改  hjo:2016-11-16    理由：过时,无法在当前版本使用
		}
		if(needVibrate)
		{
			notify.setDefaults(Notification.DEFAULT_SOUND); //替代
			notify.setVibrate(new long[]{100,250,100,500}); //替代

//			notify.defaults|=Notification.DEFAULT_VIBRATE; //2. 框架修改  hjo:2016-11-16    理由：过时,无法在当前版本使用
//			notify.vibrate=new long[]{100,250,100,500};//2. 框架修改  hjo:2016-11-16    理由：过时,无法在当前版本使用
		}
		if(needLight)
		{
			notify.setDefaults(Notification.DEFAULT_SOUND); //替代
//			notify.defaults|=Notification.DEFAULT_LIGHTS;//2. 框架修改  hjo:2016-11-16    理由：过时,无法在当前版本使用
		}
		if(isAlong)
		{
               //无替代
//			notify.flags=Notification.FLAG_AUTO_CANCEL; //2. 框架修改  hjo:2016-11-16    理由：过时,无法在当前版本使用
		}
		
		nm.notify(notifyID, notify.build());
		return notifyID;
	}
	
	///注册通知
	public static void RegistNotify(String key,int notifyId)
	{
		NOTIFY_ID_DIC.put(key, notifyId);
		NOTIFY_ID_LIST.add(notifyId);
	}
	
	///从appmainfest读取值
	
	///通关反射从程序包的资源文件中获取相应的配置值 R.String R.darwable.icon
	
	///通过反射获取Icon的资源ID
	public static int GetAppIcon() 
	{
		int result=-1;
		String packName= SuncereApplication.CurrentApplication().getPackageName();
		Class<?> rType=null;
		
		try {
			rType= Class.forName(packName+".R$drawable");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rType==null)return result;
		try {
			//Field drawable=rType.getDeclaredField("drawable");
			//Field icon= drawable.getClass().getDeclaredField("ic_launcher");
			
			Field icon=rType.getDeclaredField("ic_launcher");
			result=icon.getInt(null);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	///通过App
	public static String GetAppName()
	{
		String result="";
		

		String packName=SuncereApplication.CurrentApplication().getPackageName();
		try {
			PackageManager manager= SuncereApplication.CurrentApplication().getPackageManager();
			ApplicationInfo appInfo =SuncereApplication.CurrentApplication().getPackageManager().getApplicationInfo(packName, 0);
			result=(String) manager.getApplicationLabel(appInfo);
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	///从manifest中获取默认通知id
	public static int GetDefaultNotifyId()
	{
		String packName=SuncereApplication.CurrentApplication().getPackageName();
		int result=-1;
		try {
			ApplicationInfo appInfo=SuncereApplication.CurrentApplication().getPackageManager().getApplicationInfo(packName, PackageManager.GET_META_DATA);
			result=appInfo.metaData.getInt("SUNCERE_DEFAULT_NOTIFY_ID");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			result=-1;
			e.printStackTrace();
		}
		return result;
	}
}
