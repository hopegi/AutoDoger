package suncere.androidappcf.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectHelper {
	
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
				result.put(f.getName().replace("get", ""), f.invoke(obj, null));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
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
