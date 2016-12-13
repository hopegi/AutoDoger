package suncere.androidapp.autobasemodule;

import java.util.HashMap;

import cn.jpush.android.util.ab;

public abstract class DataLoaderConfigBase {

	///GET请求  返回集合
	public static final int GET_ARRAY=1;
	///GET请求 返回单个对象
	public static final int GET_SINGLE=2;
	///POST请求 返回集合
	public static final int POST_ARRAY=3;
//	///POST请求 返回单个对象
//	public static final int POST_SINGLE=4;
	

	public final static String CONFIG_NAME="&ConfigName";
	
	private HashMap<String, Object> para;
	
	public HashMap<String, Object> getPara() {
		return para;
	}

	public void setPara(HashMap<String, Object> para) {
		this.para = para;
	}

	///配置的标识名
	public String ConfigName()
	{
		String className= this.getClass().getSimpleName();
		return className.replace("DataLoaderConfig", "");
//		return "default";
	}
	
	public boolean IsDefault()
	{
		return false;
	}
	
	///请求的URL  仅包含  {controller}/{action} 部分
	public abstract String APIURL() ;
	
	///参数  如果存在多个重名的参数，Object可为 List或者 Array
	public HashMap<String, Object> Parameters(AutoBaseModel model)
	{
//		return null;
		return para;
	}
	
	///请求类型
	public abstract int LoadDataType();
}
