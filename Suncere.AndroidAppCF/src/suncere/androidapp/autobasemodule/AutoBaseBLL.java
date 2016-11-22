package suncere.androidapp.autobasemodule;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cn.jpush.android.data.u;
import suncere.androidapp.attributes.AttributeUsage;
import suncere.androidapp.attributes.IClassAttribute;
import suncere.androidapp.attributes.IModelFieldAttribute;
import suncere.androidapp.attributes.INeedQueryParams;
import suncere.androidapp.attributes.ListCountAttribute;
import suncere.androidapp.basemodule.BaseLoader;
import suncere.androidappcf.controls.SuncereApplication;
import suncere.androidappcf.tools.TypeHelper;
import android.content.Loader;
import android.util.Log;

public class AutoBaseBLL {

	private PluginContext context;
	
	
	public AutoBaseBLL(PluginContext context)
	{
		this.context=context;
	}

	public List<HashMap<String, Object>> GetCacheData( HashMap<String, Object> parameters)
	{
		 List<HashMap<String, Object>> result=null;//new ArrayList<HashMap<String,Object>>();
		try {
			List<Object> dataList=context. getMainDal().QueryData(context.getModel(), parameters);
			if(dataList!=null)
			{
				result=new ArrayList<HashMap<String,Object>>();
				AutoBaseModel model;
				for(Object dataItem : dataList)
				{
//					result.add(TypeHelper.ConvertToHashMap(dataItem));///原本代码
					model=(AutoBaseModel) dataItem;
					HashMap<String,Object> hashItem=TypeHelper.ConvertToHashMap(model);

					///通过Attribute转换值
					this.UseFieldAttributeConvertValue(model, hashItem,parameters,AttributeUsage.ResultDataHandle);
					//添加到集合中
					result.add(hashItem);
				}
			}	
		} catch (Exception e) {
			Log.d(SuncereApplication.CurrentApplication().getPackageName(), " " +e.getMessage());
		}
		
		//过滤集合
		this.UserClassAttributeFiltList(result);
		
		this.ResultDataHandlerHandleResult(result, parameters);
		return result;
	}
	
	public List<HashMap<String, Object>> GetNewData( HashMap<String, Object> parameters)
	{
		List<HashMap<String, Object>> result=null;//new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> localParameters=new HashMap<String,Object>();
		if(parameters!=null)
			localParameters.putAll(parameters);
		try {
			context.getChecker().SetContext(context.BuildCheckerContext());
			if(!context.getChecker().ExistData(localParameters))
			{
				AutoBaseDataLoader loader=context.getLoader();
				String packageName=context.getModel().getClass().getPackage().getName();
				
				if(!localParameters.containsKey(LoadingConfigProvider.PACKAGE_NAME))
				{
					localParameters.put(LoadingConfigProvider.PACKAGE_NAME,packageName);
				}
				else 
				{
					localParameters.remove(LoadingConfigProvider.PACKAGE_NAME);
					localParameters.put(LoadingConfigProvider.PACKAGE_NAME, packageName);
				}
				
				Object netDatas= loader.LoadDataByConfig(LoadingConfigProvider.Current().GetDataLoaderConfig(localParameters), context.getModel());
				if(netDatas==null)
					return result;
				if(netDatas.getClass().isArray())
				{
					int length=Array.getLength(netDatas);
					AutoBaseModel model;
					for(int i=0;i<length;i++)
					{
						model= (AutoBaseModel) Array.get(netDatas,i);
						HashMap<String,Object> hashItem=TypeHelper.ConvertToHashMap(model);
						this.UseFieldAttributeConvertValue(model, null,localParameters, AttributeUsage.NetDataHandle);
						if(context.getNetDataHandler()!=null)
						{
							context.getNetDataHandler().SetContext(  context.BuildNetDataHandlerContext(localParameters)  );
							context.getNetDataHandler().HandleNetData(model);
						}
						SaveDataToDb(model);
						if(result==null)
							result=new ArrayList<HashMap<String,Object>>();
						hashItem=TypeHelper.ConvertToHashMap(model);

						///通过Attribute转换值
						this.UseFieldAttributeConvertValue(model, hashItem,localParameters,AttributeUsage.ResultDataHandle);
						result.add(hashItem);
					}
				}
				else 
				{
					AutoBaseModel model=(AutoBaseModel) netDatas;
					this.UseFieldAttributeConvertValue(model, null,localParameters, AttributeUsage.NetDataHandle);
					if(context.getNetDataHandler()!=null)
						context.getNetDataHandler().HandleNetData(model);
					SaveDataToDb(model);

					if(result==null)
						result=new ArrayList<HashMap<String,Object>>();
					HashMap<String,Object> hashItem=TypeHelper.ConvertToHashMap(model);
					///通过Attribute转换值
					this.UseFieldAttributeConvertValue(model, hashItem,localParameters,AttributeUsage.ResultDataHandle);
					//添加到集合中
					result.add(hashItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(SuncereApplication.CurrentApplication().getPackageName(), " " +e.getMessage());
		}
		
		//过滤集合
		this.UserClassAttributeFiltList(result);
		
		this.ResultDataHandlerHandleResult(result, localParameters);
		return result;
	}
	
	protected void SaveDataToDb(AutoBaseModel model)
	{
		if(context.getMainDal().ExistData(model))
			context.getMainDal().UpdateData(model);
		else 
			context.getMainDal().InsertData(model);
	}
	
	private void UseFieldAttributeConvertValue(AutoBaseModel model,HashMap<String,Object> hashItem,HashMap<String, Object> otherPara,AttributeUsage usage)
	{
		HashMap<String, List<Object>>attrs= model.FieldAtrributes();
		for(Entry<String,List<Object>> fieldAttrs : attrs.entrySet())
		{
			for(Object attr: fieldAttrs.getValue())
			{
				if(TypeHelper.IsSubClassOf(attr, IModelFieldAttribute.class))
				{
					IModelFieldAttribute fieldAttr=(IModelFieldAttribute) attr;
					if((fieldAttr.AttributeUsage().Value()&usage.Value())==0)continue;
					fieldAttr.FieldName(fieldAttrs.getKey());
					if(TypeHelper.IsSubClassOf(attr, INeedQueryParams.class)  )
					  ((INeedQueryParams)fieldAttr).QueryParam(context.getModel(), otherPara);
					fieldAttr.HandleData(model, hashItem);
				}
			}
		}
	}
	
	
	private void UserClassAttributeFiltList(List<? extends Object> list)
	{
		if(context.getModel().ClassAttributes().size()>0)
		{
			for(IClassAttribute attr :  context.getModel().ClassAttributes())
			{
				if(TypeHelper.IsSubClassOf(attr, ListCountAttribute.class))
				{
					((ListCountAttribute)attr).FiltList(list);
				}
			}
		}
	}

	private void ResultDataHandlerHandleResult(List<HashMap<String,Object>> result,HashMap<String, Object> parameters)
	{
		if(context.getResultDataHandler()!=null)
		{
			ResultDataHandlerContext handleContext=new ResultDataHandlerContext();
			handleContext.setNetDataHandlerContext( context.BuildNetDataHandlerContext(parameters)  );
			handleContext.setPluginContext(context);
			handleContext.setCache(true);
			
			context.getResultDataHandler().SetContext(handleContext);
			if(result==null)
				result=new ArrayList<HashMap<String,Object>>();
			context.getResultDataHandler().HandleResultData(result);
		}
	}
}
