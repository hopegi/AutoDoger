package suncere.androidapp.tableupgradeter;

import cn.jpush.android.api.c;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import suncere.androidapp.basemodule.BaseDAL;

public class TableVersionDAL extends BaseDAL {

	private final static String TABLENAME="TableVersion";
	
	private final static String TbName="TbName";
	private final static String TbVersion="TbVersion";
	private final static String Tag="Tag";
	
	
	public TableVersionDAL(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void CreateTableVersion()
	{
		this.Open();
		String sql="CREATE TABLE if not exists TableVersion (  TbName varchar(50), TbVersion int);";
		this.ExecuteSQL(sql);
		this.Close();
	}
	
	public int QueryVersionByTbName(String tbName)
	{
		int result=-1;
		
		this.Open();
		
		Cursor cursor=this.db.query(TABLENAME, new String[]{TbVersion}, TbName+"=?", new String[]{tbName}, null, null, null);
		if(cursor.moveToFirst())
			result=cursor.getInt(0);
		
		this.Close();
		
		return result;
	}
	
	public void InitFillTableVersion()
	{
		this.Open();
		
		Cursor cursor=this.db.query("sqlite_master", new String[]{ "name" }, " type='table'", null, null, null, null);
		if(cursor.moveToFirst())
		{
			do {
				
				ContentValues values=new ContentValues();
				values.put(TbName, cursor.getString(0));
				values.put(TbVersion, 1);
				this.db.insert(TABLENAME, null, values);
				
			} while (cursor.moveToNext());
		}
		
		this.Close();
	}
	
	public int QueryTableVersionCount()
	{
		int result=-1;
		this.Open();
		
		Cursor cursor=this.db.query(TABLENAME, new String[]{"COUNT(*)"},null, null, null, null, null);
		if(cursor.moveToFirst())
			result=cursor.getInt(0);
		
		this.Close();
		return result;
	}
	
	public long InsertTableRecord(String tbName,int tbVersion)
	{
		long result=-1;
		this.Open();
		
		ContentValues value=new ContentValues();
		value.put(TbName, tbName);
		value.put(TbVersion, tbVersion);
		result= this.db.insert(TABLENAME, null, value);
		
		this.Close();
		return result;
	}
	
	public int UpdateTableRecord(String tbName,int tbVersion)
	{
		int result=-1;
		this.Open();
		ContentValues value=new ContentValues();
		value.put(TbVersion, tbVersion);
		result=this.db.update(TABLENAME, value, TbName+"=?", new String[]{tbName});
		this.Close();
		return result;
	}
	
	public boolean ExistTable(String tableName)
	{
		boolean result=false;
		
		this.Open();
		Cursor cursor=this.db.query("sqlite_master", new String[]{ "COUNT(*)"}," type='table' AND name=? ",new String[]{  tableName}, null, null, null);
		if(cursor.moveToFirst())
			result=cursor.getInt(0)>0;
		this.Close();
		
		return result;
	}
	
	
	
}
