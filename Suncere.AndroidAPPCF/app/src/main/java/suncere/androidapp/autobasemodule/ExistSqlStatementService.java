package suncere.androidapp.autobasemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import suncere.androidappcf.tools.TypeHelper;

public class ExistSqlStatementService extends SqlStatementService{

	public ExistSqlStatementService(AutoBaseModel model) {
		super(model);
	}

	public String[] GetWhereParameter(HashMap<String, Object> otherParams) 
	{
		return super.GetWhereParameter(otherParams, model.EXIST_CONDISTION);
	}
	
	public String GetWhereStr(HashMap<String, Object> otherParams)
	{
		return super.GetWhereStr(otherParams, model.EXIST_CONDISTION);
	}
}
