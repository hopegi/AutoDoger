package suncere.androidapp.autobasemodule;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.util.Log;
import cn.jpush.android.api.e;
import suncere.androidapp.basemodule.BaseLoader;
import suncere.androidappcf.app.SuncereAppParameters;
import suncere.androidappcf.tools.TypeHelper;

public class AutoBaseDataLoader extends BaseLoader {
	
	private static final String LogTag="AutoBaseDataLoader";

	@Override
	protected String APIURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object LoadDataByConfig( DataLoaderConfigBase config,AutoBaseModel model )
	{
		Object result=null;
		String paraStr=BuildParametersString(config.Parameters(model),config.LoadDataType());
		if(config.LoadDataType()==DataLoaderConfigBase.GET_ARRAY)
		{
			Log.d(LogTag, String.format("AutoBaseDataLoader %s   GET   %s%s%s",model.getClass().getName(),this.ServerIpPort(),config.APIURL(),paraStr));
			return super.GetLoadArrayData( this.ServerIpPort()+ config.APIURL()+paraStr, model.getClass());
		}
		else if(config.LoadDataType()==DataLoaderConfigBase.GET_SINGLE)
		{
			Log.d(LogTag, String.format("AutoBaseDataLoader %s   GET   %s%s%s",model.getClass().getName(),this.ServerIpPort(),config.APIURL(),paraStr));
			return super.GetLoadData( this.ServerIpPort()+ config.APIURL()+paraStr, model.getClass());
		}
		else if(config.LoadDataType()==DataLoaderConfigBase.POST_ARRAY) {
			Log.d(LogTag, String.format("AutoBaseDataLoader %s   POST   %s%s   para=  %s",model.getClass().getName(),this.ServerIpPort(),config.APIURL(),paraStr));
			 return super.PostLoadArrayData(this.ServerIpPort()+config.APIURL(), paraStr, model.getClass());
		}
//		else if (config.LoadDataType()==DataLoaderConfigBase.POST_SINGLE) {
//			
//		}
		
		return result;
	}
	
	private String BuildParametersString(HashMap<String, Object> para,int loadDataType)
	{
		String result="";
		if(para==null)return result;
		String urlKey;
		for(Entry<String, Object> kvp : para.entrySet())
		{
			urlKey=QueryParameterHelper.GetNetParameterName(kvp.getKey());
			if(urlKey.length()==0)continue;
			if(kvp.getValue() instanceof String|| TypeHelper.IsValueType(kvp.getValue()))
				result+=String.format("%s=%s&", urlKey,EncodingString( kvp.getValue().toString()));
			else if(kvp.getValue() instanceof List<?>)
			{
				List<?> list= (List<?>)kvp.getValue();
				for(int i=0;i<list.size();i++)
				{
					result+=urlKey+"="+EncodingString( list.get(i).toString())+"&";
				}
			}
			else if(kvp.getValue().getClass().isArray())
			{
				int length=Array.getLength(kvp.getValue());
				for(int i=0;i<length;i++)
				{
					result+=urlKey+"="+EncodingString( Array.get(kvp.getValue(), i).toString())+"&";
				}
			}
		}
		if(result.endsWith("&"))
			result=result.substring(0,result.length()-1);
		if(loadDataType!=DataLoaderConfigBase.POST_ARRAY)
			result="?"+result;
		return result;
	}
}
