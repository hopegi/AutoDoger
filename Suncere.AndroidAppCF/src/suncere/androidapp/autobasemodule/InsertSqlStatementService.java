package suncere.androidapp.autobasemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import suncere.androidappcf.tools.TypeHelper;
import android.content.ContentValues;
import android.util.Log;

public class InsertSqlStatementService extends SqlStatementService {

	public InsertSqlStatementService(AutoBaseModel model) {
		super(model);
	}
	
	public ContentValues GetContentValues()
	{
		ContentValues values=new ContentValues();
		Field[] fieldArr=model.getClass().getDeclaredFields();
		HashMap<String, List<Object>> attributes=model.FieldAtrributes();
		String fieldName;
		String methodName;
		Object tmpValue;
		
		for(int i=0;i<fieldArr.length;i++)
		{
			fieldName=  fieldArr[i].getName();
			 
			 if(attributes.containsKey(fieldName)&&attributes.get(fieldName).contains(model.CAN_NOT_INSERT))continue;
//			 if(fieldName.toLowerCase().equals("id")||fieldName.toLowerCase().equals("_default"))continue;
			 if(fieldName.toLowerCase().equals("_default"))continue;
			 
			try {
				tmpValue= TypeHelper.Getter(model.getClass(), fieldArr[i]).invoke(model, null);
				SetContentValue(values, GetMappingColumnName(fieldName), tmpValue);
				
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return values;
	}
	
}
