package suncere.androidappcf.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class ShareTool 
{
	static String savePath=null;
	
	 public static void saveImage(final Activity activity,final String parenthesisWords) {
		 
		 Toast.makeText(activity, "������������...", Toast.LENGTH_SHORT).show();
		 
		 
		 new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... arg0) {
				
			
		 	if(savePath==null||savePath.length()==0)
		 		savePath= "/data/data/"+ activity. getPackageName()+"/images";
	    	saveMyBitmap(getBitmapFromRootView(activity.getWindow().getDecorView()), savePath);
	    	
	    	Intent intent=new Intent(Intent.ACTION_SEND);   
			intent.setType("image/*");
	        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
	         
	        File file=new File(copyFile(savePath+"/temp.png", ""));
	        Uri u = Uri.fromFile(file);    
	        intent.putExtra(Intent.EXTRA_STREAM, u);    
	        
//	        intent.putExtra(Intent.EXTRA_TEXT, "�յ���ϢĪ���������ڲ���App�ķ����� (����ͨ)");   
	        intent.putExtra(Intent.EXTRA_TEXT,parenthesisWords);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	        activity.startActivity(Intent.createChooser(intent,activity.getTitle()));   
	        

	    	return null;
			}}.execute();
	    }

	 ///������Ļ��ͼ
	 public static void ShareScreen(final Activity activity)
	 {
		 Toast.makeText(activity, "������������...", Toast.LENGTH_SHORT).show();
		 
		 
		 new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... arg0) {
				
			 	if(savePath==null||savePath.length()==0)
			 		savePath= "/data/data/"+ activity. getPackageName()+"/images";
		    	saveMyBitmap(getBitmapFromRootView(activity.getWindow().getDecorView()), savePath);//������Ļ��ͼ
		    	
		    	Intent intent=new Intent(Intent.ACTION_SEND);   
				intent.setType("image/*");
		        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
		         
		        File file=new File(copyFile(savePath+"/temp.png", ""));
		        Uri u = Uri.fromFile(file);    
		        intent.putExtra(Intent.EXTRA_STREAM, u);    
		        
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		        activity.startActivity(Intent.createChooser(intent,activity.getTitle()));   
		        
	
		    	return null;
			}}.execute();
	 }
	 
	 ///����������Ϣ
	 public static void ShareMessage(final Context context,final String words)
	 {
		 Toast.makeText(context, "������������...", Toast.LENGTH_SHORT).show();
		 
		 
		 new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... arg0) {
				
		    	Intent intent=new Intent(Intent.ACTION_SEND);   
				intent.setType("text/plain");
				
	//	        intent.putExtra(Intent.EXTRA_TEXT, "�յ���ϢĪ���������ڲ���App�ķ����� (����ͨ)");   
		        intent.putExtra(Intent.EXTRA_TEXT,words);
		        intent.putExtra(Intent.EXTRA_SUBJECT, words);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		        context.startActivity(Intent.createChooser(intent,""));   
	
		    	return null;
			}}.execute();
	 }
	 
	 ///������ԴͼƬ
	 public static void ShareResourceImage(final Context context,final int resId)
	 {
		 	Toast.makeText(context, "������������...", Toast.LENGTH_SHORT).show();
		 	
	        new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... arg0) {

			        OutputStream bos=null;
			        String fileName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/share.jpg";
					try {
						///����ԴͼƬת����Bitmap ���洢ͼƬ
						Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), resId);
						
						bos = new FileOutputStream(fileName);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
						bos.flush();
						bos.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent intent=new Intent(Intent.ACTION_SEND);   
					intent.setType("image/*");
			        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
			         
			        File file=new File(fileName);
			        Uri u = Uri.fromFile(file);    
			        intent.putExtra(Intent.EXTRA_STREAM, u);    
			        
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
			        context.startActivity(Intent.createChooser(intent,""));   
					
					return null;
			}}.execute();
	 }
	 
	 ///����Asset��ͼƬ
	 public static void ShareAssetsImage(final Context context,final String resName)
	 {
		 	Toast.makeText(context, "������������...", Toast.LENGTH_SHORT).show();
		 	
	        new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... arg0) {
				 	if(savePath==null||savePath.length()==0)
				 		savePath= "/data/data/"+ context. getPackageName()+"/images";
			    	
			    	Intent intent=new Intent(Intent.ACTION_SEND);   
					intent.setType("image/jpg");

			        InputStream is;
					try {
						is = context.getResources().getAssets().open("erweima.png");
				        Uri u=Uri.fromFile(new File( copyFile(is,"")));
				        intent.putExtra(Intent.EXTRA_STREAM, u);    
				        
				        
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//			        intent.putExtra(Intent.EXTRA_TEXT,parenthesisWords);
//			        intent.putExtra(Intent.EXTRA_SUBJECT, parenthesisWords);
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
			        context.startActivity(Intent.createChooser(intent,""));   
					return null;
				}}.execute();
	 }
	 
	// ��ȡview��ת����bitmapͼƬ
   	private static Bitmap getBitmapFromRootView(View view) {

   		Bitmap bmp = null;
	    	view.setDrawingCacheEnabled(true);
	
	    	bmp= Bitmap.createBitmap(view.getDrawingCache());
	
	    	view.setDrawingCacheEnabled(false);
	    	return bmp;
   	}


	// ��bitmaoͼƬ���浽��Ӧ��SD��·����
   	private static void saveMyBitmap(Bitmap mBitmap, String path) {

	    	File f = new File(path);
	
	    	try {
	    		if(!f.exists())
	    			f.mkdir();
	    	f.createNewFile();
	
	    	}
	    	catch (IOException e) {
	    	e.printStackTrace();
	    	}

	    	FileOutputStream fOut = null;

	    	try {
	    	fOut =new FileOutputStream(f+"/temp.png");
	    	} catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    	}

	    	if (mBitmap != null) {
	    		//�����ʽΪPNG ����Ϊ100
	    		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
	    	}
	    	try {
	    		fOut.flush();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    	try {
	    		fOut.close();
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
	    }
	    	
	public static String copyFile(String oldPath, String newPath) {   
	    	       
		String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
		try {
			OutputStream os=new FileOutputStream(path+"Download/temp.png");
			InputStream is=new FileInputStream(oldPath);
				int count;
				byte[] buffer=new byte[1024];
				while((count=is.read(buffer))>0)
					os.write(buffer,0,count);
			os.flush();
			os.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path+"Download/temp.png";
	   }   

	public static String copyFile(InputStream inputStream,String newPath)
	{
		String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
		try {
			OutputStream os=new FileOutputStream(path+"Download/yunzhanfang.png");
			
				int count;
				byte[] buffer=new byte[1024];
				while((count=inputStream.read(buffer))>0)
					os.write(buffer,0,count);
			os.flush();
			os.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path+"Download/yunzhanfang.png";
	}
}
