package suncere.androidapp.autobasemodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import suncere.androidapp.attributes.IClassAttribute;
import suncere.androidapp.basemodule.BaseModel;

public abstract class AutoBaseModel extends BaseModel {


	public final static int CAN_NOT_UPDATE=1;

	public final static int CAN_NOT_INSERT=2;

	public final static int EXIST_CONDISTION=3;

	public final static int UPDATE_CONDISTION=4;

	public final static int DELETE_CONDITION=5;

	public final static int LASTTIME_CONDITION=6;

	public final static int TIMEPOINT_FIELD=7;

	public final static int NOT_USE_IN_SELECT=8;
	//用于order by 子句并以升序排序
	public final static int ORDER_BY_ASC=9;
	//用于order by 子句并以降序排序
	public final static int ORDER_BY_DESC=10;
	
	///字段与列名映射
	private HashMap<String, String> fieldColumnMapping;
	
	////字段特性
	private HashMap<String, List<Object>> fieldAtrributes;
	
	///映射的Table名称
	private String mappingTableName;
	
	///是否允许调用简便方法设置Attribute
	private boolean attributeSetSwitch;
	
	private List<IClassAttribute> classAttributeLst;
	
	public AutoBaseModel()
	{
		fieldColumnMapping=new HashMap<String, String>();
		fieldAtrributes=new HashMap<String, List<Object>>();
		classAttributeLst=new ArrayList<IClassAttribute>();
		
		attributeSetSwitch=true;
		this.FillingFieldAtrributes(fieldAtrributes);
		attributeSetSwitch=false;
		this.FillingClassAttributes(classAttributeLst);
		this.FillingFieldColumnMapping(fieldColumnMapping);
	}
	
	public HashMap<String, String> FieldColumnMapping()
	{
		return fieldColumnMapping;
	}
	
	public HashMap<String, List<Object>>FieldAtrributes()
	{
		return fieldAtrributes;
	}
	
	public List<IClassAttribute> ClassAttributes()
	{
		return classAttributeLst;
	}
	
	public String MappingTableName()
	{
		if(mappingTableName==null||mappingTableName.length()==0)
		{
			String className= this.getClass().getSimpleName();
			mappingTableName=className.replace("Model", "");
		}
		return mappingTableName;
	}

	public List<Object> IdDefaultAttributes()
	{
		List<Object> idAttributeLst=new ArrayList<Object>();
		idAttributeLst.add(this.CAN_NOT_INSERT);
		idAttributeLst.add(this.CAN_NOT_UPDATE);
		idAttributeLst.add(this.NOT_USE_IN_SELECT);
		return idAttributeLst;
	}
	
	protected void BatchAddFieldAttribute(String fieldName,Object... attributes)
	{
		if(!attributeSetSwitch)return;
		if(fieldAtrributes==null)return;
		if(!fieldAtrributes.containsKey(fieldName))
			fieldAtrributes.put(fieldName, new ArrayList<Object>());
		fieldAtrributes.get(fieldName).addAll(  Arrays.asList(attributes)  );
			
	}
	
	protected void FillingClassAttributes(List<IClassAttribute> collection)
	{
		
	}
	
	protected abstract void FillingFieldColumnMapping(HashMap<String, String> fieldColumnMapping);
	
	protected abstract void FillingFieldAtrributes(HashMap<String, List<Object>> fieldAtrributes) ;
}
