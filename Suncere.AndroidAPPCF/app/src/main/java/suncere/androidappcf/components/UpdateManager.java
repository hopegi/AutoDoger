package suncere.androidappcf.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import suncere.androidappcf.R;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.tools.InputStreamUtil;
import suncere.androidappcf.tools.NetworkUitl;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class UpdateManager 
{
    /* ������ */
    private static final int DOWNLOAD = 1;
    /* ���ؽ��� */
    private static final int DOWNLOAD_FINISH = 2;
    /* ���������XML��Ϣ */
    HashMap<String, String> mHashMap;
    /* ���ر���·�� */
    private String mSavePath;
    /* ��¼���������� */
    private int progress;
    /* �Ƿ�ȡ������ */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* ���½����� */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    //���ļ���URL
    private String newFileURL;
    
    //��APK����
    private String newAPKName="";
    
    private String packageName="";
    
    private AsyncTask<Void,Void,Void> noneUpdateAction;
    
//    private Thread downloadThread;
    private DownloadApkTask downloadThread;
    
    private static UpdateManager _default;
    
    public void setNoneUpdateAction(AsyncTask<Void,Void,Void> noneUpdateAction) {
		this.noneUpdateAction = noneUpdateAction;
	}

	private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            // ��������
            case DOWNLOAD:
                // ���ý�����λ��
                mProgress.setProgress(progress);
                break;
            case DOWNLOAD_FINISH:
                // ��װ�ļ�
                installApk();
                break;
            default:
                break;
            }
        };
    };

    public UpdateManager(Context context,String newAPKName)
    {
    	this.mContext=context;
    	this.newAPKName=newAPKName;
    	this.packageName=SuncereApplication.CurrentApplication().getPackageName();
    	_default=this;
    }
    
    public UpdateManager(Context context,String newAPKName,String packageName)
    {
        this.mContext = context;
        this.newAPKName=newAPKName;
        this.packageName=packageName;
    	_default=this;
    }

    /**
     * ����������
     */
    public void checkUpdate(String updateURL)
    {
        if (isUpdate( updateURL))
        {
            // ��ʾ��ʾ�Ի���
            showNoticeDialog();
        } else
        {
        	if(this.noneUpdateAction!=null)
        		this.noneUpdateAction.execute();
            //Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * �������Ƿ��и��°汾
     * 
     * @return
     */
    public boolean isUpdate(String updateURL)
    {
    	//newFileURL=new UpdateDataLoader().LoadNewAndroidAPKFile( this.getVersionCode(mContext) );
    	newFileURL=null;
    	try
    	{
//    		String url= URLEncoder.encode(this.getVersionCode(mContext) , "UTF-8");
    		InputStream is=NetworkUitl.GetStreamByHttpConnection(updateURL,5000);
    		newFileURL= InputStreamUtil.InputStream2String(is);
    	}catch(Exception ex)
    	{
    		
    	}
    	return newFileURL!=null&&!newFileURL.equals("")&&!newFileURL.equals("\"\"");
//    	return true;
    }

/**
 * ��ȡ����汾��
 * 
 * @param context
 * @return
 */
public String getVersionCode(Context context)
{
	String versionCode = "";
    try
    {
        // ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
        versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
    } catch (NameNotFoundException e)
    {
        e.printStackTrace();
    }
    return versionCode;
}

public String getVersionCode()
{
	return this.getVersionCode(SuncereApplication.CurrentApplication());
}

    /**
     * ��ʾ������¶Ի���
     */
    public void showNoticeDialog()
    {
        // ����Ի���
        Builder builder = new Builder(mContext);
        builder.setTitle("�������");
        builder.setMessage("��⵽�°汾������������");
        builder.setCancelable(false);
        // ����
        builder.setPositiveButton("����", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // ��ʾ���ضԻ���
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("�Ժ����",new OnClickListener()
		      {
		      @Override
		      public void onClick(DialogInterface dialog, int which)
		      {
		          dialog.dismiss();
		          if(noneUpdateAction!=null)
		        		noneUpdateAction.execute();
		      }
		  });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * ��ʾ������ضԻ���
     */
    private void showDownloadDialog()
    {
        // ����������ضԻ���
        Builder builder = new Builder(mContext);
        builder.setTitle("��������");
        // �����ضԻ������ӽ�����
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setCancelable(false);
        // ȡ������
        builder.setNegativeButton("ȡ������", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // ����ȡ��״̬
                cancelUpdate = true;
                if(noneUpdateAction!=null)
                	noneUpdateAction.execute();
                if(_default!=null&&_default.downloadThread!=null)
                _default.downloadThread.cancel(true);
                
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // �����ļ�
        downloadApk();
    }

    /**
     * ����apk�ļ�
     */
    private void downloadApk()
    {

    	this.mContext.startService(new Intent(mContext,DownloadApkService.class));
    }
    
    public class DownloadApkTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			cancelUpdate=false;
			new downloadApkThread().run();
			return null;
		}};
    
		public static class DownloadApkService extends Service
		{

			@Override
			public IBinder onBind(Intent arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int onStartCommand(Intent intent,int flags,int startId)
			{
		        _default.downloadThread=_default. new DownloadApkTask(); 
		        _default.downloadThread.execute();
				return START_STICKY;
			}
			
		}
		
    /**
     * �����ļ��߳�
     * 
     * @author coolszy
     *@date 2012-4-26
     *@blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
        	boolean hasException=false;
            try
            {
                // �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // ��ô洢����·��
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(newFileURL.replace('"', ' '));//URL(mHashMap.get("url"));
                    // ��������
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // ��ȡ�ļ���С
                    int length = conn.getContentLength();
                    // ����������
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // �ж��ļ�Ŀ¼�Ƿ����
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, newAPKName);//mHashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // ����
                    byte buf[] = new byte[1024];
                    // д�뵽�ļ���
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // ���������λ��
                        progress = (int) (((float) count / length) * 100);
                        // ���½���
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // �������
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // д���ļ�
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// ���ȡ����ֹͣ����.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
                hasException=true;
            } catch (IOException e)
            {
                e.printStackTrace();
                hasException=true;
            }
            if(hasException&&noneUpdateAction!=null){
                	noneUpdateAction.execute();
//            	Toast.makeText(mContext,"����ʧ��",Toast.LENGTH_LONG).show();
            }
            // ȡ�����ضԻ�����ʾ
            mDownloadDialog.dismiss();
        }
    };

    /**
     * ��װAPK�ļ�
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath, newAPKName);//mHashMap.get("name"));
        if (!apkfile.exists())
        {
            return;
        }
        // ͨ��Intent��װAPK�ļ�
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
    
    public interface NoneUpdateAction
    {
    	public void Action();
    }
    
    
}
