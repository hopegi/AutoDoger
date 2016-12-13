package suncere.androidapp.autobasemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import suncere.androidappcf.tools.TypeHelper;

public class LastTimeSqlStatementService  extends SqlStatementService{

	public LastTimeSqlStatementService(AutoBaseModel model) {
		super(model);
	}

	public String GetWhereStr(HashMap<String, Object> otherParams)
	{
		return super.GetWhereStr(otherParams, model.LASTTIME_CONDITION);
	}

	public String[] GetWhereParameter(HashMap<String, Object> otherParams) 
	{
		return super.GetWhereParameter(otherParams, model.LASTTIME_CONDITION);
	}
	 
	public String[] GetSelectStatement()
	{
		List<String> result=new ArrayList<String>();
		
		Field[] fieldArr=this.GetSelectFields();
		String fieldName;
		for (int i = 0; i < fieldArr.length; i++) {
			 fieldName=  fieldArr[i].getName();
			 
			 if(!model.FieldAtrributes().containsKey(fieldName)||
			 	!model.FieldAtrributes().get(fieldName).contains(AutoBaseModel.TIMEPOINT_FIELD))continue;

			 result.add( this.GetMappingColumnName(fieldName) );
			 
		}
		
		return   result.toArray(new String[result.size()]);
	}
	
	public String GetOrderByStr()
	{
		String result="";
		
		String[] selectStateArr=this.GetSelectStatement();
		for(int i=0; i<selectStateArr.length ; i++)
			result+=String.format("%s DESC,", selectStateArr[i]);

		if(result.length()>0)
			result=result.substring(0,result.length()-1);
		return result;
	}
}
