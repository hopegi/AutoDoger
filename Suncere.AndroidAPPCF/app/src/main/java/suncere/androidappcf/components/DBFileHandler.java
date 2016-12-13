package suncere.androidappcf.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

///数据库处理，包括数据库文件拷贝，数据库升级
public class DBFileHandler
{
	//版本与SQL语句映射集合
	private HashMap<Integer,List<String>> versionSQLDic;
	
	private static DBFileHandler _default=new DBFileHandler();
	
	protected DBFileHandler()
	{
		versionSQLDic=new HashMap<Integer,List< String>>();
		AddVersionSQL();
	}
	
	// 待子类重写添加各个版本的升级语句
	protected void AddVersionSQL()
	{
		//AddVersion2SQL();
	}
	
	public static DBFileHandler GetInstance()
	{
		return _default;
	}
	
	//初始化操作，复制数据库
	public void DBCopy(Context context,String sourceDbFileName,String desDBName )
	{
		String path="/data/data/"+ context. getPackageName()+"/databases";
		File file=new File(path);
		try {			
			if(!file.exists()){
			file.mkdir();
			
				file.createNewFile();
				OutputStream os=new FileOutputStream(path+"/"+desDBName);
				InputStream is=context. getResources().getAssets().open(sourceDbFileName);
				int count;
				byte[] buffer=new byte[1024];
				while((count=is.read(buffer))>0)
					os.write(buffer,0,count);
				os.close();
				is.close();
			}		 
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//for example
	private void AddVersion2SQL()
	{
		
		String CityForecastSQL="CREATE TABLE if not exists CityForecast ( "+
				"    id               INTEGER        PRIMARY KEY,"+
				"    CityName         VARCHAR( 10 ),"+
				"    TimePoint        DATETIME       NOT NULL,"+
				"    ForTime          DATETIME       NOT NULL,"+
				"    AQI_Low          INT,"+
				"    AQI_High         INT,"+
				"    ChineseLevel     VARCHAR( 50 ),"+
				"    AQIType          VARCHAR( 50 ),"+
				"    PrimaryPollutant VARCHAR( 50 ),"+
				"    SO2_Low          FLOAT,"+
				"    NO2_Low          FLOAT,"+
				"    CO_Low           FLOAT,"+
				"    O3_Low           FLOAT,"+
				"    NOx_Low          FLOAT,"+
				"    PM2_5_Low        FLOAT,"+
				"    O3_8H_Low        FLOAT,"+
				"    PM10_Low         FLOAT,"+
				"    NO_Low           FLOAT,"+
				"    SO2_High         FLOAT,"+
				"    NO2_High         FLOAT,"+
				"    NO_High          FLOAT,"+
				"    NOx_High         FLOAT,"+
				"    CO_High          FLOAT,"+
				"    O3_High          FLOAT,"+
				"    O3_8H_High       FLOAT,"+
				"    PM2_5_High       FLOAT,"+
				"    PM10_High        FLOAT "+
				");";
		
		int nowVersion=2;
//		this.versionSQLDic.put(6, CityForecastSQL+WeatherInfoSQL);
		this.versionSQLDic.put(nowVersion, new ArrayList<String>());
		this.versionSQLDic.get(nowVersion).add(CityForecastSQL);
	}
	
	///传入升级新旧版本号返回升级用到的SQL集合
	public List<String> BuildUpdateDBSQL(int oldVersion,int newVersion)
	{
		List<String> result=new ArrayList<String>();
		
		for(int i=oldVersion+1;i<=newVersion;i++)
		{
			if(!this.versionSQLDic.containsKey(i))continue;
			result.addAll(versionSQLDic.get(i));
		}
		
		return result;
	}
}
