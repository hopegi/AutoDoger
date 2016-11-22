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
	///֪ͨId�б�
	private static final List<Integer> NOTIFY_ID_LIST=new ArrayList<Integer>();
	
	///֪ͨId�ֵ伯
	private static final HashMap<String,Integer> NOTIFY_ID_DIC=new  HashMap<String,Integer>();
	
	static
	{
		int id=GetDefaultNotifyId();
		if(id!=-1)
		{
			RegistNotify("default",id);
		}
	}
	
	///�ر�ָ��id��֪ͨ
	public static void CloseNotify(int notifyId)
	{
		NotificationManager nm=(NotificationManager) SuncereApplication.CurrentApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(notifyId);
	}
	
	///�ر�ָ�����Ƶ�֪ͨ
	public static void CloseNotify(String notifyKey)
	{
		if(!NOTIFY_ID_DIC.containsKey(notifyKey))return ;
		CloseNotify(NOTIFY_ID_DIC.get(notifyKey));
	}
	
	///�ر�����֪ͨ
	public static void CloseNotifyAll()
	{
		NotificationManager nm=(NotificationManager) SuncereApplication.CurrentApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		//nm.cancel(NOTIFY_ID);
		for(Integer notifyId : NOTIFY_ID_LIST)
			nm.cancel(notifyId);
	}
	
	///��ʾ֪ͨ
	public static int ShowNotify(Activity goalActivity,String content)
	{
		return ShowNotify(goalActivity,GetAppIcon(),GetAppName(),content);
	}
	
	public static int ShowNotify(Class<?> goalActivity,String content)
	{
		return ShowNotify(goalActivity,GetAppIcon(),GetAppName(),content);
	}
	
	///��ʾ֪ͨ
	public static int ShowNotify(Activity goalActivity,int icon,String title,String content)
	{
		return ShowNotify(goalActivity,NOTIFY_ID_DIC.get("default"),title,icon,content,true,true,true,false);
	}
	
	public static int ShowNotify(Class<?> goalActivity,int icon,String title,String content)
	{
		return ShowNotify(goalActivity,NOTIFY_ID_DIC.get("default"),title,icon,content,true,true,true,false);
	}
	
	///��ʾ�־�֪ͨ
	public static int ShowAlongNotify(Activity goalActivity,int icon,String title,String content )
	{
		return ShowNotify(goalActivity,NOTIFY_ID_DIC.get("default"),title,icon,content,false,false,false,true);
	}
	
	///��ʾ�־�֪ͨ
	public static int ShowAlongNotify(Activity goalActivity,String content)
	{
		return ShowAlongNotify( goalActivity,GetAppIcon(),GetAppName(), content );
	}
	
	///��ʾ֪ͨ
	public static int ShowNotify(Activity goalActivity,int notifyID,String title,int icon, String content,boolean needSound,boolean needVibrate,boolean needLight,boolean isAlong)
	{
		return ShowNotify(goalActivity.getClass(),notifyID,title,icon,content,needSound,needVibrate,needLight,isAlong);
	}

	public static int ShowNotify(Class<?> goalActivity,int notifyID,String title,int icon, String content,boolean needSound,boolean needVibrate,boolean needLight,boolean isAlong)
	{

		NotificationManager nm=(NotificationManager) SuncereApplication.CurrentApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notify=new Notification(icon,content,System.currentTimeMillis());
		
		Intent intent=new Intent(SuncereApplication.CurrentApplication(),goalActivity);
		intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		PendingIntent pendingIntent=PendingIntent.getActivity(SuncereApplication.CurrentApplication(), 0, intent, 0);
		//notify.contentIntent=pendingIntent;
		notify.setLatestEventInfo(SuncereApplication.CurrentApplication(), title, content, pendingIntent);
		if(needSound)
		{
			notify.defaults|=Notification.DEFAULT_SOUND;
		}
		if(needVibrate)
		{
			notify.defaults|=Notification.DEFAULT_VIBRATE;
			notify.vibrate=new long[]{100,250,100,500};
		}
		if(needLight)
		{
			notify.defaults|=Notification.DEFAULT_LIGHTS;
		}
		if(isAlong)
		{
			notify.flags|=Notification.FLAG_NO_CLEAR;
		}
		else
		{
			notify.flags|=Notification.FLAG_AUTO_CANCEL;
		}
		
		nm.notify(notifyID, notify);
		return notifyID;
	}
	
	///ע��֪ͨ
	public static void RegistNotify(String key,int notifyId)
	{
		NOTIFY_ID_DIC.put(key, notifyId);
		NOTIFY_ID_LIST.add(notifyId);
	}
	
	///��appmainfest��ȡֵ
	
	///ͨ�ط���ӳ��������Դ�ļ��л�ȡ��Ӧ������ֵ R.String R.darwable.icon
	
	///ͨ�������ȡIcon����ԴID
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
	
	///ͨ��App
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
	
	///��manifest�л�ȡĬ��֪ͨid
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
