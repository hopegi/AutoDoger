package suncere.androidappcf.components;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import suncere.androidappcf.R;
import suncere.androidappcf.controls.SuncereApplication;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public abstract class ShareTool_v2 {

	private static HashMap<String,Object> appNameMapping;	
	private static List<HashMap<String,Object>> datasource;
	
	static {
		datasource=new ArrayList<HashMap<String,Object>>();
		LoadAppName();
	}
	
	private static void LoadAppName()
	{
		appNameMapping=new HashMap<String,Object>();
		List<ApplicationInfo> listAppcations = SuncereApplication.CurrentApplication().getPackageManager()
																	.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);  
		for(ApplicationInfo info :listAppcations)
		{
			appNameMapping.put(info.packageName, info.loadLabel(SuncereApplication.CurrentApplication().getPackageManager()));
		}
	}
	
	public static void ShareImage(Context context,String imgPath,String... appNames)
	{
		File file=new File(imgPath);
        Uri uri = Uri.fromFile(file);    
        InnerShare(context,uri,appNames);
	}
	
	public static void ShareText(Context context,String content,String... appNames)
	{
		InnerShare(context,content,appNames);
	}
	
	private static void InnerShare(Context context,Object shareContent,String... appNames)
	{
		Intent intent=new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		if(shareContent instanceof String)
		{
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_STREAM, (String)shareContent);
		}
		else if(shareContent instanceof Uri)
		{
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_STREAM, (Uri)shareContent);
		}
		else
		{
			Log.d("ShareTool_v2", "��δ֧�ֱ����ݵķ���");
		}
		List<ResolveInfo> resolveInfo=context.getPackageManager().queryIntentActivities(intent, 0);
		
		String label;
		Drawable icon;
		ResolveInfo info;
		HashMap<String,Object> item;
		 datasource.clear();
		for(int i=0;i<resolveInfo.size();i++)
		{
			item=new HashMap<String,Object>();
			 info=resolveInfo.get(i);
			 label=info.loadLabel(context.getPackageManager()).toString();
			 icon= info.loadIcon(context.getPackageManager());
			 if(appNames!=null&&appNames.length>0)
			 {
				 if(!ContainAppName( appNames,info.activityInfo.applicationInfo.packageName ))
					 continue;
			 }
			 item.put("label",  label);
			 item.put("icon",  icon  );
			 item.put("resolve", resolveInfo.get(i));
			 item.put("intent", intent);
			 Log.d("", "");
			 datasource.add(item);
		}
		View view=LayoutInflater.from(context).inflate(R.layout.share_list, null);
		ListView shareList= (ListView) view.findViewById(R.id.share_list);
		shareList.setOnItemClickListener(On_Share_Click);
		SimpleAdapter adapter=new MyAdapter(context,datasource,R.layout.share_list_item,new String[]{"label","item"},new int[]{ R.id.share_act_name,R.id.share_act_icon });
		shareList.setAdapter(adapter);
		AlertDialog dialog=new Builder( context ).setTitle("����").setView(view).create();
		shareList.setTag(dialog);
		dialog.show();
		
	}
	
	private static OnItemClickListener On_Share_Click=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			AlertDialog dialog= (AlertDialog) arg0.getTag();
			dialog.dismiss();
			
			ResolveInfo resolve=(ResolveInfo) datasource.get(arg2).get("resolve");
			ActivityInfo ai= resolve.activityInfo;
			Intent intent=new Intent((Intent) datasource.get(arg2).get("intent"));
			intent.setComponent(  new ComponentName(ai.applicationInfo.packageName,ai.name) );
			arg1.getContext().startActivity(intent);
		}};
	
	private static boolean ContainAppName(String[] appNames,String packageName)
	{
		
		if(!appNameMapping.containsKey(packageName))return false;
		String appName= appNameMapping.get(packageName).toString() ;
		for(int i=0;i<appNames.length;i++)
			if(appNames[i].equals(appName))return true;
		return false;
	}
	
	private static  class MyAdapter extends SimpleAdapter{

		public MyAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}
	
		public View getView (int position, View convertView, ViewGroup parent) 
		{
			View view=super.getView(position, convertView, parent);
			
			ImageView img= (ImageView) view.findViewById(R.id.share_act_icon);
			img.setImageDrawable((Drawable) datasource.get(position).get("icon"));
			
			return view;
		}
	}
}
