package suncere.androidapp.autobasemodule;

import java.util.HashMap;

import suncere.androidappcf.tools.TypeHelper;
import android.R.integer;

public class SqlStatementServiceProvider {

	private final static int SELECT=1;
	private final static int INSERT=2;
	private final static int UPDATE=3;
	private final static int DELETE=4;
	private final static int EXIST=5;
	private final static int LAST_TIME=6;
	
	private final static String DEFAULT_KEY="default";
	
	private static SqlStatementServiceProvider _default=new SqlStatementServiceProvider();
	
	private Object flag=new Object();
	
	public static SqlStatementServiceProvider Current()
	{
		return _default;
	}
	
	private HashMap<String, HashMap<Integer, Class<?>>> serviceSeriesCollection;
	
	private HashMap<Integer, Class<?>> _defaultSeries;
	
	private SqlStatementServiceProvider()
	{
		serviceSeriesCollection=new HashMap<String, HashMap<Integer,Class<?>>>();
		_defaultSeries=new HashMap<Integer, Class<?>>();
		_defaultSeries.put(SELECT, SelectSqlStatementService.class);
		_defaultSeries.put(INSERT, InsertSqlStatementService.class);
		_defaultSeries.put(UPDATE, UpdateSqlStatementService.class);
		_defaultSeries.put(DELETE,DeleteSqlStatementService.class);
		_defaultSeries.put(EXIST, ExistSqlStatementService.class);
		_defaultSeries.put(LAST_TIME, LastTimeSqlStatementService.class);
		serviceSeriesCollection.put(DEFAULT_KEY, _defaultSeries);
	}
	
	public SqlStatementService GetSqlStatementService(AutoBaseModel model)
	{
		return this.GetSqlStatementService(model, this.getInvokeType(4));
	}
	
	public SqlStatementService GetSqlStatementService(AutoBaseModel model,int type)
	{
		SqlStatementService result=null;
		Class<?>sqlStatementServiceType=null;
		synchronized (flag) {
			String key=model.getClass().getSimpleName();
			if(serviceSeriesCollection.containsKey(key))
			{
				HashMap<Integer,Class<?>> series=serviceSeriesCollection.get(key);
				if(series.containsKey(type))
					sqlStatementServiceType= series.get(type);
				else if(_defaultSeries.containsKey(type))
					sqlStatementServiceType= _defaultSeries.get(type);
			}
			else if(_defaultSeries.containsKey(type))
				sqlStatementServiceType= _defaultSeries.get(type);
		}
		result= (SqlStatementService) TypeHelper.NewIntance( sqlStatementServiceType,new Class[]{AutoBaseModel.class },new Object[]{model} );
		return result;
	}
	
	public void AddServiceSeries(String name,HashMap<Integer, Class<?>> series) {
		synchronized (flag) {
			if(!serviceSeriesCollection.containsKey(name))
				serviceSeriesCollection.put(name, series);
		}
	}
	
	private int getInvokeType(int layerIndex)
	{
		if(layerIndex==-1)
			layerIndex=2;
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();  
        String methodName=stack[layerIndex].getMethodName();
        if(methodName.equals("QueryData"))
        {
        	return SELECT;
        }
        else if (methodName.equals("InsertData")) {
			return INSERT;
		}
        else if (methodName.equals("UpdateData")) {
			return UPDATE;
		}
        else if (methodName.equals("ExistData")) {
			return EXIST;
		}
        else if (methodName.equals("GetLastTime")) {
			return LAST_TIME;
		}
        return -1;
	}
}
