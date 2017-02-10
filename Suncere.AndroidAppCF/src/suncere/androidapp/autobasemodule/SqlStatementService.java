package suncere.androidapp.autobasemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import suncere.androidappcf.tools.TypeHelper;
import cn.jpush.android.data.f;
import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class SqlStatementService {

	protected AutoBaseModel model;
	
	public SqlStatementService(AutoBaseModel model)
	{
		this.model=model;
	}
	
	public String[] GetSelectStatement()
	{
		String[] result=null;
		Field[] fieldArr=this.GetSelectFields();
		result= new String[fieldArr.length];
		String fieldName;
		for (int i = 0; i < fieldArr.length; i++) {
			 fieldName=  fieldArr[i].getName();
			 
			 if(model.FieldColumnMapping().containsKey(fieldName))
				 fieldName=model.FieldColumnMapping().get(fieldName);
			 result[i]=fieldName;
		}
		return result;
	}
	
	public Field[] GetSelectFields()
	{
		List<Field> queryFieldNameLst=new ArrayList<Field>();
		Field[] fieldArr=model.getClass().getDeclaredFields();
		
		String fieldName;
		HashMap<String, List<Object>> attributes=model.FieldAtrributes();
		for (int i = 0; i < fieldArr.length; i++) {
			 fieldName=  fieldArr[i].getName();

			 if(fieldName.equals("_default"))continue;
			 
			 if(attributes.containsKey(fieldName))
			 {
				if( attributes.get(fieldName).contains(model.NOT_USE_IN_SELECT))continue;
			 }
			 queryFieldNameLst.add(fieldArr[i]);
		}
		return (Field[])queryFieldNameLst.toArray(new Field[queryFieldNameLst.size()]);
	}
	
	protected String GetWhereStr(HashMap<String, Object> otherParams ,int extendsEnum) {

		HashMap<String, List<Object>> attributes=model.FieldAtrributes();
		String whereStr="";
		Object paraObjValue;
		String fieldName;
		String operatString;
		HashMap<String, Object> localParams=new HashMap<String, Object>();
//		if(otherParams!=null)
//			localParams.putAll(otherParams);
		this.CopyOtherParams(otherParams, localParams);
		
		Field[] fieldArr=model.getClass().getDeclaredFields();
		for(int i=0;i<fieldArr.length;i++)
		{
			fieldName=fieldArr[i].getName();
			if(extendsEnum!=-1&& (!attributes.containsKey(fieldName)||
					!attributes.get(fieldName).contains(extendsEnum)))continue;
			if(fieldName.equals("_default"))continue;
			
			try {
				Method getMethod=TypeHelper.Getter(model.getClass(), fieldName);
				paraObjValue=getMethod.invoke(model, null);
//				if( paraObjValue==null ||  TypeHelper.IsNumberType(paraObjValue))continue;
				if(paraObjValue==null)continue;
				localParams.put(this.GetMappingColumnName(fieldName), paraObjValue);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		String whereStrModel;
		boolean inject;
		for(Entry<String, Object> kvp : localParams.entrySet())
		{
//			fieldName=this.GetMappingColumnName(kvp.getKey());
//			paraObjValue=localParams.get(fieldName);
			paraObjValue=localParams.get(kvp.getKey());
			fieldName=this.GetMappingColumnName( QueryParameterHelper.GetDbParemeterName( kvp.getKey()) );
			operatString=QueryParameterHelper.GetDbOperator(kvp.getKey());
			inject=false;
			if(operatString.equals(QueryParameterHelper.IN))
			{
				whereStrModel="%s IN ('%s') AND ";
			}
			else if(operatString.equals(QueryParameterHelper.LIKE))
			{
				whereStrModel="%s LIKE '%%%s%' AND ";
				inject=true;
			}
			else
			{
				whereStrModel="%s  "+operatString+" %s AND ";
			}
			if(inject || TypeHelper.IsNumberType(paraObjValue))
			{
				whereStr+=String.format(whereStrModel, fieldName,paraObjValue.toString());//  fieldName+" = "+paraObjValue+" AND ";
			}
			else {
				whereStr+=String.format(whereStrModel, fieldName,"?"); //fieldName+" =? AND ";
			}
		}
		if(whereStr.endsWith("AND "))
			whereStr=whereStr.substring(0, whereStr.length()-4);
		
		return whereStr;
	}
	
	protected String[] GetWhereParameter(HashMap<String, Object> otherParams,int extendsEnum  )
	{

		HashMap<String, List<Object>> attributes=model.FieldAtrributes();
		List<String> result=new ArrayList<String>();
		
		Field[] fieldArr=model.getClass().getDeclaredFields();
		String fieldName;
		HashMap<String, Object> localParameter=new HashMap<String, Object>();
		Object value=null;
		
//		if(otherParams!=null)
//		{
//			localParameter.putAll(otherParams);
//		}

		this.CopyOtherParams(otherParams, localParameter);
 
		Object paraObjValue;
		for(int i=0;i<fieldArr.length;i++)
		{
			fieldName=fieldArr[i].getName();
			if(extendsEnum!=-1&& (!attributes.containsKey(fieldName)||
					!attributes.get(fieldName).contains(extendsEnum)))continue;
			if(fieldName.equals("_default"))continue;
			
			try {
				Method getMethod=TypeHelper.Getter(model.getClass(), fieldName);
				paraObjValue=getMethod.invoke(model, null);
//				if( paraObjValue==null|| TypeHelper.IsNumberType(paraObjValue))continue;
				if(paraObjValue==null)continue;
				localParameter.put(this.GetMappingColumnName(fieldName), paraObjValue);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		for(Entry<String, Object> kvp : localParameter.entrySet())
		{
			fieldName=kvp.getKey();
//			if(model.FieldColumnMapping().containsKey(fieldName))
//				fieldName=model.FieldColumnMapping().get(fieldName);
			value=localParameter.get(fieldName);
			
			if(TypeHelper.IsNumberType(value))
			{
				continue;
			}
			else {
				result.add(value.toString());
			}
		}
		
		return (String[])result.toArray(new String[ result.size() ]);
	}
	
	public String GetWhereStr(HashMap<String, Object> otherParams)
	{
		return null;
	}
	
	public String[] GetWhereParameter(HashMap<String, Object> otherParams) 
	{
		return null;
	}
	
	public String GetOrderByStr()
	{
		String result="";
		Field[] fieldArr=model.getClass().getDeclaredFields();
		String fieldName;
		HashMap<String, List<Object>> attributes=model.FieldAtrributes();
		for (int i = 0; i < fieldArr.length; i++) {
			 fieldName=  fieldArr[i].getName();
			 
			 if(!attributes.containsKey(fieldName))continue;
			 
			 if(attributes.get(fieldName).contains(model.ORDER_BY_ASC))
				 result+=  GetMappingColumnName(fieldName)+" ASC,";
			 else if(attributes.get(fieldName).contains(model.ORDER_BY_DESC))
				 result+=GetMappingColumnName(fieldName)+" DESC,";
		}
		if(result.length()>0)
			result=result.substring(0, result.length()-1);
		
		return result;
	}
	
	public ContentValues GetContentValues()
	{
		return null;
	}
	
	public String GetMappingColumnName(String fieldName)
	{
		if(model.FieldColumnMapping().containsKey(fieldName))
			return model.FieldColumnMapping().get(fieldName);
		return fieldName;
	}
	
	protected void CopyOtherParams(HashMap<String,Object> otherParams,HashMap<String,Object> localParams)
	{
		if(otherParams!=null&&localParams!=null)
		{
			for(Entry<String,Object> item : otherParams.entrySet())
			{
				if(item.getKey().startsWith("&"))continue;
				if(item.getKey().startsWith("@"))
					localParams.put(item.getKey().substring(1), item.getValue());
				else
					localParams.put(item.getKey(), item.getValue());
			}
		}
	}
	
	public Object GetCursorValue(Cursor c,int index,Class<?>type)
	{
		if(type.equals(Integer.class)||type.getName().equals("int"))
		{
			return c.getInt(index);
		}
		if(type.equals(Short.class)||type.getName().equals("short"))
		{
			return c.getShort(index);
		}
		if(type.equals(Long.class)||type.getName().equals("long"))
		{
			return c.getLong(index);
		}
		if(type.equals(Float.class)||type.getName().equals("float"))
		{
			return c.getFloat(index);
		}
		if(type.equals(Double.class)||type.getName().equals("double"))
		{
			return c.getDouble(index);
		}
		if(type.equals(String.class)||type.getName().equals("String"))
		{
			return c.getString(index);
		}
		if(type.equals(Byte[].class)||type.getName().equals("byte[]"))
		{
			return c.getBlob(index);
		}
		return null;
	}

	public void SetContentValue(ContentValues contentValues,String key,Object value)
	{
		Log.d("",  String.format( "%s=%s",key,value==null?"null":value.toString()));
		
		if(value==null)
			contentValues.putNull(key);
		if(value instanceof Short)
			contentValues.put(key,(Short) value);
		if(value instanceof Integer)
			contentValues.put(key, (Integer)value);
		if(value instanceof Long)
			contentValues.put(key, (Long)value);
		if(value instanceof Float)
			contentValues.put(key, (Float)value);
		if(value instanceof Double)
			contentValues.put(key, (Double)value);
		if(value instanceof String)
			contentValues.put(key, (String)value);
		if(value instanceof byte[])
			contentValues.put(key,(byte[]) value);
	}
}
