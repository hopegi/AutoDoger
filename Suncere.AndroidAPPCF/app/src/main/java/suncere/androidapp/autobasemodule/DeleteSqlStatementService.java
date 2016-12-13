package suncere.androidapp.autobasemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import suncere.androidappcf.tools.TypeHelper;

public class DeleteSqlStatementService extends SqlStatementService {

	public DeleteSqlStatementService(AutoBaseModel model) {
		super(model);
	}

	public String GetWhereStr(HashMap<String, Object> otherParams)
	{
		return super.GetWhereStr(otherParams, model.DELETE_CONDITION);
	}

	public String[] GetWhereParameter(HashMap<String, Object> otherParams) 
	{
		return super.GetWhereParameter(otherParams, model.DELETE_CONDITION);
	}

	
}
