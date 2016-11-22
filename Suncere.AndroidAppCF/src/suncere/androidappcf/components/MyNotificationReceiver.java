package suncere.androidappcf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import suncere.androidappcf.R;
import suncere.androidappcf.controls.SuncereApplication;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyNotificationReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	
//	private static final String ErrorMark="ErrorMessage";
//	private static final String RefreshViewDatasMark="RefreshViewDatas";

//	private static final String PushType="PushType";
//	private static final String Handler="Handler";
	
	///推送拦截类型
	public enum PushType
	{
		Notify,
		Message,
		Both,
		None,
	}
	
	///全局拦截类型
	protected static PushType  FiltePushType()
	{
		///可用配置进行设置
		return PushType.None;
	}
	
	///记录注册到的处理类
	private static HashMap<String,HashMap<String,Object>> registHandle=new HashMap<String,HashMap<String,Object>>();
	
	///注册推送处理
	public static void RegistPushHandler(String handleMark,PushType pushType,IPushHandler handler)
	{
		HashMap<String,Object> handlerContext=new HashMap<String,Object>();
		handlerContext.put("PushType", pushType);
		handlerContext.put("Handler", handler);
		registHandle.put(handleMark, handlerContext);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            processNotify(context,bundle);
            
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            
            
//        	//打开自定义的Activity
//        	Intent i = new Intent(context, MainActivity.class);
//        	i.putExtras(bundle);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	private void processNotify(Context context,Bundle bundle)
	{
		int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
    	String content=bundle.getString(JPushInterface.EXTRA_ALERT);
    	if(FiltePushType()!= PushType.None)
    		NotificationTool.CloseNotify(notifactionId);
//    	NotificationTool.ShowNotify(content);
    	String mark=GetExtrasValueByKey(bundle,"mark");
    	
    	if(!registHandle.containsKey(mark))return;
		HashMap<String,Object> handlerContext=registHandle.get(mark);
		PushType pt= (PushType) handlerContext.get("PushType");
		
		if(pt!=PushType.Notify&&pt!=PushType.Both)return;
		IPushHandler handler=((IPushHandler)handlerContext.get("Handler"));
		HashMap<String,String> extras=new HashMap<String,String>();
		List<String> extrasKey=handler.GetExtendExtras();
		if(extrasKey!=null)
		{
			for(String key : extrasKey)
			{
				extras.put(key, GetExtrasValueByKey(bundle,key));
			}
		}
		String msg= handler.CreateMessage(content,extras);
		
		if(msg==null)return;
		////此处要修改
		String title="";
		try
		{
			PackageInfo info=SuncereApplication.CurrentApplication().getPackageManager().getPackageInfo(SuncereApplication.CurrentApplication().getPackageName(), 0);
			title=(String) SuncereApplication.CurrentApplication().getPackageManager().getApplicationLabel(info.applicationInfo);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int notifyId= NotificationTool.ShowNotify(handler.GetSkipActivity(), R.drawable.ic_launcher, title, content);
		handler.AfterSendNotification(notifyId);
	}
	
	private void processCustomMessage(Context context, Bundle bundle) 
	{
		String content=bundle.getString(JPushInterface.EXTRA_MESSAGE);
    	
    	//bundle.(JPushInterface.EXTRA_EXTRA)
		 int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
		 if(FiltePushType()!= PushType.None)
			 NotificationTool.CloseNotify(notifactionId);
    	String mark=GetExtrasValueByKey(bundle,"mark");
    	
    	if(!registHandle.containsKey(mark))return;
		HashMap<String,Object> handlerContext=registHandle.get(mark);
		PushType pt= (PushType) handlerContext.get("PushType");
		
		if(pt!=PushType.Message&&pt!=PushType.Both)return;
		IPushHandler handler=((IPushHandler)handlerContext.get("Handler"));
		HashMap<String,String> extras=new HashMap<String,String>();
		List<String> extrasKey=handler.GetExtendExtras();
		if(extrasKey!=null)
		{
			for(String key : extrasKey)
			{
				extras.put(key, GetExtrasValueByKey(bundle,key));
			}
		}
		String msg= handler.CreateMessage(content,extras);
		
		if(msg==null)return;
		////此处要修改
		String title="";
		try {
			PackageInfo info=SuncereApplication.CurrentApplication().getPackageManager().getPackageInfo(SuncereApplication.CurrentApplication().getPackageName(), 0);
			title=(String) SuncereApplication.CurrentApplication().getPackageManager().getApplicationLabel(info.applicationInfo);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		info.v
		int notifyId= NotificationTool.ShowNotify(handler.GetSkipActivity(), R.drawable.ic_launcher , title, content);
		handler.AfterSendNotification(notifyId);
	}
	
	private static String GetExtrasValueByKey(Bundle bundle,String key)
	{
		String result="";
		for (String _key : bundle.keySet()) {
			if(!_key.contains(JPushInterface.EXTRA_EXTRA))continue;
			if (_key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				//sb.append("\nkey:" + _key + ", value:" + bundle.getInt(_key));
			}else if(_key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				//sb.append("\nkey:" + _key + ", value:" + bundle.getBoolean(_key));
			} 
			else {
				//sb.append("\nkey:" + _key + ", value:" + bundle.getString(_key));
				String extras=bundle.getString(_key);
				Pattern p = Pattern.compile("(?<=\""+key+"\":\").*?(?=\")");
				Matcher m=p.matcher(extras);
				if(m.find())
					result=m.group();
				break;
			}
		}
		return result;
	}

	public interface IPushHandler
	{
		void HandlePush();
		///null为不推送，返回推送内容
		String CreateMessage(String values,HashMap<String,String> extras);
		
		void AfterSendNotification(int id);
		
		Activity GetSkipActivity();
		
		List<String>GetExtendExtras();
	}
}
