package suncere.androidapp.autobasemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.ContentValues;
import suncere.androidappcf.tools.TypeHelper;

public class UpdateSqlStatementService extends SqlStatementService {

	public UpdateSqlStatementService(AutoBaseModel model) {
		super(model);
	}

	public String GetWhereStr(HashMap<String, Object> otherParams)
	{
		return super.GetWhereStr(otherParams, model.UPDATE_CONDISTION);
	}

	public String[] GetWhereParameter(HashMap<String, Object> otherParams) 
	{
		return super.GetWhereParameter(otherParams, model.UPDATE_CONDISTION);
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
			 
			 if(attributes.containsKey(fieldName)&&attributes.get(fieldName).contains(model.CAN_NOT_UPDATE))continue;
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
