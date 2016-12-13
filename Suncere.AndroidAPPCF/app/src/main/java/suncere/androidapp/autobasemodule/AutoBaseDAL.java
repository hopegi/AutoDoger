package suncere.androidapp.autobasemodule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cn.jpush.android.data.f;
import android.R.bool;
import android.app.backup.RestoreObserver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.nfc.tech.IsoDep;
import android.util.Log;
import suncere.androidapp.basemodule.BaseDAL;
import suncere.androidappcf.tools.TypeHelper;

public class AutoBaseDAL extends BaseDAL {

	private static final String logStartMark="-------start----------";
	private static final String logEndMark="-------end----------";
	private static final String LogTag="AutoBaseDAL";
	
	public AutoBaseDAL(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public List<Object> QueryData(AutoBaseModel model,HashMap<String, Object> otherParams)
	{
		List<Object> result=null;
		
		SqlStatementService service=SqlStatementServiceProvider.Current().GetSqlStatementService(model);
		String tableName=model.MappingTableName();
		String[] selectStatement=service.GetSelectStatement();
		String whereStr=service.GetWhereStr(otherParams);
		String[] wherPara=service.GetWhereParameter(otherParams);
		String orderStr=service.GetOrderByStr();
		

		String strSelectStatement="";
		for(String str:selectStatement)
			strSelectStatement+=str+",";
		String strWherePara="";
		for(String str:wherPara)
			strWherePara+=str+",";
		Log.d(LogTag, logStartMark);
		Log.d(LogTag, "QueryData   tablename:"+tableName);
		Log.d(LogTag, String.format( "%s  %s %s %s %s",model.getClass().getName(),  strSelectStatement,whereStr,strWherePara,orderStr));
		
		this.Open();
		Cursor c=this.db.query(tableName,
				selectStatement,
				whereStr,
				wherPara, null, null, orderStr);
		Log.d(LogTag, logEndMark);
		
		if(c.moveToFirst())
		{
			AutoBaseModel item=null;
			Field[] fieldArr=service.GetSelectFields();
			do {
				
				try {
					item= model.getClass().newInstance();
					
					Field field;
					for(int i=0;i<fieldArr.length;i++)
					{
						field=fieldArr[i];
//						String methodName="set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
//						Method setMethod= model.getClass().getMethod(methodName, field.getDeclaringClass());
						Method setMethod=TypeHelper.Setter(model.getClass(), field);
						setMethod.invoke(item, service. GetCursorValue(c,i,field.getType()));
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}  catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				if(result==null)
					result=new ArrayList<Object>();
				if(item!=null)
					result.add( item);
			} while (c.moveToNext());
		}
		this.Close();
		return result;
	}

	public long InsertData(AutoBaseModel model)
	{
		long result=-1;
		SqlStatementService service=SqlStatementServiceProvider.Current().GetSqlStatementService(model);
		this.Open();
		String tableName=model.MappingTableName();
		Log.d(LogTag, logStartMark);
		Log.d(LogTag, "InsertData   tablename:"+tableName);
		

		
		result=this.db.insert(tableName, null, service.GetContentValues());
		Log.d(LogTag, logEndMark);
		this.Close();
		return result;
	}

	public int UpdateData(AutoBaseModel model)
	{
		int result=-1;
		SqlStatementService service=SqlStatementServiceProvider.Current().GetSqlStatementService(model);
		this.Open();

		String tableName=model.MappingTableName();
		Log.d(LogTag, logStartMark);
		Log.d(LogTag, "UpdateData   tablename:"+tableName);
		

		String whereStr=service.GetWhereStr(null);
		String[] wherePara=service.GetWhereParameter(null);
		
		String strWherePara="";
		for(String str:wherePara)
			strWherePara+=str+",";
		Log.d(LogTag, String.format("%s %s", whereStr,strWherePara));
		result=this.db.update(tableName, service.GetContentValues(), whereStr, wherePara);
		Log.d(LogTag, logEndMark);
		
		this.Close();
		return result;
		
	}
	
	public boolean ExistData(AutoBaseModel model)
	{
		boolean result=false;
		
		SqlStatementService serivce=SqlStatementServiceProvider.Current().GetSqlStatementService(model);
		this.Open();
		
		String tableName=model.MappingTableName();
		Log.d(LogTag, logStartMark);
		Log.d(LogTag, "ExistData   tablename:"+tableName);
		String whereStr=serivce.GetWhereStr(null);
		String[] wherePara=serivce.GetWhereParameter(null);
		String strWherePara="";
		for(String str:wherePara)
			strWherePara+=str+",";
		
		Log.d(LogTag, String.format("%s %s", whereStr,strWherePara));
		Cursor cursor=this.db.query(tableName, new String[]{ "COUNT(*)" },whereStr ,wherePara , null, null, null);
		
		Log.d(LogTag, logEndMark);
		if(cursor.moveToFirst())
		{
			result= cursor.getInt(0)>0;
		}
		this.Close();
		return result;
	}
	
	public String GetLastTime(AutoBaseModel model) {
		String result="";
		
		SqlStatementService service=SqlStatementServiceProvider.Current().GetSqlStatementService(model);
		this.Open();

		String tableName=model.MappingTableName();
		Log.d(LogTag, logStartMark);
		Log.d(LogTag, "GetLastTime   tablename:"+tableName);
		
		String[] selectStatement=service.GetSelectStatement();
		String whereStr=service.GetWhereStr(null);
		String[] wherPara=service.GetWhereParameter(null);
		String orderStr=service.GetOrderByStr();
		
		String strSelectStatement="";
		for(String str:selectStatement)
			strSelectStatement+=str+",";
		String strWherePara="";
		for(String str:wherPara)
			strWherePara+=str+",";
		Log.d(LogTag, String.format( "  %s %s %s %s",strSelectStatement,whereStr,strWherePara,orderStr));
		
		Cursor cursor=this.db.query(tableName,
				selectStatement
				, whereStr,
				wherPara, null, null,
				orderStr);
		
		Log.d(LogTag, logEndMark);
		
		if(cursor.moveToFirst())
		{
			result= cursor.getString(0);
		}
		this.Close();
		return result;
	}
}
