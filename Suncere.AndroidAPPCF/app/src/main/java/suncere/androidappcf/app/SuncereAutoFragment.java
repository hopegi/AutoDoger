package suncere.androidappcf.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import suncere.androidapp.autobasemodule.AutoBaseBLL;
import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidapp.autobasemodule.DatasourceCombiner;
import suncere.androidapp.autobasemodule.PluginLoader;
import suncere.androidapp.viewautobinder.DatasourceTypeEnum;
import suncere.androidapp.viewautobinder.IMacroDefine;
import suncere.androidapp.viewautobinder.ViewAutoBinder;
import suncere.androidappcf.R;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.controls.SuncereFragment;
import suncere.androidappcf.tools.NetworkUitl;
import suncere.androidappcf.tools.TypeHelper;

public abstract class SuncereAutoFragment  extends SuncereFragment{

	HashMap<String,AutoBaseBLL> bllCollection;
	HashMap<String,List<HashMap<String,Object>>> datasourceCollection;
	HashMap<String,Object[]> queryModelCollection;//查询的model集合
	boolean hasRegist=false;
	private ViewAutoBinder viewAutoBinder;
 	private DatasourceCombiner datasourceCombiner;
	
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		bllCollection=new HashMap<String,AutoBaseBLL>();
//		datasourceCollection=new HashMap<String,List<HashMap<String,Object>>> ();
//		queryModelCollection=new HashMap<String,Object[]> ();
//		
//		super.onCreate(savedInstanceState);
//		this.setContentView(this.OnGetContentView());
//		this.InitViews();
//	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		bllCollection=new HashMap<String,AutoBaseBLL>();
		datasourceCollection=new HashMap<String,List<HashMap<String,Object>>> ();
		queryModelCollection=new HashMap<String,Object[]> ();
		
