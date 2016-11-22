package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.List;

import suncere.androidappcf.tools.ScreenSuitableTool;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public abstract class SuncereFragmentActivity extends FragmentActivity implements IAutoRefreshableView
{
	private AlertDialog.Builder exitMsgBox;
	
	///�ϴ��˳�ʱ��
	private long exitTime = 0;
	
	private MapView relateMapView;
	
	///�Ƿ������ҳ��
	protected boolean IsMainActivity()
	{
		return false;
	}
	
	///�Ƿ������˵�ͼ
	protected boolean IsContainMap()
	{
		return false;
	}
	
	///�Ƿ�ȫ��
	protected boolean IsFullScreen()
	{
		return false;
	}

	protected boolean IsUseJPush()
	{
		return false;
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		///ִ�е�ͼ��ʼ��
		if(this.IsContainMap())
		{
			SDKInitializer.initialize(getApplicationContext());  
		}
		if(this.IsUseJPush())
		{
			JPushInterface.setDebugMode(true);
			JPushInterface.init(SuncereApplication.CurrentApplication().getBaseContext());
		}
		
		//ִ��ȫ��
		if(this.IsFullScreen())
		{
			 if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
		            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		        }
		}
		ScreenSuitableTool.MeasureScreen(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	    //if(this.IsMainActivity()&& keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	    	if(this.DisableBackButton())return false;
	    	if(this.IsMainActivity()&&  this.UseTosatExistApp())
	    	{
	    		ShowExitToast();
		        return false;   
	    	}
	    	if(this.IsMainActivity()&&this.UseDialogExistApp())
	    	{
	    		ShowExitMsgBox();
	    		return false;
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	protected void ShowExitToast()
	{
		 if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_LONG).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {

	        	SuncereApplication.CurrentApplication().ExitApp();
	        	this.finish();

	        }
	}
	
	protected void ShowExitMsgBox()
	{
		if(exitMsgBox==null){
			exitMsgBox=new AlertDialog.Builder(this,AlertDialog.THEME_TRADITIONAL);
			exitMsgBox.setTitle("��ʾ").setMessage("ȷ��Ҫ�˳���").setNeutralButton ("ȷ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					
					SuncereApplication.CurrentApplication().ExitApp();
					SuncereFragmentActivity.this.finish();
					
				}
			}).setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}
		exitMsgBox.show();
	}
	
    public int getStatusHeight(){
        int statusHeight = 0;
        Rect localRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
	
    public int getSimpleStatusHeight()
    {
    	int statusHeight = 0;
        Rect localRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        return statusHeight;
    }
	
	public void onResume()
	{

		if(this.IsContainMap()&&relateMapView==null)
		{
			List<View> views= GetAllViews();
			for(View view : views)
			{
				if(view instanceof MapView)
				{
					this.relateMapView=(MapView)view;
					break;
				}
			}
		}
		
		super.onResume();
		if(this.IsContainMap()&&relateMapView!=null)
		{
			relateMapView.onResume();
		}
		
		if(this.IsUseJPush())
		{
			JPushInterface.onResume(this);
		}
		
		RefreshViewData();
	}
	
		public void onPause()
		{
			
			super.onPause();
			
			if(this.IsContainMap()&&relateMapView!=null)
			{
				relateMapView.onPause();
			}
			
			if(this.IsUseJPush())
			{
				JPushInterface.onPause(this);
			}
		}
		
		
		//ˢ����ͼ����
		public abstract void RefreshViewData();
		
//		///������
//		public void BindData()
//		{
//			
//		}
//		
//		///��ȡ����
//		public void GetData(boolean isCache)
//		{
//			
//		}
		
		public void startActivity(Intent intent)
		{
			super.startActivity(intent);
			//��ӵ�ǰAcitivy��ջ��
			if(SuncereApplication.CurrentApplication()!=null)
				SuncereApplication.CurrentApplication().PushAcitivty(this);
		}
		
		public void finish()
		{
			super.finish();
			//��ջ��ȥ��Activity
			if(SuncereApplication.CurrentApplication()!=null)
				SuncereApplication.CurrentApplication().PopActivity();
		}
		
		private List<View> GetAllViews()
		{
			List<View> result=new ArrayList<View>();
			View view=this.getWindow().getDecorView();
			
			List<View> viewStack=new ArrayList<View>();
			viewStack.add(0, view);
			View temp;
			ViewGroup vg;
			while(viewStack.size()!=0)
			{
				temp=viewStack.remove(0);
				if(temp instanceof ViewGroup)
				{
					vg=(ViewGroup)temp; 
					for(int i=0;i<vg.getChildCount();i++)
						viewStack.add(0, vg.getChildAt(i));
				}
				result.add(temp);
			}
			//if(view instanceof ViewGroup)
			
			return result;
		}
		
		protected boolean UseTosatExistApp()
		{
			return false;
		}
		
		protected boolean UseDialogExistApp()
		{
			return false;
		}
		
		protected boolean DisableBackButton()
		{
			return false;
		}
}
