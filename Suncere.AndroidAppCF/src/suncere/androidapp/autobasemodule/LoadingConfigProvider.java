package suncere.androidapp.autobasemodule;

import java.util.HashMap;

import cn.jpush.android.api.m;
import cn.jpush.android.data.p;

public class LoadingConfigProvider {
	
	public final static String PACKAGE_NAME="PackageName";
	
	private HashMap<String, HashMap<String, DataLoaderConfigBase>> loaderCollection;
	
	private static LoadingConfigProvider _default=new LoadingConfigProvider();
	
	private Object flag;
	
	public static LoadingConfigProvider Current()
	{
		return _default;
	}
	
	public LoadingConfigProvider()
	{
		flag=new Object();
		loaderCollection=new HashMap<String, HashMap<String,DataLoaderConfigBase>>();
	}
	
	public void RegistConfig(DataLoaderConfigBase config)
	{
		if(config==null)return;
		String packageName=config.getClass().getPackage().getName();
		synchronized (flag) {
			if(!loaderCollection.containsKey(packageName))
			{
				loaderCollection.put(packageName, new HashMap<String, DataLoaderConfigBase>());
			}
			HashMap<String, DataLoaderConfigBase> map= loaderCollection.get(packageName);

//			if(!map.containsKey(config.ConfigName()))
//				map.put(config.ConfigName(), config);
			
			///新规则 如果未有设置IsDefault者 按第一个设置为default，否则按最后一个IsDefualt设置为default;
			if(map.size()==0&&!config.IsDefault())
				map.put("default", config);
			if(!map.containsKey(config.ConfigName()))
				map.put(config.ConfigName(), config);
			if(config.IsDefault())
			{
				if(map.containsKey("default"))
					map.remove("defualt");
				map.put("default", config);
			}
		}
	}
	
	public DataLoaderConfigBase GetDataLoaderConfig(HashMap<String, Object> parameter) {
		String packageName=parameter.get(PACKAGE_NAME).toString();
		String configName="default";
		if(parameter.containsKey( DataLoaderConfigBase.CONFIG_NAME))
			configName=parameter.get(DataLoaderConfigBase.CONFIG_NAME).toString();
		DataLoaderConfigBase result= loaderCollection.get(packageName).get(configName);
		result.setPara(parameter);
		return result;
	}
	
}
