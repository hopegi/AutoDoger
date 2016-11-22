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
	//��ǰʵ��
	private static SuncereApplication _default;
	
	//�ջ
	private List<Activity> activityStack;
	
	//��ǰ�Ŀ�ˢ����ͼ
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
		//����Activity�������ڻص�
		this.registerActivityLifecycleCallbacks(activityLifecycle);
//		this.TryCreateDataBaseFile();
		//��ʼ��ȫ�ֲ���
		SuncereAppParameters.IniFile="/data/data/"+ getPackageName()+"/ini/app.ini";
		IniAppParameter();
		//ɨ������Auto���Ͳ��
		PluginLoader.Current().ScanAllPlugin();
		//ע��ViewBinder
		AddViewBinder();
		///�������ݿ�
		this.CreateDataBaseFileInner();
	}
	
	//��ǰ�Ŀ�ˢ����ͼ����
	public IAutoRefreshableView CurrAutoRefreshableView()
	{
		return currAutoRefreshableView;
	}
	
	//��ǰ�Ŀ�ˢ����ͼ����
	public void CurrAutoRefreshableView(IAutoRefreshableView value)
	{
		this.currAutoRefreshableView=value;
	}
	
	///�����ջ
	public void PushAcitivty(Activity activity)
	{
		this.activityStack.add(activity);
	}
	
	///������ջ
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
	
	//�˳�App
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
	
	//��ʼ��db����
	//protected abstract void TryCreateDataBaseFile();
	
//	//������Asset�ļ����е��ļ���
//	protected  abstract String AssetsDbFileName();
//	
//	//���浽����Ŀ¼�µ����ݿ���
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

	///�̳߳ش�С
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
