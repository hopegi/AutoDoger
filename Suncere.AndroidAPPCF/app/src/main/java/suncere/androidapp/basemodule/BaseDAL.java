package suncere.androidapp.basemodule;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import suncere.androidapp.basemodule.DatabaseManager.IInitializer;
import suncere.androidapp.tableupgradeter.TableUpgradeter;
import suncere.androidapp.tableupgradeter.TableVersionDAL;
import suncere.androidappcf.app.SuncereAppParameters;
import suncere.androidappcf.components.DBFileHandler;
import suncere.androidappcf.controls.SuncereApplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDAL implements IInitializer
{
	
	///数据库名字
	public  String DBName()
	{
		return SuncereAppParameters.DbName;
	}
	
	///数据库版本
	public  int VERSION()
	{
		return SuncereAppParameters.DbVersion;
	}
	
//	public final static String packageName="";
	
	protected final Context context;
	
	protected SQLiteDatabase db;
	
	DatabaseHelper dbHelper;
	
	private static TableUpgradeter tableTableUpgradeter;
	
	public BaseDAL(Context context)
	{
//		this.context=context;
		this.context=SuncereApplication.CurrentApplication();
		
//		DatabaseManager.initializeInstance(dbHelper);
		DatabaseManager.SetInitializer(this);
		if(!this.getClass().equals(  TableVersionDAL.class ))
		{
			if(tableTableUpgradeter==null)
				tableTableUpgradeter=new TableUpgradeter(context);
			tableTableUpgradeter.ChecckTable(this);
		}
	}
	
	public SQLiteOpenHelper GetHelper()
	{
		 dbHelper=new DatabaseHelper(this.context);
		 return (SQLiteOpenHelper)dbHelper;
	}
	
	class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context)
		{
			super(context, DBName(),null,VERSION());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			OuterOnCreate(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			OuterOnUpgrade(db,oldVersion,newVersion);
		}
	}

	
	///打开数据库连接
	public BaseDAL Open()
	{
//		db=dbHelper.getWritableDatabase();
		db= DatabaseManager.getInstance().openDatabase();
		return this;
	}
	
	//关闭数据库连接
	public void Close()
	{
		DatabaseManager.getInstance().closeDatabase();
//		dbHelper.close();
	}
	
	public void ExecuteSQL(String sql)
	{
		this.Open();
		this.db.execSQL(sql);
		this.Close();
	}
	
	protected void OuterOnCreate(SQLiteDatabase db) 
	{
		
	}
	
	protected void OuterOnUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		try {
			
			List<String> sqls=DBFileHandler.GetInstance().BuildUpdateDBSQL(oldVersion, newVersion);
			for(String sql:sqls)
				db.execSQL(sql);
			
		} catch (Exception e) {

		}			
	}
	
}
