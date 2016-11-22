package suncere.androidapp.attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import suncere.androidapp.autobasemodule.AutoBaseModel;
import suncere.androidappcf.tools.TypeHelper;

public class FromParaAttribute extends ConvertableAttribute implements INeedQueryParams {


	protected AutoBaseModel queryModel;
	protected HashMap<String, Object> otherQueryPara;

	public String paraName;
	
	public FromParaAttribute(String fieldNewName, String paraName)
	{
		this(fieldNewName, paraName,AttributeUsage.ResultDataHandle);
	}
	
	public FromParaAttribute(String fieldNewName,String paraName,AttributeUsage usage)
	{
		this.fieldNewName=fieldNewName;
		this.paraName=paraName;
		this.usage=usage;
	}
	
	public void QueryParam( AutoBaseModel model,HashMap<String, Object> otherPara)
	{
		this.queryModel=model;
		this.otherQueryPara=otherPara;
	}

	@Override
	public Object ConvertData(Object value, AutoBaseModel model) {
		Method getter=TypeHelper.Getter(queryModel.getClass(), paraName);
		Object result=null;
		if(getter!=null)
		{
			try {
				result=getter.invoke(queryModel, null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if(result==null)
		{
			if(otherQueryPara.containsKey(paraName))
				result= otherQueryPara.get(paraName);
		}
		return result;
	}
}
