package suncere.androidappcf.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public abstract class TypeHelper {

	private static HashMap<Class, HashMap<String, Field>> fieldCache=new HashMap<Class,HashMap<String, Field>>();
	private static HashMap<Class,HashMap<String, Method>> methodCache=new HashMap<Class,HashMap<String, Method>>();
	private static HashMap<Class,HashMap<Class,Boolean>> childCache=new HashMap<Class,HashMap<Class,Boolean>> ();
	
	private static Object fieldFlag =new Object(),methodFlag=new Object(),childFlag=new Object();
	
	
	public static Method Getter(Class<?> classType, Field field)
	{
		Method result=Getter(classType, field.getName());
		
		return result;
	}
	
	public static Method Getter(Class<?> classType,String fieldName)
	{
		Method result=null;
		
		try {
			String methodName=GetMethodNamePropertyName(fieldName, "get");
			synchronized (methodFlag) {
				if(!methodCache.containsKey(classType))
				{
					methodCache.put(classType, new HashMap<String,Method>());
				}
				if(methodCache.get(classType).containsKey(methodName))
					return methodCache.get(classType).get(methodName);
			}
			result=classType.getDeclaredMethod(  methodName  , null);
			synchronized (methodFlag) {
				methodCache.get(classType).put(methodName, result);
			}
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
		}
		
		return result;
	}
	
	private static String GetMethodNamePropertyName(String fieldName ,String type)
	{
		String methodName=type+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
		return methodName;
	}
	
	public static Method Setter(Class<?> classType,String fieldName)
	{
		try {
			Field field=null;
			synchronized (fieldFlag) {
				if(!fieldCache.containsKey(classType))
				{
					fieldCache.put(classType, new HashMap<String,Field>());
				}
				if(fieldCache.get(classType).containsKey(fieldName))
					field =fieldCache.get(classType).get(fieldName);
			}
			if(field==null)
			{
				field=classType.getDeclaredField(fieldName);
				synchronized (fieldCache) {
					fieldCache.get(classType).put(fieldName, field);
				}
			}
			return Setter(classType, field);
		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	public static Method Setter(Class<?> classType,Field field)
	{
		Method result=null;
		
		String methodName=GetMethodNamePropertyName(field.getName(), "set");
		try {
			synchronized (methodFlag) {
				if(!methodCache.containsKey(classType))
				{
					methodCache.put(classType, new HashMap<String,Method>());
				}
				if(methodCache.get(classType).containsKey(methodName))
					return methodCache.get(classType).get(methodName);
			}
			result=classType.getDeclaredMethod(methodName, field.getType());
			synchronized (methodFlag) {
				methodCache.get(classType).put(methodName, result);
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Object NewIntance(Class<?> type,Class[] paramTypes, Object[] params)
	{
		Object reuslt=null;
		 try {
			Constructor con = type.getConstructor(paramTypes);
			reuslt=con.newInstance(params);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		 return reuslt;
	}
	
	public static Object NewIntance(Class<?> type)
	{
		Object result=null;
		
		try {
			result= type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static Boolean IsValueType(Class<?> classType)
	{
		if(classType.equals(Integer.class)||
				classType.equals(Short.class)||
				classType.equals(Long.class)||
				classType.equals(Float.class)||
				classType.equals(Double.class)||
				classType.equals(Character.class))
			return true;
		return false;
	}
	
	public static Boolean IsValueType(Object object)
	{
		if(object instanceof Integer ||
				object instanceof Short||
				object instanceof Long||
				object instanceof Float||
				object instanceof Double||
				object instanceof Character)
			return true;
		return false;
	}
	
	public static Boolean IsNumberType(Class<?> classType)
	{
		if(classType.equals(Integer.class)||
				classType.equals(Short.class)||
				classType.equals(Long.class)||
				classType.equals(Float.class)||
				classType.equals(Double.class))
			return true;
		if(classType.getName().equals("int")||
				classType.getName().equals("short")||
				classType.getName().equals("long")||
				classType.getName().equals("float")||
				classType.getName().equals("double"))
			return true;
		return false;
	}
	
	public static Boolean IsNumberType(Object object)
	{
		if(object instanceof Integer ||
				object instanceof Short||
				object instanceof Long||
				object instanceof Float||
				object instanceof Double)
			return true;
		return false;
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
				result.put(f.getName().replace("get", ""), f.invoke(obj, null));
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

	public static boolean IsSubClassOf(Object instance,Class<?> baseType)
	{
		return IsSubClassOf(instance.getClass(), baseType);
	}
	
	public static boolean IsSubClassOf(Class<?>instanceClass,Class<?> baseType)
	{
		synchronized (childFlag) {
			if(!childCache.containsKey(baseType))
			{
				childCache.put(baseType, new HashMap<Class,Boolean>());
			}
			if(childCache.get(baseType).containsKey(instanceClass))
				return childCache.get(baseType).get(instanceClass);
		}
		Class<?> currentType=instanceClass;
//		List<Class<?>>interfaceArr;
		
		boolean result=false;
		while(!currentType.equals(Object.class))
		{
			if(currentType.equals(baseType))
			{
				result=true;
				break;
			}
			if( baseType.isAssignableFrom(instanceClass) )
			{
				result=true;
				break;
			}
//			interfaceArr=Arrays.asList( instanceClass.getInterfaces());
//			if(interfaceArr.indexOf(baseType)>-1)
//				return true;
			
			currentType=currentType.getSuperclass();
		}
		
		synchronized (childFlag) {
			childCache.get(baseType).put(instanceClass, result);
		}
		
		return result;
	}
}
