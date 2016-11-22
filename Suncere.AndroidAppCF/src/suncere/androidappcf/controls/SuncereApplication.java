package suncere.androidappcf.controls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import suncere.androidapp.autobasemodule.PluginLoader;
import suncere.androidapp.viewautobinder.BindDataViewBinder;
import suncere.androidapp.viewautobinder.ImageViewBinder;
import suncere.androidapp.viewautobinder.ListViewBinder;
import suncere.androidapp.viewautobinder.TextViewBinder;
import suncere.androidapp.viewautobinder.ViewBinder;
import suncere.androidapp.viewautobinder.ViewBinderProvider;
import suncere.androidapp.viewautobinder.ViewPagerBinder;
import suncere.androidappcf.app.SuncereAppParameters;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

public abstract class SuncereApplication extends Application
{	
	//当前实例
	private static SuncereApplication _default;
	
	//活动栈
	private List<Activity> activityStack;
	
	//当前的可刷新视图
	private IAutoRefreshableView currAutoRefreshableView;
	
//	private Context context;
	
	
	private Activity currentActivity;
	
	public SuncereApplication()
	{
//		super();
		activityStack=new ArrayList<Activity>();
	}
	
	
//	public SuncereApplication(Context context)
//	{
//		super();
//		this.context=context;
//	}
	
//	public String getPackageName()
//	{
//		String result=null;
//		try
//		{
//			result=super.getPackageName();
//		}
//		catch(Exception ex)
//		{
//			
//		}
//		if(result==null&&this.context!=null)
//		{
//			result=this.context.getPackageName();
//		}
//		return result;
//	}
	
	public void onCreate()
	{
		_default=this;
		//设置Activity生命周期回调
		this.registerActivityLifecycleCallbacks(activityLifecycle);
//		this.TryCreateDataBaseFile();
		//初始化全局参数
		SuncereAppParameters.IniFile="/data/data/"+ getPackageName()+"/ini/app.ini";
		IniAppParameter();
		//扫描所有Auto类型插件
		PluginLoader.Current().ScanAllPlugin();
		//注册ViewBinder
		AddViewBinder();
		///创建数据库
		this.CreateDataBaseFileInner();
	}
	
	//当前的可刷新视图属性
	public IAutoRefreshableView CurrAutoRefreshableView()
	{
		return currAutoRefreshableView;
	}
	
	//当前的可刷新视图属性
	public void CurrAutoRefreshableView(IAutoRefreshableView value)
	{
		this.currAutoRefreshableView=value;
	}
	
	///活动加入活动栈
	public void PushAcitivty(Activity activity)
	{
		this.activityStack.add(activity);
	}
	
	///活动弹出活动栈
	public Activity PopActivity()
	{
		Activity activity=null;
		if(this.activityStack.size()>0)
		{
			activity=this.activityStack.get(this.activityStack.size()-1);
			this.activityStack.remove(activity);
		}
		return activity;
	}
	
	//退出App
	public void ExitApp()
	{
		while(this.activityStack.size()>0)
			this.PopActivity().finish();
	}
	
	public Activity CurrentActivity()
	{
		return currentActivity;
//		Activity activity=null;
//		if(this.activityStack.size()>0)
//		{
//			activity=this.activityStack.get(this.activityStack.size()-1);
//		}
//		return activity;
	}
	
	public static SuncereApplication CurrentApplication()
	{
//		if(_default==null)
//			_default=new SuncereApplication();
		return _default;
	}
	
	//初始化db操作
	//protected abstract void TryCreateDataBaseFile();
	
//	//保存在Asset文件夹中的文件名
//	protected  abstract String AssetsDbFileName();
//	
//	//保存到程序目录下的数据库名
//	protected abstract String SaveDbName();
	
	protected abstract void IniAppParameter();
	
	protected void CreateDataBaseFileInner()
	{
		String path="/data/data/"+ getPackageName()+"/databases";
		File file=new File(path);
		if(!file.exists()){
			file.mkdir();
				try {
					file.createNewFile();
					OutputStream os=new FileOutputStream(path+"/"+SuncereAppParameters.DbName);
					InputStream is=getResources().getAssets().open(SuncereAppParameters.AssetsDbFileName);
					int count;
					byte[] buffer=new byte[1024];
					while((count=is.read(buffer))>0)
						os.write(buffer,0,count);
					os.close();
					is.close();

					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	///线程池大小
	public Integer ThreadPoolSize()
	{
		return null;
	}
	
	protected void AddViewBinder()
	{
		ViewBinderProvider.Current().AddViewBinder(new ViewBinder());
		ViewBinderProvider.Current().AddViewBinder(new ListViewBinder());
		ViewBinderProvider.Current().AddViewBinder(new ViewPagerBinder());
		ViewBinderProvider.Current().AddViewBinder(new TextViewBinder());
		ViewBinderProvider.Current().AddViewBinder(new ImageViewBinder());
		ViewBinderProvider.Current().AddViewBinder(new BindDataViewBinder());
	}
	
	private ActivityLifecycleCallbacks activityLifecycle=new ActivityLifecycleCallbacks(){

		@Override
		public void onActivityCreated(Activity activity,
				Bundle savedInstanceState) {
			currentActivity=activity;
		}

		@Override
		public void onActivityStarted(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityResumed(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityPaused(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStopped(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity,
				Bundle outState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			
		}};
}
