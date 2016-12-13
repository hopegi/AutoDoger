package suncere.androidapp.basemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import suncere.androidappcf.tools.DateTimeTool;

public abstract class BaseBLL 
{
	///时间点分钟值
	protected abstract int getMinuteOffset();
	
	///时间点小时值
	protected abstract int getHourOffset();
	
	///数据有效值
	protected abstract int getValiableMinutes();
	
	///数据过期
	protected boolean CheckOverTime(String oldDateStr)
	{
		boolean result;
		
		Date oldDate=DateTimeTool.Prase(oldDateStr);
		Date newDate=new Date();

		if(newDate.getMinutes()<this.getMinuteOffset())
			newDate.setHours(newDate.getHours()-1);
		if(this.getHourOffset()>-1)
			newDate.setHours(this.getHourOffset());
		newDate.setMinutes(0);
		float totalValue=DateTimeTool.TotalMinutes(oldDate, newDate);
		result= totalValue<this.getValiableMinutes()&&totalValue>0  ;
		
		return result;
	}

	public static HashMap<String,Object> ConvertToHashMap(Object obj)
	{
		HashMap<String,Object> result=new HashMap<String,Object>();
		if(obj==null)return result;
		Class<?> entityClass= obj.getClass();
		Method[] methods= entityClass.getMethods();//.getFields();
		for(int i=0;i<methods.length;i++)
		{
			Method f=methods[i];
			f.setAccessible(true);
			try {
				if(!f.getName().startsWith("get"))continue;
				result.put(f.getName().replace("get", ""), f.invoke(obj, (Object[]) null));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public  static <T>  List<HashMap<String,Object>> ConvertToListHashMap(List<T> lst)
	{
		List<HashMap<String,Object>> result=new ArrayList<HashMap<String,Object>>();
		
		for(T t :lst)
			result.add(ConvertToHashMap(t));
		
		return result;
	}
	
}
