package suncere.androidappcf.controls;

import java.util.Date;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import suncere.androidappcf.R;
import suncere.androidappcf.app.SuncereAppParameters;
import suncere.androidappcf.components.UpdateManager;
import suncere.androidappcf.tools.DateTimeTool;
import suncere.androidappcf.tools.NetworkUitl;

public abstract class SuncereLanuchActivity  extends SuncereFragmentActivity{

	protected UpdateManager updateManager;
	Date pre;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		pre=DateTimeTool.GetNow();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ImageView launchView=new ImageView(this);
		launchView.setImageResource( this.ContentImageView() );
		this.setContentView(launchView);
		launchView.getLayoutParams().height= LayoutParams.MATCH_PARENT;
		launchView.getLayoutParams().width= LayoutParams.MATCH_PARENT;
		launchView.setScaleType(ScaleType.FIT_XY);
		if( this.IsCheckNetwork()&& !NetworkUitl.GetNewworkState())
		{
			Toast.makeText(this, R.string.uselessnetwork, Toast.LENGTH_SHORT).show();
		}
		updateManager=  new UpdateManager(SuncereLanuchActivity.this,ApkFileName(), SuncereApplication.CurrentApplication().getPackageName() );
		updateManager.setNoneUpdateAction(
					new AsyncTask<Void,Void,Void>(){

						@Override
						protected Void doInBackground(Void... arg0) {
							Intent intent=new Intent();
							Bundle extras=new Bundle();
							extras.putBoolean("IsFirstTime", SuncereAppParameters.IsFirstTime);
							extras.putSerializable("MainActivity", LanuchFinishView());
							intent.putExtras(extras);
							if(SuncereAppParameters.IsFirstTime)
								intent.setClass(SuncereLanuchActivity.this, FirstTimeView());
							else
								intent.setClass(SuncereLanuchActivity.this, LanuchFinishView());
							SuncereAppParameters.SetIsFirstTime(false);
							startActivity(intent);
							overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
							finish();
							
							return null;
						}
					}
			);

		 Log.d("LaunchView", "  start  sleep ");
		new LoadingAction().execute();
	}
		
		
	class LoadingAction extends AsyncTask<Void,Void,Boolean>
	{
		boolean updateCheckRes=false;
		@Override
		protected Boolean doInBackground(Void... arg0) {
			
			boolean result=OnAsyncLanuch();
			if(!result)
			{
				try {
					
					
					if(EnableUpdate())
						updateCheckRes=updateManager.isUpdate(UpdateURL(updateManager.getVersionCode() ));
					else
						updateCheckRes=false;
					 Date after=DateTimeTool.GetNow();
					 float totalSecond = DateTimeTool.TotalSeconds(pre, after);
					 Log.d("LaunchView", totalSecond +"    sleep "+(LanuchStaySecond()-totalSecond));
	//				 float totalSecond = DateTimeTool.TotalSeconds(after, pre);
					 if(totalSecond<LanuchStaySecond()  )
						 Thread.sleep( (int)((LanuchStaySecond()-totalSecond)*1000)  );
					 
	//				 Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result)
		{
			if(result)
			{
				OnCancleLanuch();
			}
			else
			{
				if(updateCheckRes)
				{
					updateManager.showNoticeDialog();
				}
				else
				{
					OnNoneUpdateFalse();
					SuncereAppParameters.SetIsFirstTime(false);
					finish();
				}
			}
		}
	}
	
	protected boolean DisableBackButton()
	{
		return true;
	}
	
	@Override
	public void RefreshViewData() {
		
	}

	protected boolean OnAsyncLanuch()
	{
		return true;
	}
	
	protected void OnCancleLanuch()
	{
		
	}
	
	public boolean IsFullScreen()
	{
		return true;
	}

	/*
	 * ����ʱ����ʱ��
	 */
	protected int LanuchStaySecond()
	{
		return 3;
	}
	
	/*
	 * ����ҳ��
	 */
	protected abstract int ContentImageView();
	
	/*
	 * �����ɹ�����ҳ��
	 */
	protected abstract Class<?> LanuchFinishView() ;
	
	/*
	 * �״����������ҳ��
	 */
	protected Class<?>FirstTimeView()
	{
		return LanuchFinishView();
	}
	
	/*
	 * �Ƿ�������״̬
	 */
	protected boolean  IsCheckNetwork() {
		return false;
	}
	
	/*
	 * ���µ�ַ
	 */
	protected String UpdateURL(String version) {
		return "http://"+ SuncereAppParameters.ServerIpPort+"/api/Update/GetAndroidAPKUpdate?version="+version;
	}
	
	/*
	 * ���ü�����
	 */
	protected boolean EnableUpdate()
	{
		return true;
	}
	
	/*
	 * ������ʧ��ʱ�Ĳ���
	 */
	protected  void  OnNoneUpdateFalse() {
		Intent intent=new Intent();
		Bundle extras=new Bundle();
		extras.putBoolean("IsFirstTime", SuncereAppParameters.IsFirstTime);
		extras.putSerializable("MainActivity", LanuchFinishView());
		intent.putExtras(extras);
		if(SuncereAppParameters.IsFirstTime)
			intent.setClass(SuncereLanuchActivity.this, FirstTimeView());
		else
			intent.setClass(SuncereLanuchActivity.this, LanuchFinishView());
		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
		startActivity(intent);
	}
	
	/*
	 * APK�ļ���
	 */
	protected abstract String ApkFileName() ;
	
}
