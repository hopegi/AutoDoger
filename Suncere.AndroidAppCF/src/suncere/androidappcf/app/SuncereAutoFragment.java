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
	HashMap<String,Object[]> queryModelCollection;
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
	
	///���ò�ѯ����
	protected abstract void SetQueryParameter(HashMap<String,Object[]> queryModelCollection );
	
	///������
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
		//����
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
	
	//��������Դ��
	public void CombineDatasource(DatasourceTypeEnum dsType,String newDatasourceName,String ds1,String ds2,String... keys)
	{
		this.datasourceCombiner.AddCombineDatasource(newDatasourceName, ds1, ds2, keys);
		List<HashMap<String,Object>> newDatasource=new ArrayList<HashMap<String,Object>>();
		
		if(this.datasourceCollection.containsKey(newDatasourceName))
			this.datasourceCollection.remove(newDatasourceName);
		this.datasourceCollection.put(newDatasourceName, newDatasource);
		this.RegistQueryModel(newDatasourceName, newDatasource, dsType);
	}
	
	//��������Դ��
	public void CombineDatasource(String newDatasourceName,String ds1,String ds2,String... keys)
	{
		this.CombineDatasource(DatasourceTypeEnum.Multi, newDatasourceName, ds1, ds2, keys);
	}
	
	///ע���ѯ����Դ
	protected Object[] RegistQueryModel(String name,Class<?> autoModelBaseType)
	{
		return this.RegistQueryModel(name, autoModelBaseType,DatasourceTypeEnum.Multi);
	}
	
	protected Object[] RegistQueryModel(String name,Class<?> autoModelBaseType,DatasourceTypeEnum dsType)
	{
		if(bllCollection.containsKey(name))
		{
			///�����־ �ظ�
			Log.d("", String.format( "CFLog: AndroidAppCF.SuncereAutoFragment_%s_�ظ������%",this.getClass().getSimpleName(),name));
			return null;
		}
		if(autoModelBaseType==null)
		{
			Log.d("", String.format( "CFLog: AndroidAppCF.SuncereAutoFragment_%s_ȱ��ģ������ autoModelBaseType",this.getClass().getSimpleName()));
			return null;
		}
		AutoBaseModel model=(AutoBaseModel) TypeHelper.NewIntance(autoModelBaseType);
		if(model==null)
		{
			Log.d("", String.format( "CFLog: AndroidAppCF.SuncereAutoFragment_%s_���� %s ʵ����ʧ�ܣ��붨���޲ι��캯��",this.getClass().getSimpleName(),autoModelBaseType.getName()));
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
	
	///���첽��ѯ��ʱ��ִ��
	protected void OnAsyncLoadBackgroundDo()
	{
		
	}
	
	///���첽��ѯ���ʱִ��
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
		protected Void doInBackground(Void... arg0) {
			
			OnAsyncLoadBackgroundDo();
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
