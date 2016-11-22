package suncere.androidapp.tableupgradeter;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.sparta.xpath.ThisNodeTest;

import suncere.androidapp.basemodule.BaseDAL;
import android.content.Context;

public class TableUpgradeter {

	TableVersionDAL dal;
	
	private static List<Class<?>> hasCheckLst;
	private static Object flag;
	
	static
	{
		hasCheckLst=new ArrayList<Class<?>>();
		flag=new Object();
	}
	
	public TableUpgradeter( Context context )
	{
		dal=new TableVersionDAL(context);
		dal.CreateTableVersion();
		if(dal.QueryTableVersionCount()<1)
			dal.InitFillTableVersion();
	}
	
	public void ChecckTable(BaseDAL dal)
	{
		if(dal.getClass().equals(this.dal.getClass()))return;
		synchronized (flag) {
			if(hasCheckLst.contains(dal.getClass()))
				return;
		}
		if(dal instanceof IAutoCreateTable)
		{
			IAutoCreateTable autoCreateTable=(IAutoCreateTable) dal;
			if(!this.dal.ExistTable(autoCreateTable.GetTableName()))
			{
				String dml= autoCreateTable.CreateTableSQL();
				this.dal.ExecuteSQL(dml);
				this.dal.InsertTableRecord(autoCreateTable.GetTableName(), autoCreateTable.GetTableVersion());
			}
		}

		if(dal instanceof IAutoUpgradeTable)
		{
			IAutoUpgradeTable autoUpgradeTable=(IAutoUpgradeTable)dal;
			UpgradeTable(autoUpgradeTable);
		}
		synchronized (flag) {
			hasCheckLst.add(dal.getClass());
		}
	}
	
	public void UpgradeTable(IAutoUpgradeTable autoUpgradeTable)
	{
		int version= this.dal.QueryVersionByTbName( autoUpgradeTable.GetTableName());
		if(autoUpgradeTable.GetTableVersion()==version)return;
		if(autoUpgradeTable.GetParentUpdrade()!=null)
			UpgradeTable(autoUpgradeTable.GetParentUpdrade());
		List<String> dmlLst=autoUpgradeTable.GetUpgradeSQL();
		if(dmlLst==null) return;
		for(String dml : dmlLst)
		{
			try
			{
				this.dal.ExecuteSQL(dml);
			}
			catch(Exception ex)
			{
				
			}
		}
		this.dal.UpdateTableRecord(autoUpgradeTable.GetTableName(), autoUpgradeTable.GetTableVersion());
	}
}