		this.setContentView(this.OnGetContentView());
	}
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		View view=super.onCreateView(inflater, container, savedInstanceState);
		viewAutoBinder=new ViewAutoBinder(view);
		viewAutoBinder.AnalyzeViewBindingInfo(this.getContentView());
		datasourceCombiner=new DatasourceCombiner();
		
		return view;
	}
	
	public void onStart()
	{
		super.onStart();
		this.InitViews();
		if(!hasRegist)
		{
			this.RegistQueryModels();
			hasRegist=true;
		}
		RefreshViewData();
	}
	
	protected abstract int OnGetContentView();
	
	protected abstract void InitViews();
	
	protected abstract void RegistQueryModels();
	
	///设置查询参数
	protected abstract void SetQueryParameter(HashMap<String,Object[]> queryModelCollection );
	
	///绑定数据
	protected abstract void BindData(HashMap<String,List<HashMap<String,Object>>> datasourceCollection);
	
	public void RefreshViewData()
	{	
		this.GetData(true);
		BindData();
		Integer threadPoolSize=SuncereApplication.CurrentApplication().ThreadPoolSize();
		if(threadPoolSize==null)
		{
			new AsyncLoad().execute();
		}
		else
		{
			new AsyncLoad().executeOnExecutor(Executors.newFixedThreadPool(threadPoolSize));
		}
	}
	
	public void UnRefreshViews(View...views )
	{
		viewAutoBinder.setUnRefreshViews( Arrays.asList( views ));
	}
	
	protected void GetData(boolean isCache)
	{
		//复制
		HashMap<String,Object[]> tmpQueryModelCollecton=new HashMap<String,Object[]>();
		tmpQueryModelCollecton.putAll(queryModelCollection);
		this.SetQueryParameter( tmpQueryModelCollecton  );
		
		List<HashMap<String,Object>> datas;
		for(Entry<String,AutoBaseBLL> kvp : bllCollection.entrySet() )
		{
			if(isCache)
				datas=kvp.getValue().GetCacheData(  (HashMap<String,Object>) tmpQueryModelCollecton.get(  kvp.getKey())[1] );
			else
				datas=kvp.getValue().GetNewData(  (HashMap<String,Object>) tmpQueryModelCollecton.get(  kvp.getKey())[1] );
			if(datas!=null)
			{
				datasourceCollection.get(  kvp.getKey() ).clear();
				datasourceCollection.get(  kvp.getKey() ).addAll(datas);
			}
		}
		
		this.datasourceCombiner.CombineData(datasourceCollection);
	}
	
	protected void BindData()
	{
		HashMap<String,List<HashMap<String,Object>>> tmpDs=new HashMap<String,List<HashMap<String,Object>>>();
		tmpDs.putAll(datasourceCollection);
		
		if(TypeHelper.IsSubClassOf(this, IMacroDefine.class) )
		{
			HashMap<String,String> macoFieldCollection=new HashMap<String,String>();
			((IMacroDefine)this).DefineMacroField(macoFieldCollection);
			if(macoFieldCollection.size()>0)
				viewAutoBinder.setDefineCollection(macoFieldCollection);
		}
		try
		{
			viewAutoBinder.AutoBindData();
		}
		catch(Exception ex)
		{
			
		}
		this.BindData(tmpDs);
	}
	
	//设置数据源绑定
	public void CombineDatasource(DatasourceTypeEnum dsType,String newDatasourceName,String ds1,String ds2,String... keys)
	{
		this.datasourceCombiner.AddCombineDatasource(newDatasourceName, ds1, ds2, keys);
		List<HashMap<String,Object>> newDatasource=new ArrayList<HashMap<String,Object>>();
		
		if(this.datasourceCollection.containsKey(newDatasourceName))
			this.datasourceCollection.remove(newDatasourceName);
		this.datasourceCollection.put(newDatasourceName, newDatasource);
		this.RegistQueryModel(newDatasourceName, newDatasource, dsType);
	}
	
	//设置数据源绑定
	public void CombineDatasource(String newDatasourceName,String ds1,String ds2,String... keys)
	{
		this.CombineDatasource(DatasourceTypeEnum.Multi, newDatasourceName, ds1, ds2, keys);
	}
	
	///注册查询数据源
	protected Object[] RegistQueryModel(String name,Class<?> autoModelBaseType)
	{
		return this.RegistQueryModel(name, autoModelBaseType,DatasourceTypeEnum.Multi);
	}
	
	protected Object[] RegistQueryModel(String name,Class<?> autoModelBaseType,DatasourceTypeEnum dsType)
	{
		if(bllCollection.containsKey(name))
		{
			///输出日志 重复
			Log.d("", String.format( "CFLog: AndroidAppCF.SuncereAutoFragment_%s_重复插件名%",this.getClass().getSimpleName(),name));
			return null;
		}
		if(autoModelBaseType==null)
		{
			Log.d("", String.format( "CFLog: AndroidAppCF.SuncereAutoFragment_%s_缺少模型类型 autoModelBaseType",this.getClass().getSimpleName()));
			return null;
		}
		AutoBaseModel model=(AutoBaseModel) TypeHelper.NewIntance(autoModelBaseType);//使用反射实例化该model类型
		if(model==null)
		{
			Log.d("", String.format( "CFLog: AndroidAppCF.SuncereAutoFragment_%s_类型 %s 实例化失败，请定义无参构造函数",this.getClass().getSimpleName(),autoModelBaseType.getName()));
			return null;
		}
		AutoBaseBLL bll=PluginLoader.Current().InitPlugin(model);
		HashMap<String,Object> queryParameters=new HashMap<String,Object>();
		List<HashMap<String,Object>> datasource=new ArrayList<HashMap<String,Object>>();
		
		this.bllCollection.put(name, bll);
		this.datasourceCollection.put(name, datasource);
		this.queryModelCollection.put(name, new Object[]{ model, queryParameters});
		
//		viewAutoBinder.RegistDataSource(name, datasource,dsType);
		this.RegistQueryModel(name, datasource, dsType);
		
		return this.queryModelCollection.get(name);
	}
	
	protected void RegistQueryModel(String name,Object datasource)
	{
		this.RegistQueryModel(name, datasource, DatasourceTypeEnum.Multi);
	}
	
	protected void RegistQueryModel(String name,Object datasource, DatasourceTypeEnum dsType)
	{
		viewAutoBinder.RegistDataSource(name, datasource,dsType);
	}
	
	///在异步查询的时候执行
	protected void OnAsyncLoadBackgroundDo()
	{
		
	}
	
	///在异步查询完成时执行
	protected void OnAsyncLoadPostExecute()
	{
		
	}

//	protected void DefineMacoField(HashMap<String,String> fieleCollection)
//	{
//		
//	}

	protected  boolean ShowNetworkState() {
		return false;
	}
	
	private class AsyncLoad extends AsyncTask<Void,Void,Void>
	{
		Long threadId;

		@Override
		protected void onPreExecute() {
			OnAsyncLoadBackgroundDo();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			
			
			GetData(false);
			this.threadId=Thread.currentThread().getId();
			return null;
		}
		
		
		protected void onPostExecute(Void res)
		{
			if(ShowNetworkState()&& NetworkUitl.ErrorConnectionList.contains(threadId))
			{
				NetworkUitl.ErrorConnectionList.remove(threadId);
				Toast.makeText(getActivity(), R.string.uselessnetwork, Toast.LENGTH_SHORT).show();
			}
			BindData();
			OnAsyncLoadPostExecute();
			
		}
	}

}
