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
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    //新文件的URL
    private String newFileURL;
    
    //新APK名称
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
            // 正在下载
            case DOWNLOAD:
                // 设置进度条位置
                mProgress.setProgress(progress);
                break;
            case DOWNLOAD_FINISH:
                // 安装文件
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
     * 检测软件更新
     */
    public void checkUpdate(String updateURL)
    {
        if (isUpdate( updateURL))
        {
            // 显示提示对话框
            showNoticeDialog();
        } else
        {
        	if(this.noneUpdateAction!=null)
        		this.noneUpdateAction.execute();
            //Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查软件是否有更新版本
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
 * 获取软件版本号
 * 
 * @param context
 * @return
 */
public String getVersionCode(Context context)
{
	String versionCode = "";
    try
    {
        // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
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
     * 显示软件更新对话框
     */
    public void showNoticeDialog()
    {
        // 构造对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("软件更新");
        builder.setMessage("检测到新版本，立即更新吗");
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton("更新", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("稍后更新",new OnClickListener()
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
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("正在下载");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setCancelable(false);
        // 取消更新
        builder.setNegativeButton("取消更新", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
                if(noneUpdateAction!=null)
                	noneUpdateAction.execute();
                if(_default!=null&&_default.downloadThread!=null)
                _default.downloadThread.cancel(true);
                
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
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
     * 下载文件线程
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
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(newFileURL.replace('"', ' '));//URL(mHashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, newAPKName);//mHashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
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
//            	Toast.makeText(mContext,"下载失败",Toast.LENGTH_LONG).show();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    };

    /**
     * 安装APK文件
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath, newAPKName);//mHashMap.get("name"));
        if (!apkfile.exists())
        {
            return;
        }
        // 通过Intent安装APK文件
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
